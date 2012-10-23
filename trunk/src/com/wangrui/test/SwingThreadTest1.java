package com.wangrui.test;

import java.awt.FlowLayout;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.File;

import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JProgressBar;  
import javax.swing.JTextField;  
import javax.swing.SwingUtilities;  

import com.wangrui.client.CollectSysConfig;
public class SwingThreadTest1 extends JFrame {  
     
	
	private static final long serialVersionUID = 1L;  
     private static final String STR = "Completed : ";  
     private JProgressBar progressBar = new JProgressBar();  
     private JTextField text = new JTextField(10);  
     private JButton start = new JButton("Start");  
     private JButton end = new JButton("End");  
     private boolean flag = false;  
     private int count = 0;  
       
     private GoThread t = null;  
       
     private Runnable run = null;//更新组件的线程  
    
     public SwingThreadTest1() {  
         this.setLayout(new FlowLayout());  
         add(progressBar);  
         text.setEditable(false);  
         add(text);  
         add(start);  
         add(end);  
         start.addActionListener(new Start());  
         end.addActionListener(new End());  
           
         run = new Runnable(){//实例化更新组件的线程  
             public void run() {  
               //  progressBar.setValue(count);  
               // text.setText(STR + String.valueOf(count) + "%");  

            	 progressBar.setIndeterminate(true);
            	 progressBar.setString("正在查询中，请稍等！");
            	 progressBar.setStringPainted(true);// 设置在进度条中显示百分比
             }  
         };  
     }  
     private void go() {  
         while (count < 100) {  
             try {  
                 Thread.sleep(100);  
             } catch (InterruptedException e) {  
                 e.printStackTrace();  
             }  
             if (flag) {  
            	 for(int i = 0;i<2000;i++)
            	 {
            		 System.out.println(i);
            		 if(i==1000){
            			 flag = false;
            		 }
            	 }
                 count++;  
                 SwingUtilities.invokeLater(run);//将对象排到事件派发线程的队列中  
             }  
         } 

     }  
     private class Start implements ActionListener {  
         public void actionPerformed(ActionEvent e) {  
             flag = true;  
             if(t == null){  
                 t = new GoThread();  
                 t.start();  
             }  
         }  
     }  
       
     class GoThread extends Thread{  
         public void run() {  
             //do something... 
        	 System.out.println("searching");
        	
       
             go();  
         }  
     }  
     private class End implements ActionListener {  
         public void actionPerformed(ActionEvent e) {  
             flag = false;  
         }  
     }  
     public static void main(String[] args) {  
         SwingThreadTest1 fg = new SwingThreadTest1();  
         fg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
         fg.setSize(300, 100);  
         fg.setVisible(true);  
     }  
}