package com.wangrui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.nio.charset.Charset;
import java.util.Properties;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;

import com.jcraft.jsch.ChannelExec;

import com.jcraft.jsch.JSch;

import com.jcraft.jsch.JSchException;

import com.jcraft.jsch.Session;

 
public class JSchDemo2 
{
public void Connect(String ip,String username,String password)throws JSchException
{
   JSch jsch=new JSch();
   Session session=jsch.getSession(username, ip,22);
   session.setPassword(password);
   Properties pop=new Properties();
   pop.setProperty("StrictHostKeyChecking","no");
   session.setConfig(pop);
   session.connect();
   Channel channel=session.openChannel("exec");
   ((ChannelExec)channel).setCommand("df -k | sed -e '1d;/ /!N;s/\\( \\)\\{1,\\}/\\1/g;'");
   try {
    InputStream in=channel.getInputStream();
   
    File file=new File("c:\\queryresult_linux.txt");
    FileOutputStream fos=new FileOutputStream(file);
    channel.connect();
    FileCopyUtils.copy(in, fos);// 将结果存放至文件
    
    channel.disconnect();   
    session.disconnect();
   
   
//    while (true)
//    {
//     if (!channel.isClosed())
//     {
//      try {
//       Thread.sleep(50);
//      } catch (InterruptedException e) {
//       e.printStackTrace();
//      }
//      continue;
//     }
//     int exitStatus = channel.getExitStatus();
//     if (exitStatus != 0)
//     {
//      System.out.println("existStatus="+exitStatus);
//     }
//     break;
//    }
   } catch (IOException e) {
    e.printStackTrace();
   }
  
  
}
public static void main(String args[])
{
   try {
    new JSchDemo2().Connect("10.1.0.22","root","xbzq2005");
   } catch (JSchException e) {
    e.printStackTrace();
   }
}
}

