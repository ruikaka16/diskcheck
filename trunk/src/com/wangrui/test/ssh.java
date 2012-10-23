package com.wangrui.test;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
 
public class ssh{
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 22;
		String username = "root";
		String host = "10.1.0.22";
		String pass = "xbzq2005";
		String khfile = "/root/.ssh/known_hosts";
		String identityfile = "/root/.ssh/id_rsa_1024";
 
		JSch jsch = null;
		Session session = null;
		Channel channel = null;
		ChannelSftp c = null;
		ChannelExec f = null;
		
		try {
			jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(pass);
			//			jsch.setKnownHosts(khfile);
			//			jsch.addIdentity(identityfile);
 

			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config); 
			session.connect();

 //sftp文件上传
			channel = session.openChannel("sftp");
			channel.connect();
			c = (ChannelSftp) channel;
 
			System.out.println("is Connected !");
 
			System.out.println("Starting File Download:");
			String fsrc = "C:\\src.txt"; 
			String fdest = "/proc/partitions";
			//c.put(fsrc, fdest); 
			//c.put(fdest, fsrc); 
			c.get(fdest, "C:\\src.copy.by.upload.and.download.txt");	//下载		
 
		} catch (Exception e) { 	
			e.printStackTrace();
 
		}
 
		c.disconnect();
		session.disconnect();
	}
 
}
