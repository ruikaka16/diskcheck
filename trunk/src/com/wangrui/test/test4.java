package com.wangrui.test;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class test4 
{
	public static void main(String[] args) throws FileNotFoundException, IOException {

		

		      String fileName = "c:\\queryresult.txt";// 要读取的txt文件
		        String sId;
		        List<String[]> list = getFileContent(fileName);// 将所有读到的文件放到数组里面

		        String[] ary = list.get(1);// 

		        for (String str : ary) {
		            //System.out.println(str);
		            
		            //System.out.println(str.split(","));
		            
		            ary=str.split(",");
		            sId=ary[1];
		            System.out.println(ary[1]);
		        }

	

		}

		private static List<String[]> getFileContent(String fileName)
		            throws FileNotFoundException, IOException {
		        File file = new File(fileName);
		        BufferedReader bf = new BufferedReader(new FileReader(file));

		        String content = "";

		        List<String[]> contentList = new ArrayList<String[]>();
		        while (content != null) {
		            content = bf.readLine();
		           // System.out.println(content);

		            if (content == null) {
		                break;
		            }

		            if (!content.trim().equals("")) {
		                contentList.add(content.trim().split("\\s+"));
		            }

		        }

		        bf.close();

		        return contentList;
		    }
		}