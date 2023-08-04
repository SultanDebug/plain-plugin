package com.hzq.plainplugin.swing.panel;

import com.alibaba.fastjson.JSONObject;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.Constants;
import com.hzq.plainplugin.swing.config.FormatConfig;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/4 10:31
 */
public class FormatPanel {
    private static FormatPanel formatPanel;

    private FormatPanel() {
    }

    public static FormatPanel getInstance() {
        if (formatPanel == null) {
            formatPanel = new FormatPanel();
        }
        return formatPanel;
    }

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();

    ButtonGroup group = new ButtonGroup();
    private JLabel type = new JLabel("格式化方式：");
    private JRadioButton b1 = new JRadioButton("实时格式化", true);
    private JRadioButton b2 = new JRadioButton("手动保存格式化");

    {
        b1.setName("realtime");
        b2.setName("save");
    }

    public void setType(String type) {
        if ("realtime".equals(type)) {
            b1.setSelected(true);
            b2.setSelected(false);
        } else {
            b1.setSelected(false);
            b2.setSelected(true);
        }
    }

    public JPanel initCenter() {
        center.removeAll();

        //定义表单的主体部分，放置到IDEA会话框的中央位置

        //一个简单的3行2列的表格布局
        center.setLayout(new FlowLayout());

        //row1 ：db类型
        JPanel btn = new JPanel();
        group.add(b1);
        group.add(b2);
        btn.setLayout(new FlowLayout());
        btn.add(b1);
        btn.add(b2);

        center.add(type);
        center.add(btn);

        return center;
    }

    public JPanel initSouth(Project project) {
        south.removeAll();
        JButton save = new JButton("保存");
        //水平居中
        save.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        save.setVerticalAlignment(SwingConstants.CENTER);
        south.add(save);

        save.addActionListener(e -> {
            String type = "realtime";
            Enumeration<AbstractButton> elements = group.getElements();
            while (elements.hasMoreElements()) {
                AbstractButton abstractButton = elements.nextElement();
                if (abstractButton.isSelected()) {
                    type = abstractButton.getName();
                    break;
                }
            }

            FormatConfig mp = new FormatConfig();
            mp.setStyle(type);
            ConfigCache.getInstance().getMpIdeaModule().setFormatConfig(mp);

            try {
                //本地文件记忆
                File file = new File(Constants.CONFIG_PATH);
                if (!file.exists()) {
                    try {
                        //如果文件不存在则创建文件
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(JSONObject.toJSONString(ConfigCache.getInstance().getMpIdeaModule()).getBytes());
                outputStream.close();
            } catch (Exception ex) {
                Messages.showMessageDialog(project, ex.getMessage(), "Error", Messages.getErrorIcon());
            }
            Messages.showMessageDialog(project, "保存成功", "info", Messages.getInformationIcon());
        });

        return south;
    }
}
