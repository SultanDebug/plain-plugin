package com.hzq.plainplugin.swing;

import com.hzq.plainplugin.swing.config.CodeGeneratorConfig;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.hzq.plainplugin.swing.panel.CodeGeneratorPanel;
import com.hzq.plainplugin.swing.panel.FormatPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 功能说明
 *
 * @author 黄震强
 * @version 1.0.0
 * @date 2020/7/10 14:41
 */
public class MyDialogV2 extends DialogWrapper {
    private Project project;

    //swing样式类
    private MyFormSwingV2 formSwing = new MyFormSwingV2();

    public MyDialogV2(@Nullable Project project) {
        super(project);
        // 设置会话框标题
        setTitle("Plain Plugin");
        setOKActionEnabled(false);
        this.project = project;

        CodeGeneratorConfig codeGenerator = ConfigCache.getInstance().getMpIdeaModule().getCodeGeneratorConfig();
        FormatConfig format = ConfigCache.getInstance().getMpIdeaModule().getFormatConfig();

        if (codeGenerator != null) {
            CodeGeneratorPanel codeGeneratorPanel = CodeGeneratorPanel.getInstance();
            codeGeneratorPanel.setDbContent(codeGenerator.getDb());
            codeGeneratorPanel.setIpContent(new JTextField(codeGenerator.getIp()));
            codeGeneratorPanel.setMainPkgContent(new JTextField(codeGenerator.getMain()));
            codeGeneratorPanel.setModuleContent(new JTextField(codeGenerator.getModule()));
            codeGeneratorPanel.setNameContent(new JTextField(codeGenerator.getName()));
            codeGeneratorPanel.setPassContent(new JTextField(codeGenerator.getPass()));
            codeGeneratorPanel.setSecPkgContent(new JTextField(codeGenerator.getSec()));
            codeGeneratorPanel.setTableContent(new JTextField(codeGenerator.getTable()));
            codeGeneratorPanel.setUserNameContent(new JTextField(codeGenerator.getUser()));
        }

        if (format != null) {
            FormatPanel formatPanel = FormatPanel.getInstance();
            formatPanel.setType(format.getStyle());
        }

        //触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    /*// 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最上方的位置
    @Override
    protected JComponent createNorthPanel() {
        return formSwing.initNorth(project);
    }*/

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最下方的位置
    @Override
    protected JComponent createSouthPanel() {
        return null;
    }

    @Override
    protected JComponent createCenterPanel() {
        return formSwing.initCenter(this, project);
    }
}
