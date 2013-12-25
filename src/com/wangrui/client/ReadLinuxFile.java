package com.wangrui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
import com.wangrui.client.window.SoFileCompare;
import com.wangrui.server.DBConnection;

public class ReadLinuxFile {

	private ResultSet rs_queryDeviceInfo;
	
	public ReadLinuxFile(int system_type) {
		
		try {
		
					DBConnection conn_query = new DBConnection();
					String sql1 = "select * from test.update_device where system_type=0";
					String sql2 = "select * from test.update_device where system_type=1";
					if(system_type==0){
						rs_queryDeviceInfo = conn_query.executeQuery(sql1);
					}else if(system_type==1){
						rs_queryDeviceInfo = conn_query.executeQuery(sql2);
					}

					while (rs_queryDeviceInfo.next()) {		
						WriteSoInfo(rs_queryDeviceInfo.getString("ip"), rs_queryDeviceInfo.getString("username"), rs_queryDeviceInfo.getString("password"),Integer.parseInt(rs_queryDeviceInfo.getString("system_type")));
					}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void WriteSoInfo(String ip, String username, String password,int system_type)
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
			File file = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_so.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();
			
			insertSoInfoDatabase(ip,system_type);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * 将queryresult_liunx文件数据插入数据库
	 */
	public void insertSoInfoDatabase(String ip,int system_type) {

		File txtfile = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_so.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			DBConnection conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into update_sofilelist (ip,file_name,file_size,file_time,calc_time,system_type) values('"+ip+"','"+s[8]+"','"+s[4]+"','"+s[7]+"-"+s[5]+"-"+s[6]+"',date_format(now(),'%Y%m%d'),"+system_type+")";
					conn_insertLiunxCommdToDatabase.executeUpdate(sql);

				}
			}

			conn_insertLiunxCommdToDatabase.close();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}
	}
	
	public boolean isSoFileIn(){
		try{
			int i=0;
			String sql = "select count(*) from test.update_sofilelist where calc_time = date_format(now(),'%Y%m%d')";
			System.out.println(sql);
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			while(rs_vaildInsert.next()){
				//System.out.println("i="+i);
				if(rs_vaildInsert.getString("count(*)").equals("0")){
					//JOptionPane.showMessageDialog(null, "该操作员已被占用");
					return false;
				}else{
					return true;
				}				
			}	return false;
		}catch(Exception e){
			
			e.printStackTrace();
			return true;
		}
	}
}
