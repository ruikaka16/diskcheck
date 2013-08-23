package com.wangrui.test;

import java.awt.Container;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.security.auth.login.LoginContext;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MyClient {
	InputStream in;
	OutputStream ou;
	JFrame j31;
	TextArea t1;
	JButton b1;
	Socket s1;

	My m = new My();

	MyClient() {

		// 构造界面
		j31 = new JFrame("客户机");
		j31.setBounds(300, 300, 300, 400);
		j31.setVisible(true);
		j31.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j31.setLayout(null);

		Container B = j31.getContentPane();
		B.setBounds(0, 0, 300, 400);

		t1 = new TextArea(5, 8);
		t1.setBounds(0, 0, 300, 200);
		B.add(t1);

		b1 = new JButton("发送");
		b1.setBounds(50, 250, 80, 30);
		B.add(b1);

		// 网络接收

		try {
			s1 = new Socket(InetAddress.getByName("168.100.102.44"), 9502);
			in = s1.getInputStream();
			ou = s1.getOutputStream();
			byte b[] = new byte[800];
			int l = in.read(b);
			System.out.println("客户端接收到：" + new String(b, 0, l));

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b1.addActionListener(m);

	}

	class My implements ActionListener // 事件监听类（内部类实现）实现事件监听接口
	{

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == b1) // 如果按下发送
			{
				try {

					ou.write(t1.getText().getBytes());

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}

	public static void main(String[] args) throws Exception {
		new MyClient();
		 Socket s = new Socket("168.100.102.44",9502);  
	        DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());  
	        dataOutputStream.writeUTF("haha.test");  
	        dataOutputStream.flush();  
	        dataOutputStream.close();  
	        s.close();  

	}
}