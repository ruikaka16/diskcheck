package com.wangrui.test;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import com.wangrui.client.ChkResult;

import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgressBar extends JFrame{
	
	public void ProgressBar(){
		
	       int height;
	        int width;
	        addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e)
	                { System.exit(0); } } );
	        
	        setLocation(200,150);
	        Container pane = getContentPane();
	        pane.setLayout(null);
	        Insets insets = pane.getInsets();

	        JProgressBar bar = new JProgressBar(0,100);
	        bar.setIndeterminate(true);
	        bar.setValue(0);//设置进度条的值
	        bar.setStringPainted(true);//设置在进度条中显示百分比

	        Dimension dim = bar.getPreferredSize();
	        int x = insets.left + 20;
	        int y = insets.top + 20;
	        dim.width += 100;
	        bar.setBounds(x,y,dim.width,dim.height);
	        pane.add(bar);

	        width = x + dim.width + 20 + insets.left;
	        height = y + dim.height + 40 + insets.bottom;

	        ProgINDemoTask task = new ProgINDemoTask(bar);
	        task.start();//进度条开始运行

	        setSize(width,height);
	        
	        setVisible(true);
	}
	 public static void main(String[] args) {
//       // TODO Auto-generated method stub
       
     new ProgressBar();
   }

}
class ProgINDemoTask implements Runnable {//实现Runnable，创建一个线程
    private boolean running;
    private Thread looper;
    private JProgressBar bar;
    private int value;
    ProgINDemoTask(JProgressBar bar) {
        this.bar = bar;
        running = false;
    }
    public void start() {//线程启动方法
        if(!running) {
            value = 0;
            bar.setValue(value);
            running = true;
            looper = new Thread(this);
            looper.start();
        }
    }
    public void run() {//线程运行中的动作
        try {
            while(running) {
                Thread.sleep(200);
               // bar.setValue(++value);
                bar.setString("正在查询中，请稍等！");
                if(value >= 100)//到100停下
                    running = false;
            }
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
