package com.hzq.plainplugin.format;

import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;
import static java.util.Collections.singletonList;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/5 13:39
 */
public class HandSaveAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        FormatConfig formatConfig = ConfigCache.getInstance().getMpIdeaModule().getFormatConfig();

        if (formatConfig != null && (!"save".equals(formatConfig.getStyle()) && !"all".equals(formatConfig.getStyle()))) {
            return;
        }

        PsiFile psiFile = e.getData(PSI_FILE);
        Project project = e.getProject();
        Set<PsiFile> psiFilesSet = new HashSet<>(singletonList(psiFile));
        PsiFile[] psiFiles = psiFilesSet.toArray(PsiFile[]::new);

        FormatProcess.processorHand(project, psiFiles);

    }
}
