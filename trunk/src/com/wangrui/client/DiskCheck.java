package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wangrui.client.ChkResult;
import com.wangrui.server.DBConnection;

public class DiskCheck extends JFrame {

	/**
	 * @param args
	 */
	public String filePath = "C:";
	public static JTextArea jTextArea = new JTextArea(9, 42);
	private JMenuBar menuBar;
	private JFrame frame = null;
	public DBConnection conn_query, conn_insertLiunxCommdToDatabase,conn_insertCommdToDatabase, conn_querySysConfig,
			conn_getFilePath, conn_getUtil;
	JPanel contentPanel;
	public static JLabel jLabel2;
	public ResultSet rs_queryDeviceInfo, rs_querySysConfigInfo, rs_getFilePath,
			rs_getUtil;
	// private String utilresult,filePathresult;
	private int l = 1000, m = 1002, n = 1003;
	private SystemConfigBean sysConfigBean;
	public static LoginMain loginMain;
	public RandomAccessFile dos_vbs=null;

	public DiskCheck() {

		// 界面部分
		jTextArea.setMargin(new Insets(5, 5, 5, 5));
		jTextArea.setEditable(false);
		JScrollPane jScrollPanel = new JScrollPane(jTextArea);
		JLabel jLabel1 = new JLabel();
		// JLabel jLabel2 = new JLabel();
		JLabel jLabel3 = new JLabel();
		JLabel mainLabel = new JLabel("欢迎使用磁盘空间查询系统");
		
		JLabel systimeLable = new JLabel();
		systimeLable.setLocation(450, 250);
		SystTimeUpdateTimer s = new SystTimeUpdateTimer(systimeLable);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		// jLabel1.setText("系统初始化日期：");
		JButton button = new JButton("开始查询！");
		mainLabel.setFont(new Font("华文楷体", Font.ITALIC, 24));
		Image icon = (new ImageIcon("D:/Program Files/DiskCheck/disk.gif")).getImage();
		setIconImage(icon);

		/*
		 * 从systemconfig表中去参数配置
		 */
		final CollectSysConfig collectSysConfig = new CollectSysConfig();
		System.out.println(CollectSysConfig.filePathresult);
		
		// 先删除已存在的.vbs文件
		File delbatFile = new File(CollectSysConfig.filePathresult + "//"); // 生成bat的目录
		final File[] delbatFiles = delbatFile.listFiles();
		for (int j = 0; j < delbatFiles.length; j++) {
			if (delbatFiles[j].getName().endsWith(".vbs")
					|| delbatFiles[j].getName().endsWith(".bak")
					|| delbatFiles[j].getName().endsWith(".txt")) {

				delbatFiles[j].delete();
			}
		}
		

		//执行vbs的查询命令
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				jTextArea.append("【" + getSystime() + "】︰"
						+ "开始写入查询命令，请等待!" + "\n");
				jTextArea.paintImmediately(jTextArea.getBounds());
				
				//查参数配置表并写入查询命令
				queryDeviceInfoAll();
		
				System.out.println("执行vbs命令阶段!");
				File batFile = new File(CollectSysConfig.filePathresult+"\\"); // vbs的目录
				String[] cmd   = new String[]{"wscript",CollectSysConfig.filePathresult+"\\vbsCommd.vbs"};
				
				System.out.println("vbs目录："+CollectSysConfig.filePathresult+"\\vbsCommd.vbs");
				final File[] batFiles = batFile.listFiles();
				if (batFiles != null) {
			
							Runtime rn = Runtime.getRuntime();
							Process p = null;

							try {	
								p = rn.exec(cmd);
								//System.out.println("vbs命令所在目录："+batFiles[i].getPath());
								// rn.exec("WMIC /node:168.100.8.48 /user:admin /password:xb123 /output:C:\\a.bak logicaldisk where drivetype=3 get DeviceID,Size,FreeSpace /format:csv");
								int exitValue = p.waitFor();
								System.out.println("返回值：" + exitValue);
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						
					
				} else {
					JOptionPane.showMessageDialog(null, "文件不存在!");
				}

				//文件写入后等待一下  以防止执行空文件
				try {
					Thread.sleep(5000);
					System.out.println("等待一下！");
				} catch (InterruptedException e2) {
					
					e2.printStackTrace();
				}

				changeTxtType();
				
				try {
					Thread.sleep(5000);
					System.out.println("等待一下！");
				} catch (InterruptedException e2) {
					
					e2.printStackTrace();
				}
				
				//执行Windows查询结果插入数据库操作
				insertCommdToDatabase();
				
				try {
					Thread.sleep(5000);
					System.out.println("等待一下！");
				} catch (InterruptedException e2) {
					
					e2.printStackTrace();
				}
				

			}
		});

		this.setJMenuBar(this.getMenu());//
		jPanel.add(mainLabel);
		// jPanel.add(jLabel1);
		// jPanel.add(jLabel2);

		jPanel.add(jScrollPanel);
		setContentPane(jPanel);
		jPanel.add(button);
		jPanel.add(systimeLable);
		Container contentpane = getContentPane();

	}	
/*	
 *将Windows设备查询命令写入到vbs文件中 
*/
	public void WriteVbsCommd(String ip, String username, String password) {		
		// 写入新的vbs文件
		try {
		 dos_vbs = new RandomAccessFile(
					CollectSysConfig.filePathresult + "\\" + "vbsCommd" + ".vbs",
					"rw");
			dos_vbs.seek(dos_vbs.length());

			String impSQL = "Set objWsh = CreateObject(\"WScript.Shell\")"
					+ "\r\n"
					+ "objWsh.Run \"WMIC /node:"
					+ ip
					+ " /user:"
					+ username
					+ " /password:"
					+ password
					+ " /output:"
					+ CollectSysConfig.filePathresult
					+ "\\"
					+ ip
					+ ".bak logicaldisk where drivetype=3 get DeviceID,Size,FreeSpace /format:csv\",vbhide\r\nWScript.Sleep 3000\r\n";
			System.out.println("打印vbs命令:\r\n" + impSQL);

			//dos_vbs.write(impSQL.getBytes());
			dos_vbs.writeBytes(new String(impSQL.getBytes(),"iso8859-1")+"\r\n");
			
			//dos_vbs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 finally { // finally中将reader对象关闭　　　　　
		
						if (dos_vbs != null) {
							try {
								dos_vbs.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

	}
/*
 *将Linux设备的查询命令写入文件
*/
	public void WriteLinuxInfo(String ip,String username,String password)throws JSchException{
		
		 JSch jsch=new JSch();
		   Session session=jsch.getSession(username, ip,22);
		   session.setPassword(password);
		   Properties pop=new Properties();
		   pop.setProperty("StrictHostKeyChecking","no");
		   session.setConfig(pop);
		   session.connect();
		   Channel channel=session.openChannel("exec");
		   ((ChannelExec)channel).setCommand("df -k | sed -e '1d;/ /!N;s/\\( \\)\\{1,\\}/\\1/g;'");
		   try {
		    InputStream in=channel.getInputStream();
		   
		    File file=new File(CollectSysConfig.filePathresult+"\\queryresult_linux.txt");
		    FileOutputStream fos=new FileOutputStream(file);
		    channel.connect();
		    FileCopyUtils.copy(in, fos);// 将结果存放至文件
		    
		    channel.disconnect();   
		    session.disconnect();
		   
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  
		
	}
/*
 *  取查询设备表device
 */

	public List queryDeviceInfoAll() {

		// 生成执行命令并写入vbs中
		try {
			File bakfile = new File(CollectSysConfig.filePathresult+"//vbsCommdtest.vbs");
			conn_query = new DBConnection();
			String sql = "select * from test.device order by ip desc";
			rs_queryDeviceInfo = conn_query.executeQuery(sql);

			List deviceInfoList = new ArrayList();
			DeviceBean devInfoBean = new DeviceBean();

			while (rs_queryDeviceInfo.next()) {

				devInfoBean.setIp(rs_queryDeviceInfo.getString("ip"));
				devInfoBean.setUsername(rs_queryDeviceInfo
						.getString("username"));
				devInfoBean.setPassword(rs_queryDeviceInfo
						.getString("password"));
				devInfoBean.setOs(rs_queryDeviceInfo.getString("os"));
				deviceInfoList.add(devInfoBean);
				System.out.println("得到的设备信息：" + devInfoBean.getIp());
				
				if(rs_queryDeviceInfo.getString("os").equals("Windows"))//如果该设备为Windows则写入导入命令
				{
					WriteVbsCommd(rs_queryDeviceInfo.getString("ip"),
						rs_queryDeviceInfo.getString("username"),
						rs_queryDeviceInfo.getString("password"));
				}
				else if(rs_queryDeviceInfo.getString("os").equals("Linux"))//如果该设备为Linux则直接查询并返回文件后写入数据库
				{
					System.out.println("Linux系统的查询操作");
					try{
					WriteLinuxInfo(rs_queryDeviceInfo.getString("ip"),
						rs_queryDeviceInfo.getString("username"),
						rs_queryDeviceInfo.getString("password"));
					
					//Linux查询后直接插入数据库
					insertLinuxCommdToDatabase(rs_queryDeviceInfo.getString("ip"));
					
					}catch(Exception e){
						e.printStackTrace();
					}
				}

			}
			
			jTextArea.append("【" + getSystime() + "】︰"
					+ "查询命令写入完成!" + "\n");
			jTextArea.paintImmediately(jTextArea.getBounds());
			
			jTextArea.append("【" + getSystime() + "】︰"
					+ "开始查询磁盘信息，请等待!" + "\n");
			jTextArea.paintImmediately(jTextArea.getBounds());
			conn_query.close();
			return deviceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
//取系统配置表systemconfig
	public List querySystemConfigAll() {


		try {

			conn_querySysConfig = new DBConnection();
			String sql = "select * from test.systemconfig";
			rs_querySysConfigInfo = conn_querySysConfig.executeQuery(sql);

			List sysconfigList = new ArrayList();
			// SystemConfigBean sysconfigBean = new SystemConfigBean();

			while (rs_querySysConfigInfo.next()) {

				sysConfigBean = new SystemConfigBean();
				sysConfigBean.setId(rs_querySysConfigInfo.getString("id"));
				sysConfigBean
						.setValue(rs_querySysConfigInfo.getString("value"));
				sysConfigBean.setRemark(rs_querySysConfigInfo
						.getString("remark"));
				sysconfigList.add(sysConfigBean);

			}
			conn_querySysConfig.close();
			return sysconfigList;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}
/*
 * 将queryresult_liunx文件数据插入数据库
 */
	
	public void insertLinuxCommdToDatabase(String ip){
		
		File txtfile = new File(CollectSysConfig.filePathresult+"//queryresult_linux.txt");
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if(!str.trim().isEmpty()){
				String s[] = str.split(" ");
				String sql = "insert into deviceDisk1 (date,ip,deviceId,freespace,size,util,type) values(date_format(now(),'%Y%m%d'),'"+ip+"','"
					+ s[5]
					+ "',"
					+ "'"
					+ s[3]
					+ "'/1024/1024,('"
					+ Integer.parseInt(s[1])
					+ "')/1024/1024,100-round('"
					+ s[3]
					+ "'*100/('"
					+ Integer.parseInt(s[1])
					+ "'),2),case when 100-round('"
					+ s[3]
					+ "'*100/('"
					+ Integer.parseInt(s[1])
					+ "'),0)>= 40 then '1'else '0' end)";

				System.out.println(s[0]);
				conn_insertLiunxCommdToDatabase.executeUpdate(sql);
				
				}
			}
			
		
			conn_insertLiunxCommdToDatabase.close();	
			
//			JOptionPane.showMessageDialog(null, "查询完毕!");
//			jTextArea.append("【" + getSystime() + "】︰"
//					+ip+":Linux磁盘信息查询完成!" + "\n");
//			jTextArea.paintImmediately(jTextArea.getBounds());
//查询完成直接显示结果	
			//未分页
//			ChkResult t = new ChkResult();
//			t.setVisible(true);
//			t.setLocationRelativeTo(null);
			
			//分页表格
//			ChkResultTable t = new  ChkResultTable();
//			t.setLocationRelativeTo(null);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
}
	}
//将转换后的txt文件插入数据库中
	public void insertCommdToDatabase() {

		File txtfile = new File(CollectSysConfig.filePathresult+"//queryresult_windows.txt");

				try {
					BufferedReader bf = new BufferedReader(new FileReader(txtfile));
					String str;
					conn_insertCommdToDatabase = new DBConnection();
					while (null != (str = bf.readLine())) {
						if(!str.trim().isEmpty()){
						String s[] = str.split(",");
						String sql = "insert into deviceDisk1 (date,ip,deviceId,freespace,size,util,type) values(date_format(now(),'%Y%m%d'),'"
								+ s[0]
								+ "','"
								+ s[1]
								+ "','"
								+ s[2]
								+ "'/1024/1024/1024,'"
								+ s[3]
								+ "'/1024/1024/1024,100-round('"
								+ s[2]
								+ "'*100/'"
								+ s[3]
								+ "',2),case when 100-round('"
								+ s[2]
								+ "'*100/'" + s[3] + "',0)>= '"+CollectSysConfig.utilresult+"' then '1'else '0' end)";

						System.out.println(s[0]);
						conn_insertCommdToDatabase.executeUpdate(sql);
						
						}
					}
					
				
					conn_insertCommdToDatabase.close();	
					
					JOptionPane.showMessageDialog(null, "查询完毕!");
					jTextArea.append("【" + getSystime() + "】︰"
							+"磁盘信息查询完成!" + "\n");
					jTextArea.paintImmediately(jTextArea.getBounds());
//查询完成直接显示结果	
					//未分页
//					ChkResult t = new ChkResult();
//					t.setVisible(true);
//					t.setLocationRelativeTo(null);
					
					//分页表格
					ChkResultTable t = new  ChkResultTable();
					t.setLocationRelativeTo(null);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}

	}
//将查询结果的bak文件转换为txt，因为查询结果的格式存在问题，所以必须进行转换
	public void changeTxtType() {
		File bakfile = new File(CollectSysConfig.filePathresult+"//");
		System.out.println(CollectSysConfig.filePathresult+"//");
		File[] bakFiles = bakfile.listFiles();
		String content = ""; // content保存文件内容，　　　　
		BufferedReader reader = null; // 定义BufferedReader
		
		for (int j = 0; j < bakFiles.length; j++) {
			if (bakFiles[j].getName().endsWith(".bak")) {
			
				try {
					InputStreamReader in = new InputStreamReader(new FileInputStream(
							bakFiles[j]), "unicode");// 编码装换

					reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
					String line;
					int i = 0;
					while ((line = reader.readLine()) != null) {

						i++;
						if (i > 2) {
							content += line + "\r\n";
						}					
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
				}
			}
//			else{
//				
//				JOptionPane.showMessageDialog(null, "通讯线路有问题，请检查！");
//				System.exit(1);
//			}
			
		}

		System.out.println("显示打印内容：\r\n"+content);
		int size1 = content.indexOf(",");
//		
		try{
		  FileWriter fw = new FileWriter(CollectSysConfig.filePathresult+"//queryresult_windows.txt", true);
		  BufferedWriter bw = new BufferedWriter(fw);
		  bw.write(content);
		  bw.newLine();
		  bw.flush(); //将数据更新至文件
		  bw.close();
		  fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//程序工具栏菜单设计
	private JMenuBar getMenu() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			JMenu m1 = new JMenu();
			m1.setText("文件");

			JMenu m6 = new JMenu();
			m6.setText("帮助");

			JMenu m2 = new JMenu();
			m2.setText("工具");

			JMenu m4 = new JMenu();
			m4.setText("配置");

			JMenuItem item11 = new JMenuItem();
			item11.setText("退出");

			JMenuItem item12 = new JMenuItem();
			item12.setText("磁盘信息查询");

			JMenuItem item31 = new JMenuItem();
			item31.setText("配置查询设备");

			JMenuItem item41 = new JMenuItem();
			item41.setText("配置系统参数");

			JMenuItem item21 = new JMenuItem();
			item21.setText("版本信息");
			item21.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(null, "磁盘查询程序V2.1，支持Windows、Linux系统！");
				}
			});

			item11.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});

			item12.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					//未分页表格
					ChkResult t = new ChkResult();
					t.setVisible(true);
					t.setLocationRelativeTo(null);
					
					//分页表格
//					ChkResultTable t = new  ChkResultTable();
//					t.setLocationRelativeTo(null);
				}
			});

			item31.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					DeviceConfig deviceConfig = new DeviceConfig();
					deviceConfig.setVisible(true);
					deviceConfig.setLocationRelativeTo(null);
				}
			});

			item41.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SystemConfig systemConfig = new SystemConfig();
					systemConfig.setVisible(true);
					systemConfig.setLocationRelativeTo(null);

				}
			});
			m2.add(item12);
			m1.addSeparator();
			m1.add(item11);
			m6.add(item21);
			m4.add(item41);
			m4.addSeparator();
			m4.add(item31);

			menuBar.add(m1);
			menuBar.add(m2);
			menuBar.add(m4);
			menuBar.add(m6);
		}
		return menuBar;
	}
// 获取系统时间一遍显示~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static String getSystime() {

			Date date = new Date();
			DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
					DateFormat.MEDIUM, DateFormat.MEDIUM);

			return mediumDateFormat.format(date);
		}
//程序入口
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//loginMain = new LoginMain();
//		DiskCheck diskCheck = new DiskCheck();
//		// win.setIconImage(icon);
//		diskCheck.setResizable(false);
//		diskCheck.setTitle("磁盘空间查询");
//		diskCheck.setAlwaysOnTop(false);
//		diskCheck.setSize(490, 310);
//		diskCheck.setLocationRelativeTo(null); // 设置窗口居中显示
//		diskCheck.show();
//	}


}
