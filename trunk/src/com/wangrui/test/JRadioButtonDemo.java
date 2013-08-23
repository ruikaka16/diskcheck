package com.wangrui.test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class JRadioButtonDemo {
	JLabel display;
	public JRadioButtonDemo()
	{
	   JFrame Frame=new JFrame("JRadioButton");//创建一个窗体
	   JRadioButton JRB1=new JRadioButton("石头");//创建JRadioButton对象
	   JRB1.setMnemonic(KeyEvent.VK_S);//设置等效按键
	   JRB1.setActionCommand("石头");//设置命令字符串
	   JRadioButton JRB2=new JRadioButton("剪刀");
	   JRB2.setMnemonic(KeyEvent.VK_J);//设置等效按键
	   JRB2.setActionCommand("剪刀");//设置命令字符串
	   JRadioButton JRB3=new JRadioButton("布");
	   JRB3.setMnemonic(KeyEvent.VK_B);//设置等效按键
	   JRB3.setActionCommand("布");//设置命令字符串
	   ButtonGroup group=new ButtonGroup();//将单选按钮组成一组
	   group.add(JRB1);
	   group.add(JRB2);
	   group.add(JRB3);
	   RadioListener myListener=new RadioListener();//为单选按钮添加事件监听器
	   JRB1.addActionListener((ActionListener) myListener);
	   JRB2.addActionListener(myListener);
	   JRB3.addActionListener(myListener);
	  
	   display=new JLabel("这是一个JRadioButton的实例");
	   Frame.getContentPane().setLayout(new GridLayout(0,1));//设置窗体的布局
	   Frame.getContentPane().add(JRB1);
	   Frame.getContentPane().add(JRB2);
	   Frame.getContentPane().add(JRB3);
	   Frame.getContentPane().add(display);
	   Frame.setSize(200,150);
	   Frame.setVisible(true);//设置窗体的可见性
	}

	//JRadioGroup事件服务类
	class RadioListener implements ActionListener
	{
	   public void actionPerformed(ActionEvent e)
	   {
	    display.setText("你选择了："+e.getActionCommand());
	   }
	}
	/**
	* @param args
	*/
	public static void main(String[] args) {
	   // TODO Auto-generated method stub
	   JRadioButtonDemo JRBD=new JRadioButtonDemo();//创建对象
	}
	}
