package com.wangrui.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wangrui.client.CollectSysConfig;

public class test6 {
	public static void main(String[] args) {
		
		File bakfile = new File("C://");
		File[] bakFiles = bakfile.listFiles();
		String content = ""; // content保存文件内容，　　　　
		BufferedReader reader = null; // 定义BufferedReader
		
		for (int j = 0; j < bakFiles.length; j++) {
			if (bakFiles[j].getName().endsWith(".txt")) {
			
				try {
					InputStreamReader in = new InputStreamReader(new FileInputStream(
							bakFiles[j]), "unicode");// 编码装换

					reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
					String line;
					int i = 0;
					while ((line = reader.readLine()) != null) {

						i++;
//						if (i > 1) {
//							content += line + "\r\n";
//						}
						 System.out.println("line:"+line);
						 System.out.println("i=:"+i);
						
						
					}
				}

				catch (IOException e) {
					e.printStackTrace();
				} finally { // finally中将reader对象关闭　　　　　

					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}

		System.out.println("显示打印内容：\r\n"+content);
		int size1 = content.indexOf(",");
//		
		try{
		  FileWriter fw = new FileWriter("c:\\result.txt", true);
		  BufferedWriter bw = new BufferedWriter(fw);
		  bw.write(content);
		  bw.newLine();
		  bw.flush(); //将数据更新至文件
		  bw.close();
		  fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
