package com.hzq.plainplugin.swing.panel;

import com.alibaba.fastjson.JSONObject;
import com.hzq.plainplugin.codegenerator.CodeGenerator;
import com.hzq.plainplugin.swing.config.CodeGeneratorConfig;
import com.hzq.plainplugin.swing.config.ConfigCache;
import com.hzq.plainplugin.swing.config.Constants;
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
 * @date 2023/8/4 10:30
 */
public class CodeGeneratorPanel {

    private static CodeGeneratorPanel codeGeneratorPanel;

    private CodeGeneratorPanel() {
    }

    public static CodeGeneratorPanel getInstance() {
        if (codeGeneratorPanel == null) {
            codeGeneratorPanel = new CodeGeneratorPanel();
        }
        return codeGeneratorPanel;
    }

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();

    ButtonGroup group = new ButtonGroup();
    private JLabel db = new JLabel("DB 类型：");
    private JRadioButton b1 = new JRadioButton("mysql", true);
    private JRadioButton b2 = new JRadioButton("postgresql");


    private JLabel ip = new JLabel("DB 地址：");
    private JTextField ipContent = new JTextField("127.0.0.1:3306/my_db");

    private JLabel userName = new JLabel("DB用户名：");
    private JTextField userNameContent = new JTextField("root");

    private JLabel pass = new JLabel("DB密码：");
    private JTextField passContent = new JTextField("123456");

    private JLabel name = new JLabel("代码注释作者：");
    private JTextField nameContent = new JTextField("sultan");

    private JLabel module = new JLabel("maven模块路径：");
    private JTextField moduleContent = new JTextField("/sub-module");

    private JLabel mainPkg = new JLabel("代码输出主包名：");
    private JTextField mainPkgContent = new JTextField("com.hzq.buss");

    private JLabel secPkg = new JLabel("代码输出子包名：");
    private JTextField secPkgContent = new JTextField("subpkg");

    private JLabel table = new JLabel("表名：");
    private JTextField tableContent = new JTextField("table_name");

    public void setDbContent(String db) {
        if (db.equals("mysql")) {
            b1.setSelected(true);
            b2.setSelected(false);
        } else {
            b1.setSelected(false);
            b2.setSelected(true);
        }
    }

    public void setIpContent(JTextField ipContent) {
        this.ipContent = ipContent;
    }

    public void setUserNameContent(JTextField userNameContent) {
        this.userNameContent = userNameContent;
    }

    public void setPassContent(JTextField passContent) {
        this.passContent = passContent;
    }

    public void setNameContent(JTextField nameContent) {
        this.nameContent = nameContent;
    }

    public void setModuleContent(JTextField moduleContent) {
        this.moduleContent = moduleContent;
    }

    public void setMainPkgContent(JTextField mainPkgContent) {
        this.mainPkgContent = mainPkgContent;
    }

    public void setSecPkgContent(JTextField secPkgContent) {
        this.secPkgContent = secPkgContent;
    }

    public void setTableContent(JTextField tableContent) {
        this.tableContent = tableContent;
    }

    public JPanel initCenter() {
        center.removeAll();

        //定义表单的主体部分，放置到IDEA会话框的中央位置

        //一个简单的3行2列的表格布局
        center.setLayout(new GridLayout(10, 2));

        //row1 ：db类型
        JPanel btn = new JPanel();
        group.add(b1);
        group.add(b2);
        btn.setLayout(new GridLayout(1, 2));
        btn.add(b1);
        btn.add(b2);

        center.add(db);
        center.add(btn);

        //row2：作者
        center.add(name);
        center.add(nameContent);

        //row3：ip
        center.add(ip);
        center.add(ipContent);

        //row4：用户名
        center.add(userName);
        center.add(userNameContent);

        //row5：密码
        center.add(pass);
        center.add(passContent);

        //row6：作者
        center.add(module);
        center.add(moduleContent);

        //row7：主包
        center.add(mainPkg);
        center.add(mainPkgContent);

        //row8：子包
        center.add(secPkg);
        center.add(secPkgContent);

        //row9：表名
        center.add(table);
        center.add(tableContent);

        return center;
    }

    public JPanel initSouth(Project project) {
        south.removeAll();

        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("生成");
        //水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);

        JButton save = new JButton("保存");
        //水平居中
        save.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        save.setVerticalAlignment(SwingConstants.CENTER);

        south.add(submit);
        south.add(save);

        //按钮事件绑定
        submit.addActionListener(e -> {

            String db = "mysql";
            Enumeration<AbstractButton> elements = group.getElements();
            while (elements.hasMoreElements()) {
                AbstractButton abstractButton = elements.nextElement();
                if (abstractButton.isSelected()) {
                    db = abstractButton.getText();
                    break;
                }
            }

            //获取到name和age
            String name = nameContent.getText();
            String ip = ipContent.getText();
            String user = userNameContent.getText();
            String pass = passContent.getText();

            String main = mainPkgContent.getText();
            String sec = secPkgContent.getText();
            String table = tableContent.getText();
            String module = moduleContent.getText();

            String path = project.getBasePath();
            try {
                CodeGenerator.init(db, path, ip, user, pass, name, main, sec, table, module);
            } catch (Exception ex) {
                Messages.showMessageDialog(project, ex.getMessage(), "Error", Messages.getErrorIcon());
            }

            Messages.showMessageDialog(project, "生成完毕", "info", Messages.getInformationIcon());
            //关闭对话框
//            myDialog.doCancelAction();

//            myDialog.close(1);

        });


        save.addActionListener(e -> {
            String db = "mysql";
            Enumeration<AbstractButton> elements = group.getElements();
            while (elements.hasMoreElements()) {
                AbstractButton abstractButton = elements.nextElement();
                if (abstractButton.isSelected()) {
                    db = abstractButton.getText();
                    break;
                }
            }
            //获取到name和age
            String name = nameContent.getText();
            String ip = ipContent.getText();
            String user = userNameContent.getText();
            String pass = passContent.getText();

            String main = mainPkgContent.getText();
            String sec = secPkgContent.getText();
            String table = tableContent.getText();
            String module = moduleContent.getText();

            CodeGeneratorConfig mp = new CodeGeneratorConfig();
            mp.setDb(db);
            mp.setIp(ip);
            mp.setMain(main);
            mp.setModule(module);
            mp.setName(name);
            mp.setPass(pass);
            mp.setSec(sec);
            mp.setTable(table);
            mp.setUser(user);

            ConfigCache.getInstance().getMpIdeaModule().setCodeGeneratorConfig(mp);

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
