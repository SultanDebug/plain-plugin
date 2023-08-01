
package com.hzq.plainplugin.codegenerator;

import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 功能说明
 *
 * @author 黄震强
 * @version 1.0.0
 * @date 2020/7/10 14:44
 */
public class MyFormSwing {
    private JPanel north = new JPanel();

    private JPanel center = new JPanel();

    private JPanel btn = new JPanel();

    private JPanel south = new JPanel();

    //为了让位于底部的按钮可以拿到组件内容，这里把表单组件做成类属性
    /*private JLabel r1 = new JLabel("输出：");
    private JLabel r2 = new JLabel("NULL");*/

    ButtonGroup group = new ButtonGroup();
    private JLabel db = new JLabel("DB 类型：");
    private JRadioButton b1 = new JRadioButton("mysql", true);
    private JRadioButton b2 = new JRadioButton("postgresql");


    private JLabel ip = new JLabel("DB 地址（如127.0.0.1:3306/my_db）：");
    private JTextField ipContent = new JTextField("127.0.0.1:3306/my_db");

    private JLabel userName = new JLabel("DB用户名：");
    private JTextField userNameContent = new JTextField("root");

    private JLabel pass = new JLabel("DB密码：");
    private JTextField passContent = new JTextField("123456");

    private JLabel name = new JLabel("代码注释作者：");
    private JTextField nameContent = new JTextField("sultan");

    private JLabel module = new JLabel("maven模块路径（如 /sub-module）：");
    private JTextField moduleContent = new JTextField("/sub-module");

    private JLabel mainPkg = new JLabel("代码输出主包名（如com.hzq）：");
    private JTextField mainPkgContent = new JTextField("com.hzq");

    private JLabel secPkg = new JLabel("代码输出子包名（如subpkg）：");
    private JTextField secPkgContent = new JTextField("subpkg");

    private JLabel table = new JLabel("表名（table_name）：");
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

    public JPanel initNorth() {

        //定义表单的标题部分，放置到IDEA会话框的顶部位置

        JLabel title = new JLabel("代码生成参数设置");
        //字体样式
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        //水平居中
        title.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        title.setVerticalAlignment(SwingConstants.CENTER);
        north.add(title);

        return north;
    }

    public JPanel initCenter() {

        //定义表单的主体部分，放置到IDEA会话框的中央位置

        //一个简单的3行2列的表格布局
        center.setLayout(new GridLayout(9, 2));

        //row1：按钮事件触发后将结果打印在这里
        /*
        //设置字体颜色
        r1.setForeground(new Color(255, 47, 93));
        center.add(r1);
        //设置字体颜色
        r2.setForeground(new Color(139, 181, 20));
        center.add(r2);*/

        //row1 ：db类型
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

    public JPanel initSouth(Project project, MyDialog myDialog) {

        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("提交");
        //水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);

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
            //刷新r2标签里的内容，替换为name和age
//            r2.setText(String.format("name:%s, age:%s", name, age));

            MpIdeaConfig mp = new MpIdeaConfig();
            mp.setDb(db);
            mp.setIp(ip);
            mp.setMain(main);
            mp.setModule(module);
            mp.setName(name);
            mp.setPass(pass);
            mp.setSec(sec);
            mp.setTable(table);
            mp.setUser(user);


            String path = project.getBasePath();
            try {
//                THCodeGenerator.init(path,"Sultan" ,"com.hzq.demo","elevator","module_elevator_data");
                CodeGenerator.init(db, path, ip, user, pass, name, main, sec, table, module);

                //本地文件记忆
                File file = new File("./mp-idea.txt");
                if (!file.exists()) {
                    try {
                        //如果文件不存在则创建文件
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(JSONObject.toJSONString(mp).getBytes());
                outputStream.close();
//            THCodeGenerator.init(path);
            } catch (Exception ex) {
                Messages.showMessageDialog(project, ex.getMessage(), "Error", Messages.getErrorIcon());
            }

            Messages.showMessageDialog(project, "生成完毕", "info", Messages.getInformationIcon());
            //关闭对话框
//            myDialog.doCancelAction();

//            myDialog.close(1);

        });

        return south;
    }
}
