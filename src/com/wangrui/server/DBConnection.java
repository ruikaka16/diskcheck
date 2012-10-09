package com.wangrui.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * @param args
 */

public class DBConnection {
	private static String dbUrl = null;
	private String theUser = null;// 用户�?
	private String thePw = null;// 密码
	private Connection c = null;//
	private java.sql.Statement conn;
	private ResultSet rs = null;
	private String driver = null;
	private String dbSid = null;
	private Statement stmt;

	public DBConnection() {
		try {
			// ORACLE的JDBC驱动�?
			Properties pro = new Properties();
			try {
				pro.load(new FileInputStream(
						"C:/Program Files/mysql/mysql.properties"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,"请联系系统开发人员，报错信息为："+e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// System.out.println(pro.getProperty("jdbc.driver"));
			dbUrl = pro.getProperty("jdbc.url");
			theUser = pro.getProperty("jdbc.user");
			thePw = pro.getProperty("jdbc.password");
			driver = pro.getProperty("jdbc.driver");
			dbSid = pro.getProperty("jdbc.sid");

			setdbName(theUser);
			setdbPsw(thePw);
			setdbSid(dbSid);

			Class.forName(driver).newInstance();
			try {
				c = DriverManager.getConnection(dbUrl, theUser, thePw);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn = c.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setdbName(String theUser) {
		this.theUser = theUser;
	}

	public String getdbName() {
		return theUser;
	}

	public void setdbPsw(String thePw) {
		this.thePw = thePw;
	}

	public String getdbPsw() {
		return thePw;
	}

	public void setdbSid(String dbSid) {
		this.dbSid = dbSid;
	}

	public String getdbSid() {
		return dbSid;
	}

	public ResultSet executeQuery(String sql) {
		rs = null;
		try {
			rs = ((java.sql.Statement) conn).executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;

	}


	public boolean executeUpdate(String sql) {

		try {

			stmt=c.createStatement();
			stmt.executeUpdate(sql);
			return true;

		}

		catch (SQLException e) {

			e.printStackTrace();

			return false;
		}


	}

	public void close() {
		try {
			// conn.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// newjdbc.testPorpertiesFile();
		ResultSet newrs;
		// ConnectOracle newjdbc = new ConnectOracle();
		DBConnection newjdbc = new DBConnection();

		System.out.println(newjdbc.getdbName());
		System.out.println(newjdbc.getdbPsw());
		System.out.println(newjdbc.getdbSid());
		System.out.println("connect sucess");

		newjdbc.close();
	}

}
