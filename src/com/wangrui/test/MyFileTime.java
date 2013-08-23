package com.wangrui.test;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.util.Date;  
  
public class MyFileTime {  
  public static void main(String[] args) {  
    File file = new File("E:\\工作文档\\1.2.0账户测试系统升级文件-0.142\\20130110_账户系统1_恢复生产到补丁13_后重新升级补丁14-16&统一认证PACK5及补丁1\\整理后\\so");// 文件目录  
    try {  
      for (File demoFile : file.listFiles()) {  
        Process ls_proc = Runtime.getRuntime().exec(  
            "cmd.exe /c dir " + file.getAbsolutePath() + " /tc");// 通过DOS获得的创建时间  
        InputStream is = ls_proc.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(is));  
        String str;  
        int i = 0;  
        while ((str = br.readLine()) != null) {  
          i++;  
          if (i == 6) {  
            System.out.println("Create time:" + str.substring(0, 17));  
          }  
        }  
     
        Date date = new Date(demoFile.lastModified());// 最后修改时间  
        file.setLastModified(date.getTime());
        System.out.println("File Name:"+date.toLocaleString());
        System.out.println("Last Time:" + date);  
        FileInputStream fis = new FileInputStream(demoFile);  
        System.out.println("File Size:" + fis.available());  
      }  
    } catch (Exception ex) {  
    }  
  }  
  
} 
