package com.wangrui.dbf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import com.wangrui.dbf.DBFException;
import com.wangrui.dbf.DBFReader;
import com.wangrui.server.DBConnection;

public class ReadHQ {
	
	public static Timer timer;
	
	
	
	public ReadHQ(){
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					System.out.println("开始执行读DBF文件！");
					
					long start = System.currentTimeMillis();
					readDBF("D:/hq/szhq/sjshq.dbf", "C:/szhq.txt");
					
					long end = System.currentTimeMillis();
					//

					System.out.println("耗时="+(end-start));
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("DBF文件没找到！");
					e1.printStackTrace();
				} catch (DBFException e1) {
					// TODO Auto-generated catch block
					System.out.println("读取DBF文件失败！");
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JDBFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//executeCommd("C:/szhq.txt");
			}

		}, 0, 3000);

	}

	public static void main(String args[]) throws IOException, JDBFException {

		InputStream fis = new FileInputStream("D:/hq/szhq/sjshq.dbf");
		// 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息
		DBFReader dbfreader = new DBFReader(fis);
		dbfreader.setCharactersetName("GB2312"); // 设置编码方式
		//File txtFile = new File(txtpath);
		//FileWriter fw = new FileWriter(txtFile);
		//BufferedWriter bw = new BufferedWriter(fw);

		int i;

		 for (i=0; i<dbfreader.getFieldCount(); i++) {
		// System.out.print(dbfreader.getField(i).getName()+"  ");
		 }

		
		Object[] aobj;
		//aobj = dbfreader.nextRecord();
		
		// System.out.println(aobj.length);
		 //for(i = 0; i<dbfreader.getRecordCount(); i++){

		while ((aobj = dbfreader.nextRecord())!= null) {
			
			String inputStr = new String();
			for (int j = 0; j < 2; j++) {
				if (aobj[j].equals("")) {
					inputStr += "";
					
				} else {
					System.out.println(aobj[j]);
					inputStr += aobj[j] + "|";
				}
			}
			inputStr += "\n";
			//fw.write(inputStr);
			//bw.write(inputStr);
		}
		//fw.close();
		
//		final Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//			public void run() {
//				try {
//					System.out.println("开始执行读DBF文件！");
//					readDBF("X:/sjshq.dbf", "C:/szhq.txt");
//					//executeCommd("C:/szhq.txt");
//					
//				} catch (FileNotFoundException e1) {
//					// TODO Auto-generated catch block
//					System.out.println("DBF文件没找到！");
//					e1.printStackTrace();
//				} catch (DBFException e1) {
//					// TODO Auto-generated catch block
//					System.out.println("读取DBF文件失败！");
//					e1.printStackTrace();
//				}
//
//				//executeCommd("C:/szhq.txt");
//			}
//
//		}, 0, 10000);

	}

	public static void readDBF(String dbfpath, String txtpath)throws IOException, JDBFException {

		InputStream fis = new FileInputStream(dbfpath);
		// 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息
		DBFReader dbfreader = new DBFReader(fis);
		//dbfreader.setCharactersetName("GB2312"); // 设置编码方式
		File txtFile = new File(txtpath);
		FileWriter fw = new FileWriter(txtFile);
		BufferedWriter bw = new BufferedWriter(fw);

		int i;

		// for (i=0; i<dbfreader.getFieldCount(); i++) {
		// System.out.print(dbfreader.getField(i).getName()+"  ");
		// }

		Object[] aobj;
		aobj = dbfreader.nextRecord();
		while ((aobj = dbfreader.nextRecord()) != null) {
			
			String inputStr = new String();
			for (int j = 0; j < aobj.length; j++) {
				if (aobj[j].equals("")) {
					inputStr += "";
					//System.out.println(aobj[j - 1].toString());
				} else {
					inputStr += aobj[j] + "|";
				}
			}
			inputStr += "\n";
			fw.write(inputStr);
			//bw.write(inputStr);
		}
		fw.close();
		
		executeCommd("c:/szhq.txt");
	}
	
	public static void executeCommd(String txtpath){
		
		String SQLStr = "LOAD DATA INFILE '"+txtpath+"' REPLACE INTO TABLE szhq FIELDS TERMINATED BY '|';";
		//System.out.println(SQLStr);

		Statement stmt = null;
		//CallableStatement cstmt = null;
		DBConnection conn = new DBConnection();

		try {
			//cstmt = conn.prepareCall("{call update_szhq()}"); // 调用update_szhq()存储过程
			//cstmt.execute();
			stmt = conn.execute(SQLStr);
			//stmt.execute(SQLStr);
			System.out.println("导入完成！");
			
			} finally {
			try {

				//cstmt.close();
				stmt.close();
				conn.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}

