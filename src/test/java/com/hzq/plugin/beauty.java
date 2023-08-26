package com.hzq.plugin;

import javax.swing.*;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/8 17:39
 */
public class beauty {
    private JTabbedPane format;
    private JRadioButton realtime;
    private JRadioButton save;
    private JRadioButton all;
    private JLabel formattype;
    private JButton saveconf;
    private JPanel mypanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("beauty");
        frame.setContentPane(new beauty().mypanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
