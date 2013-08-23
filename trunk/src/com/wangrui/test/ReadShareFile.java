package com.wangrui.test;

import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFile;

public class ReadShareFile {

	 public static void main(String[] args) {
	     
	  try{
	   SmbFile smbFile=new SmbFile("smb://administrator:xbzq2005@168.100.1.212/qs/深证通金融平台西部证券有关参数20110914.doc");
	    //通过 smbFile.isDirectory();isFile()可以判断smbFile是文件还是文件夹
	     int length = smbFile.getContentLength();//得到文件的大小
	      byte buffer[] = new byte[length];
	      SmbFileInputStream in = new SmbFileInputStream(smbFile); //建立smb文件输入流
	      while ((in.read(buffer)) != -1) {
	        System.out.write(buffer);
	        System.out.println(buffer.length);
	      }
	      in.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	}