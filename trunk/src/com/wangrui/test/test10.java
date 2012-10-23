package com.wangrui.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class test10 {
    
	public static String sshExecute(String host, String user, String pwd,
            String command) {
        String osName = System.getProperty("os.name");
        // ps -ef|grep tomcat|grep -v grep|awk '{print $2}'
        StringBuffer sb = new StringBuffer();
        try {
        	
        	
            JSch jsch = new JSch();
           // if (osName.toUpperCase().indexOf("WINDOWS") > -1) {
           //     jsch.setKnownHosts("c:\\known_hosts");
           // } else {
                jsch.setKnownHosts("/root/.ssh/known_hosts");
          //  }
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(pwd);
            session.connect();
            
            java.util.Properties config = new java.util.Properties(); 
        	config.put("StrictHostKeyChecking", "no");
        	session.setConfig(config);
        	
        	JSch.setConfig("StrictHostKeyChecking", "no");
        	
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream in = channel.getInputStream();
            channel.connect();
            int nextChar;
            while (true) {
                while ((nextChar = in.read()) != -1) {
                    sb.append((char) nextChar);
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: "
                            + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static void main(String args[]) throws Exception {
    	sshExecute("10.1.0.22","root","xbzq2005","ls");
    }
}