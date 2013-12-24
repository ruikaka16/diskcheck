package com.wangrui.test;

import java.net.*;
import java.io.*;
public class TCPClient {
	public static void main(String []args) throws Exception{
		Socket s = new Socket("168.100.8.47",6666);
		OutputStream os = s.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF("HELLO SERVER !");
		System.out.println("I am a client !");
		dos.flush();
		dos.close();
		s.close();
	}
}
