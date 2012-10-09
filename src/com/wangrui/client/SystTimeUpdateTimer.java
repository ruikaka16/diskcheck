package com.wangrui.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.DateFormat;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SystTimeUpdateTimer implements ActionListener {
	public final int ONE_SECOND = 1000; // 1秒钟更新一次
	private JLabel timeLabel = null; // 在此label上显示时间由外部传入
	private Calendar calendar = null; // 获取当前时间的日历类
	private DateFormat dateFormat = null; // 时间格式类 用来格式化时间用
	private Timer timeTimer = null; // 计时器
	private TimeZone currentTimeZone; // *当前的时区 只所以

	// 用它是因为返回的时间可能跟时区有关而跟你机器上的相差几小时我就遇到过*/

	public SystTimeUpdateTimer(JLabel jLabel) {
		// TODO Auto-generated constructor stub
		this.timeLabel = jLabel;
		this.currentTimeZone = TimeZone.getDefault();// 此方法返回本地时区 程序运行的地方
		this.dateFormat = DateFormat.getDateTimeInstance(2, 2,
				java.util.Locale.getDefault());

		// this.dateFormat= //上面的是简写下面的是带汉字的如 2007年4月 3点4分5秒 自己试试
		// DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,java.util.Locale.getDefault());//此为完整的时间格式
		this.timeTimer = new Timer(this.ONE_SECOND, this);
		this.timeTimer.setRepeats(true);
		this.timeTimer.start();
	}

	public void stopTimer() {
		this.timeTimer.stop();
	}

	public void reStartTimer() {
		this.timeTimer.restart();
	}

	// 下面就是计时器每隔一定时间做的事
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.calendar = Calendar.getInstance(this.currentTimeZone);
		this.timeLabel.setText(""
				+ this.dateFormat.format(this.calendar.getTime()));

		// 下面的也可以 更精确些 不过把计时器的时间间隔弄小些不然刷新跟不上豪秒就不显示了
		// java.sql.Timestamp timeStamp=new
		// java.sql.Timestamp(System.currentTimeMillis());
		// this.timeLabel.setText(timeStamp.toString());
	}
//	public static void main(String[] args){
//		
//		JFrame j = new JFrame("test");
//		JPanel p = new JPanel();
//		p.setSize(490, 310);
//		JLabel a = new JLabel("time");
//		SystTimeUpdateTimer s = new SystTimeUpdateTimer(a);
//		p.add(a);
//		j.add(p);
//		j.setVisible(true);
//
//	}
}
