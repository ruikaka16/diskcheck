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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.window.SoFileCompare;
import com.wangrui.server.DBConnection;

public class ReadLinuxFile {

	private ResultSet rs_queryDeviceInfo,rs_queryDeviceIp;
	
	public static void main(String args[]) throws JSchException{
		WriteVersionInfo("168.100.102.42","hundsun","hundsun",0);
	}
	
	public ReadLinuxFile(int system_type) {
		
		try {
		
					DBConnection conn_query = new DBConnection();
					DBConnection conn_query_version = new DBConnection();
					String sql1 = "select * from test.update_device where system_type=0";
					String sql2 = "select * from test.update_device where system_type=1";
					String sql3 = "select * from update_device  where system_type = 0 order by ip desc limit 1;";
					String sql4 = "select * from update_device  where system_type = 1 order by ip desc limit 1;";
					if(system_type==0){
						rs_queryDeviceInfo = conn_query.executeQuery(sql1);
						rs_queryDeviceIp = conn_query_version.executeQuery(sql3);
					}else if(system_type==1){
						rs_queryDeviceInfo = conn_query.executeQuery(sql2);
						rs_queryDeviceIp = conn_query_version.executeQuery(sql4);
					}
					System.out.println("开始执行导入SO文件信息......");
					while (rs_queryDeviceInfo.next()) {		
						System.out.println("正在导入"+rs_queryDeviceInfo.getString("ip"));
						WriteSoInfo(rs_queryDeviceInfo.getString("ip"), rs_queryDeviceInfo.getString("username"), rs_queryDeviceInfo.getString("password"),Integer.parseInt(rs_queryDeviceInfo.getString("system_type")));					
					}
					System.out.println("导入SO文件信息完成！");
					System.out.println("开始执行导入SO文件版本信息......");
					while(rs_queryDeviceIp.next()){
						System.out.println("正在导入"+rs_queryDeviceIp.getString("ip"));
						WriteVersionInfo(rs_queryDeviceIp.getString("ip"), rs_queryDeviceIp.getString("username"), rs_queryDeviceIp.getString("password"),Integer.parseInt(rs_queryDeviceIp.getString("system_type")));
					}
					System.out.println("导入SO文件版本信息完成！");
					rs_queryDeviceInfo.close();
					rs_queryDeviceIp.close();
					conn_query.close();
					conn_query_version.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void WriteSoInfo(String ip, String username, String password,int system_type)
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
		((ChannelExec) channel).setCommand("ls -l cd appcom |sed 's/ \\+/ /g'|sed '1,2d'" );
		//((ChannelExec) channel).setCommand("cd appcom;strings -f *.so|grep V8" );
		try {
			InputStream in = channel.getInputStream();
			// 查询结果返回到queryresult_linux.txt文件中
			File file = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_so.txt");
			//File file = new File("C:\\"+ip+"_so.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();
			
			insertSoInfoDatabase(ip,system_type);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,ip+"的so文件信息不存在！");
		}

	}
	
	/*
	 * 将queryresult_liunx文件数据插入数据库
	 */
	public static void insertSoInfoDatabase(String ip,int system_type) {

		File txtfile = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_so.txt");

		CallableStatement  clstm= null;
		
		Connection conn = null;
		//使用存储过程插入
		try {
			DBConnection c = new DBConnection();
			conn = c.getConnection();
			String sql = "{call insert_SoInfo(?,?,?,?,?)}";
			clstm = conn.prepareCall(sql);
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					clstm.setString(1, ip);
					clstm.setString(2, s[8]);
					clstm.setString(3, s[4]);
					clstm.setString(4,s[7]+"-"+s[5]+"-"+s[6]);
					clstm.setInt(5, system_type);
					clstm.execute();
				}
		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
		      try {  		         
		          if(clstm!=null){  
		        	  clstm.close();  
		          }  
		          if(conn!=null){  
		            conn.close();  
		          }   
		      }catch (SQLException ex1) {  
		      }  
		} 
	
	}
	public static void WriteVersionInfo(String ip, String username, String password,int system_type) throws JSchException{
		
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, ip, 22);
		session.setPassword(password);
		Properties pop = new Properties();
		pop.setProperty("StrictHostKeyChecking", "no"); 
		session.setConfig(pop);
		session.connect();
		Channel channel = session.openChannel("exec");
		//((ChannelExec) channel).setCommand("ls -l cd appcom |sed 's/ \\+/ /g'|sed '1,2d'" );
		((ChannelExec) channel).setCommand("cd appcom;strings -f *.so|fgrep V8." );
		try {
			InputStream in = channel.getInputStream();
			// 查询结果返回到queryresult_linux.txt文件中
			//File file = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_so.txt");
		    File file = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_version.txt");
			//File file = new File("C:\\"+ip+"_version.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();
			
			insertSoVersionInfoDatabase(ip,system_type);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,ip+"的so文件信息不存在！");
		}
	}
	public static void insertSoVersionInfoDatabase(String ip,int system_type) {
		
		File txtfile = new File(CollectSysConfig.filePathresult+ "\\log\\"+SoFileCompare.getSystime()+"\\"+ip+"_version.txt");
		//File txtfile = new File("C:\\"+ip+"_version.txt");

		CallableStatement  clstm= null;
		
		Connection conn = null;
		//使用存储过程插入
		try {
			DBConnection c = new DBConnection();
			conn = c.getConnection();
			String sql = "{call insert_SoVersionInfo(?,?,?)}";
			clstm = conn.prepareCall(sql);
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(": ");
					clstm.setString(1, s[0]);
					clstm.setString(2, s[1]);
					clstm.setInt(3, system_type);
					clstm.execute();
				}
		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
		      try {  		         
		          if(clstm!=null){  
		        	  clstm.close();  
		          }  
		          if(conn!=null){  
		            conn.close();  
		          }   
		      }catch (SQLException ex1) {  
		      }  
		} 
	}
}
