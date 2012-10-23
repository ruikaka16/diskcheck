package com.wangrui.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Color;
import java.util.Random;

//定义第一个线程
class T2 extends Thread {

	private ThreadDemo t;

	T2(ThreadDemo td) {
		t = td;
	}

	public void run() {
		try {
			while (true) {
				t.i++;
				String timer = getAppointedFormatDate("yyyy-MM-dd a HH:mm:ss");
				t.time.setText(timer);
				Thread.sleep(1000);
				System.out.println("i=" + t.i);

				if (t.i == 10) {
					t.flag = true;
					t.bar.setVisible(false);
					break;

				}
			}
		} catch (InterruptedException ie) {
			// ie.printStackTrace();
		}
	}

	// 获得指定格式的日期时间字符串
	public String getAppointedFormatDate(String dateFormat) {

		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date d = new Date();
		return df.format(d);
	}
}

public class ThreadDemo implements ActionListener {

	private JFrame frame;
	private JPanel ptop;
	private JButton b1;

	// 因为要在类外部访问以下4个标签，所以要声明为包类型
	JLabel time;
	JLabel color1;
	JLabel color2;
	JLabel color3;
	JProgressBar bar;
	Boolean flag = false;
	int i = 0;
	JPanel pcenter;

	private T2 t1;

	ThreadDemo() {
		setWindow();
		addComponent();
		addEvent();
		frame.setVisible(true);
	}

	private void setWindow() {
		frame = new JFrame("线程示例");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setSize(300, 200);
	}

	private void addComponent() {

		ptop = new JPanel();
		b1 = new JButton("启动第一个线程");

		ptop.add(b1);

		pcenter = new JPanel();
		bar = new JProgressBar(0, 100);
		bar.setIndeterminate(true);
		bar.setString("正在查询中，请稍等！");
		bar.setStringPainted(true);// 设置在进度条中显示百分比
		bar.setVisible(true);

		time = new JLabel("时间", JLabel.CENTER);

		pcenter.setLayout(new FlowLayout());
		pcenter.add(time);

		frame.add(ptop, BorderLayout.NORTH);
		frame.add(pcenter, BorderLayout.CENTER);
	}

	private void addEvent() {
		b1.addActionListener(this);

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ThreadDemo();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("启动第一个线程")) {
			pcenter.add(bar);
			t1 = new T2(this);
			t1.start();
			b1.setText("终止第一个线程");

		}

	}
}