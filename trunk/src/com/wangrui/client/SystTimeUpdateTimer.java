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
	public final int ONE_SECOND = 1000; // 1���Ӹ���һ��
	private JLabel timeLabel = null; // �ڴ�label����ʾʱ�����ⲿ����
	private Calendar calendar = null; // ��ȡ��ǰʱ���������
	private DateFormat dateFormat = null; // ʱ���ʽ�� ������ʽ��ʱ����
	private Timer timeTimer = null; // ��ʱ��
	private TimeZone currentTimeZone; // *��ǰ��ʱ�� ֻ����

	// ��������Ϊ���ص�ʱ����ܸ�ʱ���йض��������ϵ���Сʱ�Ҿ�������*/

	public SystTimeUpdateTimer(JLabel jLabel) {
		// TODO Auto-generated constructor stub
		this.timeLabel = jLabel;
		this.currentTimeZone = TimeZone.getDefault();// �˷������ر���ʱ�� �������еĵط�
		this.dateFormat = DateFormat.getDateTimeInstance(2, 2,
				java.util.Locale.getDefault());

		// this.dateFormat= //������Ǽ�д������Ǵ��ֵ��� 2007��4�� 3��4��5�� �Լ�����
		// DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,java.util.Locale.getDefault());//��Ϊ�����ʱ���ʽ
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

	// ������Ǽ�ʱ��ÿ��һ��ʱ��������
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.calendar = Calendar.getInstance(this.currentTimeZone);
		this.timeLabel.setText(""
				+ this.dateFormat.format(this.calendar.getTime()));

		// �����Ҳ���� ��ȷЩ ����Ѽ�ʱ����ʱ����ŪСЩ��Ȼˢ�¸��Ϻ���Ͳ���ʾ��
		// java.sql.Timestamp timeStamp=new
		// java.sql.Timestamp(System.currentTimeMillis());
		// this.timeLabel.setText(timeStamp.toString());
	}
	public static void main(String[] args){
		
		JFrame j = new JFrame("test");
		JPanel p = new JPanel();
		p.setSize(490, 310);
		JLabel a = new JLabel("time");
		SystTimeUpdateTimer s = new SystTimeUpdateTimer(a);
		p.add(a);
		j.add(p);
		j.setVisible(true);

	}
}
