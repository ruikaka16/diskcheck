package com.wangrui.client.window;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.wangrui.client.CollectSysConfig;

public class AboutInfo extends JDialog{

	private JScrollPane aboutInfowin;
	private JTextArea aboutContent;
	
	public AboutInfo(){
		
		initCompoments();
	}

	private void initCompoments() {
		// TODO Auto-generated method stub
		aboutContent = new JTextArea(14, 48);
		aboutContent.setMargin(new Insets(5, 5, 5, 5));
		aboutContent.setEditable(false);
		
		
		aboutContent.append(getAboutContent());
		aboutInfowin = new JScrollPane(aboutContent);
		aboutInfowin.doLayout();
		//aboutInfowin.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JScrollBar jscrollBar = aboutInfowin.getVerticalScrollBar();
	    if (jscrollBar != null)
	        jscrollBar.setValue(jscrollBar.getMinimum());
		
		System.out.println(jscrollBar.getMaximum());
		
		add(aboutInfowin);
		
		ImageIcon logo21=new ImageIcon(CollectSysConfig.filePathresult+"/image/information.png");   //这里定义一个Icon图片
		setIconImage(logo21.getImage());  //这里设置Icon图片到JMenu
		setTitle("程序版本信息");
		setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setSize(700, 400);
	    setLocationRelativeTo(this);
		setResizable(false);
	}

	@SuppressWarnings("finally")
	private String getAboutContent() {
		// TODO Auto-generated method stub
		BufferedReader reader = null; // 定义BufferedReader
		String content = ""; // content保存文件内容，
		try {
			
			File changelog = new File(CollectSysConfig.filePathresult + "//changelog.log");
			InputStreamReader in = new InputStreamReader(new FileInputStream(changelog),"GBK");// 编码装换

			reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {

				i++;
//				if (i > 2) {
				content += line + "\r\n"; // 从第二行开始取数据
//				}
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return content;
		}
	}
}
