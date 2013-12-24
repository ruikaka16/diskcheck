package com.wangrui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wangrui.client.CollectSysConfig;
import com.wangrui.server.DBConnection;

public class ReadLinuxFile extends JFrame{

	public ReadLinuxFile() {

		initComponents();
	}
	private void initComponents() {
		// TODO Auto-generated method stub
		JButton jButton2 = new javax.swing.JButton("读取");
		jButton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					WriteLinuxInfo("168.100.102.22", "hundsun", "hundsun");
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		setSize(200, 200);
		add(jButton2);
	}
	public void WriteLinuxInfo(String ip, String username, String password)
			throws JSchException {
		// 通过Jsch对Linux设备进行通信，并执行Linux命令获取设备信息
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, ip, 22);
		session.setPassword(password);
		Properties pop = new Properties();
		pop.setProperty("StrictHostKeyChecking", "no"); 
		session.setConfig(pop);
		session.connect();
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand("ls -l cd appcom |sed 's/ \\+/ /g'|sed '1,2d'" );// 查询磁盘信息的Linux命令
		try {
			InputStream in = channel.getInputStream();
			// 查询结果返回到queryresult_linux.txt文件中
			File file = new File("C:\\22.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();
			
			insertLinuxCommdToDatabase("168.100.102.22");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * 将queryresult_liunx文件数据插入数据库
	 */
	public void insertLinuxCommdToDatabase(String ip) {

		File txtfile = new File("c://22.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			DBConnection conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into update_sofilelist (ip,file_name,file_size,file_time,calc_time,system_type) values('"+ip+"','"+s[8]+"','"+s[4]+"','"+s[7]+"-"+s[5]+"-"+s[6]+"',2013-12-23,0)";
					conn_insertLiunxCommdToDatabase.executeUpdate(sql);

				}
			}

			conn_insertLiunxCommdToDatabase.close();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}
	}
	public static void main(String arg[]){
		
		ReadLinuxFile a = new ReadLinuxFile();
		a.setVisible(true);
	}
}
