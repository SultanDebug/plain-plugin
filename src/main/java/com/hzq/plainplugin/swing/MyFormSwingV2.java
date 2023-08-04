package com.hzq.plainplugin.swing;

import com.hzq.plainplugin.swing.panel.CodeGeneratorPanel;
import com.hzq.plainplugin.swing.panel.FormatPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * 功能说明
 *
 * @author 黄震强
 * @version 1.0.0
 * @date 2020/7/10 14:44
 */
public class MyFormSwingV2 {
    private JPanel north = new JPanel();

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();

    public JPanel initCenter(DialogWrapper dialog, Project project) {
        JPanel jPanel = new JPanel();
        JBTabbedPane jTabbedPane = new JBTabbedPane();

        JPanel jPanel1 = new JPanel();
        BorderLayout borderLayout1 = new BorderLayout();
        jPanel1.setLayout(borderLayout1);
        jPanel1.add(center(dialog, "code", project), BorderLayout.NORTH);
        jPanel1.add(initSouth(dialog, "code", project), BorderLayout.SOUTH);

        jTabbedPane.add("代码生成", jPanel1);

        JPanel jPanel2 = new JPanel();
        BorderLayout borderLayout2 = new BorderLayout();
        jPanel2.setLayout(borderLayout2);
        jPanel2.add(center(dialog, "format", project), BorderLayout.NORTH);
        jPanel2.add(initSouth(dialog, "format", project), BorderLayout.SOUTH);


        jTabbedPane.add("代码格式化", jPanel2);

//        jTabbedPane.setLayout(new FormLayout());

        jPanel.add(jTabbedPane);

        return jPanel;
    }

    private static JPanel center(DialogWrapper dialog, String type, Project project) {
        if ("code".equals(type)) {
            return CodeGeneratorPanel.getInstance().initCenter();
        } else if ("format".equals(type)) {
            return FormatPanel.getInstance().initCenter();
        } else {
            JLabel jLabel = new JLabel("暂不支持");
            JPanel jPanel = new JPanel();
            jPanel.add(jLabel);
            return jPanel;
        }
    }

    public JPanel initSouth(DialogWrapper dialog, String type, Project project) {
        if ("code".equals(type)) {
            return CodeGeneratorPanel.getInstance().initSouth(project);
        } else if ("format".equals(type)) {
            return FormatPanel.getInstance().initSouth(project);
        } else {
            JLabel jLabel = new JLabel("暂不支持");
            JPanel jPanel = new JPanel();
            jPanel.add(jLabel);
            return jPanel;
        }
    }

}
