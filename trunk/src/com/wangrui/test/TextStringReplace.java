package com.wangrui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class TextStringReplace {
	
	public static void main(String args[]) throws IOException{
		
		String s=null;
		File fileseg=new File("你的目录");
		File  fileres=new File("你的输出目录");
		BufferedReader readerseg;
		try {
			readerseg = new BufferedReader(new FileReader(fileseg));
			PrintStream ps=new PrintStream(new FileOutputStream(fileres));
			while((s=readerseg.readLine())!=null){
			s=s.replaceAll("@","");
			ps.print(s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
