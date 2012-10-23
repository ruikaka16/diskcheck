package com.wangrui.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabTest implements ActionListener{
	
	private static JButton okButton,cancelButton;
	
	private static JPanel jpanel1,jpanel2,jpanel3;
	
	public TabTest(){
		
		initDialog();
		
	}
	private void initDialog()
	{
	   JFrame owner = new JFrame();
	   owner.setLayout(null);
	   owner.setBounds(300, 200, 445, 330);
	   owner.setTitle("Frame");
	   JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
	   tabbedPane.setBounds(0, 0, 445, 250);
	   // 
	   jpanel1 = new JPanel();
	   tabbedPane.addTab("tab1", jpanel1);
	   // 
	   jpanel2 = new JPanel();
	   tabbedPane.addTab("tab2", jpanel2);
	   // 
	   jpanel3 = new JPanel();
	   tabbedPane.addTab("tab3", jpanel3);
	   owner.add(tabbedPane);
	   okButton = new JButton("确定");
	   okButton.setBounds(280, 260, 60, 30);
	   okButton.addActionListener(this);
	   cancelButton = new JButton("取消");
	   cancelButton.setBounds(360, 260, 60, 30);
	   cancelButton.addActionListener(this);
	   owner.add(okButton);
	   owner.add(cancelButton);
	   owner.setVisible(true);
	}

	public static void main(String args[]){
		
		 new TabTest();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
