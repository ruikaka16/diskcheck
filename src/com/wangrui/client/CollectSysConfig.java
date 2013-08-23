package com.wangrui.client;

import java.sql.ResultSet;

import com.wangrui.server.DBConnection;

public class CollectSysConfig {

	public static DBConnection conn_getFilePath, conn_getUtil,conn_getDbUrl,conn_getHqpath,conn_getExcelPath,conn_updatePath;
	public static String filePathresult, utilresult,dburl,hqpath,excelpath,updatePath;
	public static ResultSet rs_getFilePath, rs_getUtil,rs_getDbUrl,rs_getHqpath,rs_getExcelPath,rs_updatePath;

	public CollectSysConfig() {

		String sql_getFilePath = "select value from systemconfig where id = 1000";
		String sql_getUtil = "select value from systemconfig where id = 1001";
		String sql_dburl = "select value from systemconfig where id = 1002";
		String sql_hqpath = "select value from systemconfig where id = 1003";
		String sql_excelpath = "select value from systemconfig where id = 1004";
		String sql_updatepath = "select value from systemconfig where id = 1005";

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
		
		conn_getDbUrl = new DBConnection();
		rs_getDbUrl = conn_getDbUrl.executeQuery(sql_dburl);
		try {
			while (rs_getDbUrl.next()) {
				dburl = rs_getDbUrl.getString("value");
			}
			conn_getDbUrl.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		conn_getHqpath = new DBConnection();
		rs_getHqpath = conn_getHqpath.executeQuery(sql_hqpath);
		try {
			while (rs_getHqpath.next()) {
				hqpath = rs_getHqpath.getString("value");
			}
			conn_getHqpath.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		conn_getExcelPath = new DBConnection();
		rs_getExcelPath = conn_getExcelPath.executeQuery(sql_excelpath);
		try{
			while(rs_getExcelPath.next()){
				excelpath = rs_getExcelPath.getString("value");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		conn_getExcelPath.close();
		
		conn_updatePath = new DBConnection();
		rs_updatePath = conn_updatePath.executeQuery(sql_updatepath);
		try{
			while(rs_updatePath.next()){
				updatePath = rs_updatePath.getString("value");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		conn_updatePath.close();
	}

	public static String getUpdatePath() {
		return updatePath;
	}

	public static void setUpdatePath(String updatePath) {
		CollectSysConfig.updatePath = updatePath;
	}

	public static String getExcelpath() {
		return excelpath;
	}

	public static void setExcelpath(String excelpath) {
		CollectSysConfig.excelpath = excelpath;
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

	public static String getDburl() {
		return dburl;
	}

	public static String getHqpath() {
		return hqpath;
	}

	public static void setHqpath(String hqpath) {
		CollectSysConfig.hqpath = hqpath;
	}

}