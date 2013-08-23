package com.wangrui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

public class ssh {

	/**
	 * @param args
	 */
	public static JSch jsch = null;
	public static Session session = null;
	public static Channel channel = null;
	public static ChannelSftp c = null;
	public static ChannelExec execChannel = null;

	public static void ssh(String username, String pwd, String host, int port) {

		// String khfile = "/root/.ssh/known_hosts";
		// String identityfile = "/root/.ssh/id_rsa_1024";

		try {
			jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(pwd);
			// jsch.setKnownHosts(khfile);
			// jsch.addIdentity(identityfile);

			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			// sftp文件上传
			channel = session.openChannel("sftp");
			channel.connect();
			c = (ChannelSftp) channel;

			System.out.println("远程主机已连接!");

			System.out.println("开始上传文件:");
			String fsrc = "E:\\工作文档\\1.2.0账户测试系统升级文件-0.142\\20120911_账户系统2_私募债相关\\升级备份\\appcom\\libauth.so";
			String fdest = "/home/hundsun/Backup/20131014";
			// c.put(fsrc, fdest);
			// File file = new File(fsrc);
			// long fileSize = file.length();
			
			c.put(fsrc, fdest, new MyProgressMonitor(), ChannelSftp.OVERWRITE);

			// c.put(fsrc, fdest, new FileProgressMonitor(fileSize),
			// ChannelSftp.OVERWRITE);

		} catch (Exception e) {
			e.printStackTrace();

		}

		c.disconnect();
		session.disconnect();

	}

	public static void main(String[] args) {

		ssh("hundsun", "hundsun", "168.100.101.51", 22);
		
		//execcmd("hundsun","hundsun","168.100.101.51",22,"ll");
		
	}

	public static String execcmd(String username, String pwd, String host, int port,String cmd) {
		try {
			jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(pwd);
			// jsch.setKnownHosts(khfile);
			// jsch.addIdentity(identityfile);

			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			channel = session.openChannel("exec");
			execChannel = (ChannelExec) channel;
			execChannel.setCommand(cmd);
			execChannel.connect();
			BufferedReader fromServer;
			fromServer = new java.io.BufferedReader(new InputStreamReader(
					(execChannel.getInputStream())));
			StringBuffer sb = new StringBuffer();
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// while (fromServer.ready()) {
			// String tt = fromServer.readLine();
			// sb.append(tt + '\n');
			// }
			String tt = null;
			while ((tt = fromServer.readLine()) != null) {
				sb.append(tt + '\n');
			}

			fromServer.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			execChannel.disconnect();
		}
	}

}
