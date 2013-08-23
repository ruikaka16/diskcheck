package com.wangrui.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author wangrui
 * 20130221
 */
public class ConnectUtil {
	static{
		try{
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		String url = "jdbc:mysql://168.100.8.47:3306/test";
		String username = "root";
		String password = "wangrui";
		Connection con = null;
		try{
			con = DriverManager.getConnection(url,username,password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	public static void close(ResultSet rs, Statement ps, Connection conn){
		try{
			if(rs!=null){
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			if(ps!=null){
				ps.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			if(conn!=null){
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	 }
	public static void main(String[] args) {
		System.out.println(getConnection());
	}
}