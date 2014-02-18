package com.wangrui.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.swing.JOptionPane;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.DataValue;
import com.wangrui.client.DTO.UpdateLogDateValue;

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
	public static String app_path=null;
	private Statement stmt;
	private PreparedStatement pm;

	public DBConnection() {
		try {
			// ORACLE的JDBC驱动�?
			Properties pro = new Properties();
			try {
				pro.load(new FileInputStream(
						"C:/Program Files/mysql/mysql.properties"));
						//CollectSysConfig.filePathresult+"/properties/mysql.properties"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "请联系系统开发人员，报错信息为：" + e1);
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
			//app_path=pro.getProperty("config.path");
				
			setdbName(theUser);
			setdbPsw(thePw);
			setdbSid(dbSid);
			//setApp_path(app_path);

			Class.forName(driver).newInstance();
			try {
				c = DriverManager.getConnection(dbUrl, theUser, thePw);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn = c.createStatement();
				stmt=c.createStatement();
				
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
	
	public Connection getConnection() throws Exception {  
        return DriverManager.getConnection(dbUrl, theUser, thePw);  
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

	public String getApp_path() {
		return app_path;
	}

	public void setApp_path(String app_path) {
		this.app_path = app_path;
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

			stmt = c.createStatement();
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

	public ArrayList executeQuery1(String sql) {
		ResultSet res = null;
		ArrayList list = null;
		try {
			res = stmt.executeQuery(sql);
			list = this.getResult(res);
			System.out.println("res="+res.getString("oc_date"));
			System.out.println("List="+list.size());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return list;
	}

	public ResultSet executeQuery2(String sql) {
		ResultSet res = null;
		ArrayList list = null;
		try {
			res = stmt.executeQuery(sql);
			// list=this.getResult(res);
			// System.out.println(list.size());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return res;
	}
	
	public ArrayList executeQuery3(String sql) {
		ResultSet res = null;
		ArrayList list = null;
		try {
			res = stmt.executeQuery(sql);
			list = this.getUpdateLogResult(res);
			System.out.println(list.size());
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return list;
	}

	public ArrayList getResult(ResultSet rs) {
		ArrayList result = new ArrayList();
		try {
			while (rs.next()) {
				DataValue value = new DataValue();
				value.setDate(rs.getString("date"));
				value.setIp(rs.getString("ip"));
				value.setDeviceid(rs.getString("deviceid"));
				value.setFreespace(rs.getString("freespace"));
				value.setSize(rs.getString("size"));
				value.setUtil(rs.getString("util"));
				value.setType(rs.getString("type"));
				result.add(value);
			}
		} catch (Exception e) {

		}
		return result;
	}
	
	public ArrayList getUpdateLogResult(ResultSet rs) {
		ArrayList result = new ArrayList();
		try {
			while (rs.next()) {
				UpdateLogDateValue value = new UpdateLogDateValue();
				value.setOc_date(rs.getString("oc_date"));
				value.setRemark(rs.getString("remark"));
				value.setUpdate_content(rs.getString("update_content"));
				value.setOperator(rs.getString("operator"));
				result.add(value);
			}
		} catch (Exception e) {

		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// newjdbc.testPorpertiesFile();
		ResultSet newrs;
		// ConnectOracle newjdbc = new ConnectOracle();
		DBConnection newjdbc = new DBConnection();

		try {
			String sql5 = "insert into test.device (ip,username,password,os) values('168.100.8.48','a','a','')";
			newjdbc = new DBConnection();
			newjdbc.executeUpdate(sql5);
       
		}
		finally {
			newjdbc.close(); 
			} 
	}

	public Statement execute(String sql) {
		// TODO Auto-generated method stub
		Statement stmt;
		try {

			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			return stmt;

		}

		catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}


}
