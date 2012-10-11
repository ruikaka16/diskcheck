package com.wangrui.test;

import java.io.*;
import java.sql.*;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.server.DBConnection;

public class test5 {

	public DBConnection conn_insertCommmd;

	public static void main(String args[]) throws Exception {
		DBConnection conn_insertCommmd;
		File f = new File("C:\\log.txt");
		BufferedReader bf = new BufferedReader(new FileReader(f));
		String str;
		String[] files = new String[5];
		try {

			conn_insertCommmd = new DBConnection();

			while ((str = bf.readLine()) != null) {

				if (!str.trim().isEmpty()) {
					// files = str.split(" ");
					String[] s = str.split(" ");

					String sql = "insert into deviceDisk1 (date,ip,deviceId,freespace,size,util,type) values(date_format(now(),'%Y%m%d'),'','"
							+ s[0]
							+ "',"
							+ "'"
							+ s[3]
							+ "'/1024/1024,('"
							+ Integer.parseInt(s[1])
							+ "')/1024/1024,100-round('"
							+ s[3]
							+ "'*100/('"
							+ Integer.parseInt(s[1])
							+ "'),2),case when 100-round('"
							+ s[3]
							+ "'*100/('"
							+ Integer.parseInt(s[1])
							+ "'),0)>= 40 then '1'else '0' end)";
					// String sql =
					// "insert into deviceDisk1 (freespace) values('"+(Integer.parseInt(s[1]))/1024/1024+"')";

					conn_insertCommmd.executeUpdate(sql);
					System.out.println(s[2]);
				}
				// �ַ����ת��
				// int i=st.executeUpdate(sql);
				// System.out.println(i+"����¼ִ�гɹ�");
			}
			conn_insertCommmd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
