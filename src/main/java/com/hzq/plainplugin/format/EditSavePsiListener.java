package com.hzq.plainplugin.format;

import com.google.common.collect.Lists;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.PsiErrorElementUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author Huangzq
 * @description
 * @date 2023/7/31 18:43
 */
public class EditSavePsiListener implements FileDocumentManagerListener {
    private static final Logger LOGGER = Logger.getInstance(EditSavePsiListener.class);

    private final Project project;
    private final PsiDocumentManager psiDocumentManager;

    public EditSavePsiListener(Project project) {
        this.project = project;
        psiDocumentManager = PsiDocumentManager.getInstance(project);
    }

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        FormatConfig formatConfig = ConfigCache.getInstance().getMpIdeaModule().getFormatConfig();

        if (formatConfig != null && (!"realtime".equals(formatConfig.getStyle()) && !"all".equals(formatConfig.getStyle()))) {
            return;
        }

        processor(Lists.newArrayList(document));
    }

    @Override
    public void beforeAllDocumentsSaving() {
        FormatConfig formatConfig = ConfigCache.getInstance().getMpIdeaModule().getFormatConfig();

        if (formatConfig != null && (!"realtime".equals(formatConfig.getStyle()) && !"all".equals(formatConfig.getStyle()))) {
            return;
        }

        List<Document> unsavedDocuments = Arrays.asList(FileDocumentManager.getInstance().getUnsavedDocuments());
        if (!unsavedDocuments.isEmpty()) {
            processor(unsavedDocuments);
        }
    }

    public void processor(List<Document> documents) {
        if (project.isDisposed()) {
            return;
        }
        Set<PsiFile> psiFiles = documents.stream()
                .map(psiDocumentManager::getPsiFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (psiFiles.isEmpty()) {
            return;
        }

        PsiFile[] psiFilesEligible = psiFiles.stream()
                .filter(psiFile -> isPsiFileEligible(project, psiFile))
                .collect(toSet()).toArray(PsiFile[]::new);


        OptimizeImportsProcessor optimizeImportsProcessor = new OptimizeImportsProcessor(project, psiFilesEligible, null);
        optimizeImportsProcessor.run();

        ReformatCodeProcessor reformatCodeProcessor = new ReformatCodeProcessor(project, psiFilesEligible, null, false);
        reformatCodeProcessor.run();

        RearrangeCodeProcessor rearrangeCodeProcessor = new RearrangeCodeProcessor(project, psiFilesEligible, CodeInsightBundle.message("command.rearrange.code"), null, false);
        rearrangeCodeProcessor.run();

    }


    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null
                && isProjectValid(project)
                && isPsiFileValid(psiFile)
                && isPsiFileFresh(psiFile)
                && isPsiFileInProject(project, psiFile)
                && isPsiFileNoError(project, psiFile);
    }

    private boolean isProjectValid(Project project) {
        boolean valid = project.isInitialized() && !project.isDisposed();
        if (!valid) {
            LOGGER.info("项目失效，可能未初始化完成或者已销毁.");
        }
        return valid;
    }

    private boolean isPsiFileValid(PsiFile psiFile) {
        boolean valid = psiFile.isValid();
        if (!valid) {
            LOGGER.info(String.format("文件 %s 当前上下文中不可访问或无效.", psiFile));
        }
        return valid;
    }

    private boolean isPsiFileFresh(PsiFile psiFile) {
        boolean isFresh = psiFile.getModificationStamp() != 0;
        if (!isFresh) {
            LOGGER.info(String.format("文件 %s 修改标记过期.", psiFile));
        }
        return isFresh;
    }

    private boolean isPsiFileInProject(Project project, @NotNull PsiFile psiFile) {
        boolean inProject = ProjectRootManager.getInstance(project).getFileIndex().isInContent(psiFile.getVirtualFile());
        if (!inProject) {
            LOGGER.info(String.format("文件 %s 不在当前项目 %s. 文件属于 %s",
                    psiFile,
                    project,
                    psiFile.getProject()));
        }
        return inProject;
    }

    private boolean isPsiFileNoError(Project project, PsiFile psiFile) {
        boolean hasErrors = PsiErrorElementUtil.hasErrors(project, psiFile.getVirtualFile());
        if (hasErrors) {
            LOGGER.info(String.format("文件 %s 有编译错误", psiFile));
        }
        return !hasErrors;
    }

}
