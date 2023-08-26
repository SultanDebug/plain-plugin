package com.hzq.plainplugin.format;

import com.google.common.collect.Lists;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

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

        FormatProcess.processor(this.project, this.psiDocumentManager, Lists.newArrayList(document));
    }

    @Override
    public void beforeAllDocumentsSaving() {
        FormatConfig formatConfig = ConfigCache.getInstance().getMpIdeaModule().getFormatConfig();

        if (formatConfig != null && (!"realtime".equals(formatConfig.getStyle()) && !"all".equals(formatConfig.getStyle()))) {
            return;
        }

        List<Document> unsavedDocuments = Arrays.asList(FileDocumentManager.getInstance().getUnsavedDocuments());
        if (!unsavedDocuments.isEmpty()) {
            FormatProcess.processor(this.project, this.psiDocumentManager, unsavedDocuments);
        }
    }


}
