package com.wangrui.test;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuTestPop extends JFrame implements ActionListener {
    public static void main(String arg[]) {
    	MenuTestPop ms = new MenuTestPop();
    }
    public MenuTestPop() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//改变框架关闭时的默认动作为退出
        JMenuBar menuBar = buildMenuBar();
        setJMenuBar(menuBar);
        setSize(300,200);
        setLocation(250,150);
        setVisible(true);
    }
    JMenuBar buildMenuBar() { 
         JMenuBar menuBar = new JMenuBar();

         JMenu fileMenu = new JMenu("File");//一级菜单
         fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);//当外观显示弹出菜单时，它选择不使用轻量级（纯 Java 的）弹出菜单。
         menuBar.add(fileMenu);

         JMenuItem openMenuItem = new JMenuItem("Open");//二级菜单
         openMenuItem.addActionListener(this);
         fileMenu.add(openMenuItem);

         JMenuItem saveMenuItem = new JMenuItem("Save");
         saveMenuItem.addActionListener(this);
         fileMenu.add(saveMenuItem);

         JMenuItem exitMenuItem = new JMenuItem("Exit");
         exitMenuItem.addActionListener(this);
         fileMenu.add(exitMenuItem);

         JMenu decideMenu = new JMenu("Decide");//一级菜单
         fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);
         menuBar.add(decideMenu);

         JMenuItem fishMenuItem = new JMenuItem("Fish");
         fishMenuItem.addActionListener(this);
         decideMenu.add(fishMenuItem);

         JMenuItem cutBaitMenuItem = new JMenuItem("Cut Bait");
         cutBaitMenuItem.addActionListener(this);
         decideMenu.add(cutBaitMenuItem);

         return(menuBar);
    }
    public void actionPerformed(ActionEvent e) {
        String selection = e.getActionCommand();
        System.out.println(selection);
        if(selection.equals("Exit")) {
            System.exit(0);
        }
    }
} 