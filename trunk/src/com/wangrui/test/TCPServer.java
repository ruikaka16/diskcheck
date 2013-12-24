package com.wangrui.test;

import java.net.*;
import java.io.*;
public class TCPServer {
	public static void main(String []args) throws Exception{
		ServerSocket ss = new ServerSocket(6666);
		int count = 0;
		while (true){
			Socket s = ss.accept();
			count ++;
			DataInputStream dis = new DataInputStream(s.getInputStream());
			System.out.println("第" + count + "个客户:" + dis.readUTF() + s.getInetAddress() + "port" + s.getPort());
			dis.close();
			s.close();
		}
	}
}
