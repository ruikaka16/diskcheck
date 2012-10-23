package com.wangrui.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class test {

	public static void main(String[] args) {

		String sql = "insert into table_name values('168.100.8.48','ab','abc','')";
		String uri = "jdbc:mysql://168.100.8.47:3306/test";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;


		try {
			Class.forName("com.jdbc.mysql.Driver");
			conn = DriverManager.getConnection(uri, "root", "wangrui");
			stmt = conn.createStatement();
			stmt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "重复数据");
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
