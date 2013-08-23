package com.wangrui.test;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class ChooseFile {
   
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Choose File v1.0");
        final Container content = frame.getContentPane();
        content.setLayout(new GridBagLayout());
        JButton button = new JButton("Choose File...");
        content.add(button);
       
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //打开文件选择对话框
                JFileChooser file = new JFileChooser();
                file.showOpenDialog(frame);
                //如果参数为null，则显示在显示器中央，现在显示在frame中央
            }
        });
       
        frame.setSize(200,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
