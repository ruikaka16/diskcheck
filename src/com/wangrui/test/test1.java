package com.wangrui.test;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.wangrui.client.CollectSysConfig;


public class test1 {

    public static void main(String[] args) throws Exception {  
    
		File batFile = new File("c://"); // vbs的目录
		String[] cpCmd   = new String[]{"wscript", "c://vbsCommd.vbs"};  
		//System.out.println("vbs目录："+CollectSysConfig.filePathresult);
		final File[] batFiles = batFile.listFiles();
		if (batFiles != null) {
		
					Runtime rn = Runtime.getRuntime();
					Process p = null;
					try {
						
						//p = rn.exec("wscript" + batFiles[i].getPath());
						//System.out.println("vbs命令所在目录："+batFiles[i].getPath());
					    p=  rn.exec(cpCmd);
						int exitValue = p.waitFor();
						
//						}
						System.out.println("返回值：" + exitValue);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				
			
		} else {
			JOptionPane.showMessageDialog(null, "文件不存在!");
		}
    }  
} 