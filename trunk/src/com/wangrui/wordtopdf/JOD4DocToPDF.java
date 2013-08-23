package com.wangrui.wordtopdf;

import java.io.File;
import java.net.ConnectException;
import java.util.Date;

import javax.swing.JOptionPane;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;


public class JOD4DocToPDF {
	
	public void docToPdf(File inputFile, File outputFile){
		Date start = new Date();
		 // connect to an OpenOffice.org instance running on port 8100
	    OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
	    try{
	    	connection.connect();
	    	
	    	 // convert
		    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		    converter.convert(inputFile, outputFile);
	    }catch(ConnectException cex){
	    	cex.printStackTrace();
	    }finally{
		    // close the connection
	    	if(connection!=null){
	    		connection.disconnect();
	    		connection = null;
	    	}
	    }
	    long l = (start.getTime()- new Date().getTime());
	    long day=l/(24*60*60*1000);
	       long hour=(l/(60*60*1000)-day*24);
	       long min=((l/(60*1000))-day*24*60-hour*60);
	       long s=(l/1000-day*24*60*60-hour*60*60-min*60);
	       System.out.println("生成"+outputFile.getName()+"耗费："+min+"分"+s+"秒");
	}
	
	class TestThread extends java.lang.Thread{
		public File inputFile;
		public File outputFile;
		
		public void run(){
			JOD4DocToPDF t = new JOD4DocToPDF();
			t.docToPdf(inputFile, outputFile);
			System.out.println(outputFile.getName()+"文件已生成");
			JOptionPane.showMessageDialog(null, outputFile.getName()+"文件已生成");
		}
	}
	
	public void test(String path){
		TestThread t1 = new TestThread();
		t1.inputFile = new File(path);
//		t1.inputFile = new File(path+st+".xls");
		t1.outputFile = new File(path.substring(0, path.length()-4)+".pdf");
		
//		
//		TestThread t2 = new TestThread();
//		t2.inputFile = new File("d:/document2.doc");
//		t2.outputFile = new File("d:/document2.pdf");
//		
//		TestThread t3 = new TestThread();
//		t3.inputFile = new File("d:/document3.doc");
//		t3.outputFile = new File("d:/document3.pdf");
		
		
//		t2.start();
		t1.start();
//		t3.start();
	}
}
