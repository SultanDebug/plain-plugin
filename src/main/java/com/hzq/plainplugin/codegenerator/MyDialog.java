
package com.hzq.plainplugin.codegenerator;

import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 功能说明
 *
 * @author 黄震强
 * @version 1.0.0
 * @date 2020/7/10 14:41
 */
public class MyDialog extends DialogWrapper {
    private Project project ;

    //swing样式类
    private MyFormSwing formSwing ;

    public MyDialog(@Nullable Project project) {
        super(project);
        // 设置会话框标题
        setTitle("代码生成器");
        this.project = project;
        formSwing = new MyFormSwing();
        File file = new File("./mp-idea.txt");
        if(file.exists()){
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream byteBufferOutputStream = new ByteArrayOutputStream();
                int len = 0;
                while((len = inputStream.read(bytes))!=-1){
                    byteBufferOutputStream.write(bytes,0,len);
                }
                String s = new String(byteBufferOutputStream.toByteArray(), StandardCharsets.UTF_8);

                MpIdeaConfig mpa = JSONObject.parseObject(s,MpIdeaConfig.class);

                formSwing.setDbContent(mpa.getDb());
                formSwing.setIpContent(new JTextField(mpa.getIp()));
                formSwing.setMainPkgContent(new JTextField(mpa.getMain()));
                formSwing.setModuleContent(new JTextField(mpa.getModule()));
                formSwing.setNameContent(new JTextField(mpa.getName()));
                formSwing.setPassContent(new JTextField(mpa.getPass()));
                formSwing.setSecPkgContent(new JTextField(mpa.getSec()));
                formSwing.setTableContent(new JTextField(mpa.getTable()));
                formSwing.setUserNameContent(new JTextField(mpa.getUser()));

                inputStream.close();
                byteBufferOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最上方的位置
    @Override
    protected JComponent createNorthPanel() {
        return formSwing.initNorth();
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最下方的位置
    @Override
    protected JComponent createSouthPanel() {
        return formSwing.initSouth(project,this);
    }

    @Override
    protected JComponent createCenterPanel() {
        return formSwing.initCenter();
    }
}
