package com.hzq.plainplugin.codegenerator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;


/**
 * 功能说明
 *
 * @author 黄震强
 * @version 1.0.0
 * @date 2020/7/9 15:16
 */
public class MyPlusGenerator extends AnAction {

    public MyPlusGenerator() {
        // Set the menu item name.
        super("_CodeGenerator");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        Project project = event.getData(PlatformDataKeys.PROJECT);

        MyDialog formTestDialog = new MyDialog(project);
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        formTestDialog.setResizable(true);
        formTestDialog.show();


//        String txt = Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
//        Messages.showInfoMessage(project, "开始生成1", "info");

    }

    /*public static void main(String[] args) {
        THCodeGenerator.init("Sultan" ,"com.hzq.demo","elevator","module_elevator_data");
    }*/
}
