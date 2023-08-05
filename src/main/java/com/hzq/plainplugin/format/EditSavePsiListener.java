package com.hzq.plainplugin.format;

import com.google.common.collect.Lists;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Huangzq
 * @description
 * @date 2023/7/31 18:43
 */
public class EditSavePsiListener implements FileDocumentManagerListener {

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
        PsiFile[] psiFiles = documents.stream()
                .map(psiDocumentManager::getPsiFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).toArray(PsiFile[]::new);

        OptimizeImportsProcessor optimizeImportsProcessor = new OptimizeImportsProcessor(project, psiFiles, null);
        optimizeImportsProcessor.run();

        ReformatCodeProcessor reformatCodeProcessor = new ReformatCodeProcessor(project, psiFiles, null, true);
        reformatCodeProcessor.run();

    }
}
