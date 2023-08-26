package com.hzq.plainplugin.format;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.PsiErrorElementUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/26 11:39
 */
public class FormatProcess {
    private static final Logger LOGGER = Logger.getInstance(FormatProcess.class);

    public static void processor(Project project, PsiDocumentManager psiDocumentManager, List<Document> documents) {
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


        extracted(project, psiFilesEligible);

    }

    public static void processorHand(Project project, PsiFile[] psiFilesEligible) {
        for (PsiFile psiFile : psiFilesEligible) {
            if (!isPsiFileEligible(project, psiFile)) {
                return;
            }
        }
        extracted(project, psiFilesEligible);
    }

    private static void extracted(Project project, PsiFile[] psiFilesEligible) {
        OptimizeImportsProcessor optimizeImportsProcessor = new OptimizeImportsProcessor(project, psiFilesEligible, null);
        optimizeImportsProcessor.run();

        ReformatCodeProcessor reformatCodeProcessor = new ReformatCodeProcessor(project, psiFilesEligible, null, false);
        reformatCodeProcessor.run();

        RearrangeCodeProcessor rearrangeCodeProcessor = new RearrangeCodeProcessor(project, psiFilesEligible, CodeInsightBundle.message("command.rearrange.code"), null, false);
        rearrangeCodeProcessor.run();
    }


    private static boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null
                && isProjectValid(project)
                && isPsiFileValid(psiFile)
                && isPsiFileFresh(psiFile)
                && isPsiFileInProject(project, psiFile)
                && isPsiFileNoError(project, psiFile);
    }

    private static boolean isProjectValid(Project project) {
        boolean valid = project.isInitialized() && !project.isDisposed();
        if (!valid) {
            LOGGER.info("项目失效，可能未初始化完成或者已销毁.");
        }
        return valid;
    }

    private static boolean isPsiFileValid(PsiFile psiFile) {
        boolean valid = psiFile.isValid();
        if (!valid) {
            LOGGER.info(String.format("文件 %s 当前上下文中不可访问或无效.", psiFile));
        }
        return valid;
    }

    private static boolean isPsiFileFresh(PsiFile psiFile) {
        boolean isFresh = psiFile.getModificationStamp() != 0;
        if (!isFresh) {
            LOGGER.info(String.format("文件 %s 修改标记过期.", psiFile));
        }
        return isFresh;
    }

    private static boolean isPsiFileInProject(Project project, @NotNull PsiFile psiFile) {
        boolean inProject = ProjectRootManager.getInstance(project).getFileIndex().isInContent(psiFile.getVirtualFile());
        if (!inProject) {
            LOGGER.info(String.format("文件 %s 不在当前项目 %s. 文件属于 %s",
                    psiFile,
                    project,
                    psiFile.getProject()));
        }
        return inProject;
    }

    private static boolean isPsiFileNoError(Project project, PsiFile psiFile) {
        boolean hasErrors = PsiErrorElementUtil.hasErrors(project, psiFile.getVirtualFile());
        if (hasErrors) {
            LOGGER.info(String.format("文件 %s 有编译错误", psiFile));
        }
        return !hasErrors;
    }
}
