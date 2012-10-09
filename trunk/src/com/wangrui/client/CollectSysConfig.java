package com.wangrui.client;

import java.sql.ResultSet;

import com.wangrui.server.DBConnection;

public class CollectSysConfig {

	public static DBConnection conn_getFilePath, conn_getUtil;
	public static String filePathresult, utilresult;
	public static ResultSet rs_getFilePath, rs_getUtil;

	public CollectSysConfig() {

		String sql_getFilePath = "select value from systemconfig where id = 1000";
		String sql_getUtil = "select value from systemconfig where id = 1001";

		conn_getFilePath = new DBConnection();
		rs_getFilePath = conn_getFilePath.executeQuery(sql_getFilePath);
		try {
			while (rs_getFilePath.next()) {
				filePathresult = rs_getFilePath.getString("value");
			}
			conn_getFilePath.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		conn_getUtil = new DBConnection();
		rs_getUtil = conn_getUtil.executeQuery(sql_getUtil);
		try {
			while (rs_getUtil.next()) {
				utilresult = rs_getUtil.getString("value");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		conn_getUtil.close();
	}

	public static String getFilePathresult() {
		return filePathresult;
	}

	public static void setFilePathresult(String filePathresult) {
		CollectSysConfig.filePathresult = filePathresult;
	}

	public static String getUtilresult() {
		return utilresult;
	}

	public static void setUtilresult(String utilresult) {
		CollectSysConfig.utilresult = utilresult;
	}
}