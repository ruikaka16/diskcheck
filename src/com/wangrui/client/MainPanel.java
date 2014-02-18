package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wangrui.client.window.AboutInfo;
import com.wangrui.client.window.AddUser;
import com.wangrui.client.window.ModifedPswd;
import com.wangrui.client.window.SoFileCompare;
import com.wangrui.client.window.SoVersionSearch;
import com.wangrui.client.window.UpdateLog;
import com.wangrui.server.DBConnection;
import com.wangrui.test.TreeDemo;

/*
 * 定义查询的各项操作的线程
 */
class T1 extends Thread {

	private MainPanel t;

	T1(MainPanel td) {
		t = td;
	}

	public void run() {

		t.jTextArea.append("【" + getSystime() + "】︰" + "开始写入查询命令，请等待!" + "\n");
		t.jTextArea.paintImmediately(t.jTextArea.getBounds());

		// 查参数配置表并写入查询命令
		if(LoginMain.userType.getText().equals("0")){
			queryDeviceInfoAll(0);
		}else if(LoginMain.userType.getText().equals("1")){
			queryDeviceInfoAll(1);
		}

		// 执行vbs命令阶段
		System.out.println("执行vbs命令阶段!");
		File batFile = new File(LoginMain.app_path + "/log/"+SoFileCompare.getSystime()+""); // vbs的目录
		String[] cmd = new String[] { "wscript",
				LoginMain.app_path + "/log/"+SoFileCompare.getSystime()+"/vbsCommd.vbs" };

		System.out.println("vbs目录：" + LoginMain.app_path + "/log/"+SoFileCompare.getSystime()+"/vbsCommd.vbs");
		final File[] batFiles = batFile.listFiles();
		if (batFiles != null) {

			Runtime rn = Runtime.getRuntime();
			Process p = null;

			try {
				p = rn.exec(cmd);
				int exitValue = p.waitFor();
				System.out.println("返回值：" + exitValue);// 执行后的返回值，0为正常结束，1为出现异常
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(null, "文件不存在!");
			t.jdiaLog.setVisible(false);// 出现异常后要去除进度条
		}

		// 文件写入后等待一下 以防止执行空文件
		try {
			Thread.sleep(5000);
			System.out.println("等待一下！");
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		// windows设备结果需要进行字符的处理
		changeTxtType();

		// 字符的处理后等待一下，然后插入数据库
		try {
			Thread.sleep(2000);
			System.out.println("等待一下！");
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		// 执行Windows查询结果插入数据库操作
		if(LoginMain.userType.getText().equals("0")){
			insertCommdToDatabase(0);
		}else if(LoginMain.userType.getText().equals("1")){
			insertCommdToDatabase(1);
		}

	}

	/*
	 * 将windows设备查询后数据插入数据库
	 */
	public void insertCommdToDatabase(int system_type) {

		File txtfile = new File(LoginMain.app_path+ "//log//"+SoFileCompare.getSystime()+"//queryresult_windows.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(",");
					String sql = "insert into deviceDisk (date,ip,deviceId,freespace,size,util,type,system_type) values(date_format(now(),'%Y%m%d'),'"
							+ s[0]
							+ "','"
							+ s[1]
							+ "','"
							+ s[2]
							+ "'/1024/1024/1024,'"
							+ s[3]
							+ "'/1024/1024/1024,100-round('"+ s[2]+ "'*100/'"+ s[3]+ "',2),case when 100-round('"+ s[2]+ "'*100/'"+ s[3]+ "',0)>= '"+ LoginMain.disk_util+ "' then '1'else '0' end,"+system_type+")";

					System.out.println(s[0]);
					t.conn_insertCommdToDatabase.executeUpdate(sql);

				}
			}

			t.conn_insertCommdToDatabase.close();
			t.jdiaLog.setVisible(false); // 插入结束后，去除进度条，并提示查询完毕！
			JOptionPane.showMessageDialog(null, "查询完毕!");

			t.jTextArea.append("【" + getSystime() + "】︰" + "磁盘信息查询完成!" + "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());

			// 查询完成直接显示结果
			// 未分页
			// ChkResult t = new ChkResult();
			// t.setVisible(true);
			// t.setLocationRelativeTo(null);

			// 分页表格
			ChkResultTable t = new ChkResultTable();
			t.setLocationRelativeTo(null);

		} catch (Exception e) {
			e.printStackTrace();
			t.bar.setVisible(false);
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}

	}

	/*
	 * 将windows查询结果的bak文件转换为txt文件，因为查询结果的格式存在问题，所以必须进行转换
	 */
	public void changeTxtType() {
		File bakfile = new File(LoginMain.app_path + "//log//"+SoFileCompare.getSystime()+"");
		//System.out.println(LoginMain.app_path + "//");
		File[] bakFiles = bakfile.listFiles();
		String content = ""; // content保存文件内容，　　　　
		BufferedReader reader = null; // 定义BufferedReader
		// 将bak文件的前两行去掉：bak文件第一行为空，第二行为字段名，只取其中的数据
		for (int j = 0; j < bakFiles.length; j++) {
			if (bakFiles[j].getName().endsWith(".bak")) {
				try {
					InputStreamReader in = new InputStreamReader(
							new FileInputStream(bakFiles[j]), "unicode");// 编码装换

					reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
					String line;
					int i = 0;
					while ((line = reader.readLine()) != null) {

						i++;
						if (i > 2) {
							content += line + "\r\n"; // 从第二行开始取数据
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
		}

		System.out.println("显示打印内容：\r\n" + content);

		// 将转换后的数据写入新的txt文件中
		try {
			FileWriter fw = new FileWriter(LoginMain.app_path
					+ "//log//"+SoFileCompare.getSystime()+"//queryresult_windows.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.flush(); // 将数据更新至文件
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取系统时间,在日志栏显示
	 */
	public static String getSystime() {

		Date date = new Date();
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM);

		return mediumDateFormat.format(date);
	}

	/*
	 * 获取要查询设备的信息，从device表中取，并存入bean中
	 */
	public List queryDeviceInfoAll(int system_type) {

		// 生成执行命令并写入vbs中
		try {
			t.conn_query = new DBConnection();
			String sql = "select * from test.device where system_type = "+system_type+" order by ip desc";
			System.out.println(sql);
			t.rs_queryDeviceInfo = t.conn_query.executeQuery(sql);

			List deviceInfoList = new ArrayList();// 定义一个List
			DeviceBean devInfoBean = new DeviceBean();// 实例化一个DeviceBean

			while (t.rs_queryDeviceInfo.next()) {

				// 将rs结果集存入javaBean中
				devInfoBean.setIp(t.rs_queryDeviceInfo.getString("ip"));
				devInfoBean.setUsername(t.rs_queryDeviceInfo
						.getString("username"));
				devInfoBean.setPassword(t.rs_queryDeviceInfo
						.getString("password"));
				devInfoBean.setOs(t.rs_queryDeviceInfo.getString("os"));
				deviceInfoList.add(devInfoBean);// 将bean装入List中
				System.out.println("得到的设备信息：" + devInfoBean.getIp());

				if (t.rs_queryDeviceInfo.getString("os").equals("Windows"))// 如果该设备为Windows则通过WriteVbsCommd过程写入导入命令
				{
					WriteVbsCommd(t.rs_queryDeviceInfo.getString("ip"),
							t.rs_queryDeviceInfo.getString("username"),
							t.rs_queryDeviceInfo.getString("password"));
				} else if (t.rs_queryDeviceInfo.getString("os").equals("Linux"))// 如果该设备为Linux则直接查询并返回文件后通过insertLinuxCommdToDatabase过程写入数据库
				{
					System.out.println("Linux系统的查询操作");
					try {
						WriteLinuxInfo(t.rs_queryDeviceInfo.getString("ip"),
								t.rs_queryDeviceInfo.getString("username"),
								t.rs_queryDeviceInfo.getString("password"));

						insertLinuxCommdToDatabase(t.rs_queryDeviceInfo.getString("ip"),system_type);

					} catch (Exception e) {
						e.printStackTrace();
						t.bar.setVisible(false);
					}
				}

			}

			t.jTextArea.append("【" + getSystime() + "】︰" + "查询命令写入完成!" + "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());

			t.jTextArea.append("【" + getSystime() + "】︰" + "开始查询磁盘信息，请等待!"
					+ "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());
			t.conn_query.close();
			return deviceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/*
	 * 将queryresult_liunx文件数据插入数据库
	 */
	public void insertLinuxCommdToDatabase(String ip,int system_type) {

		File txtfile = new File(LoginMain.app_path
				+ "//log//"+SoFileCompare.getSystime()+"//queryresult_linux.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into deviceDisk (date,ip,deviceId,freespace,size,util,type,system_type) values(date_format(now(),'%Y%m%d'),'"
							+ ip
							+ "','"
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
							+ "'),0)>= "+LoginMain.disk_util+" then '1'else '0' end,"+system_type+")";

					System.out.println("s5="+s[5]);
					t.conn_insertLiunxCommdToDatabase.executeUpdate(sql);

				}
			}

			t.conn_insertLiunxCommdToDatabase.close();

		} catch (Exception e) {
			e.printStackTrace();
			t.bar.setVisible(false);
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}
	}

	/*
	 * 将Linux设备的查询命令写入文件
	 */
	public void WriteLinuxInfo(String ip, String username, String password)
			throws JSchException {
		// 通过Jsch对Linux设备进行通信，并执行Linux命令获取设备信息
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, ip, 22);
		session.setPassword(password);
		Properties pop = new Properties();
		pop.setProperty("StrictHostKeyChecking", "no");
		session.setConfig(pop);
		session.connect();
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel)
				.setCommand("df -k | sed -e '1d;/ /!N;s/\\( \\)\\{1,\\}/\\1/g;'");// 查询磁盘信息的Linux命令
		try {
			InputStream in = channel.getInputStream();
			// 查询结果返回到queryresult_linux.txt文件中
			File file = new File(LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/queryresult_linux.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 将Windows设备查询命令写入到vbs文件中
	 */
	
	public void WriteVbsCommd(String ip, String username, String password) {
		// 写入新的vbs文件
		try {
			t.dos_vbs = new RandomAccessFile(LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/" + "vbsCommd" + ".vbs", "rw");
			t.dos_vbs.seek(t.dos_vbs.length());

			String impSQL = "Set objWsh = CreateObject(\"WScript.Shell\")"
					+ "\r\n"
					+ "objWsh.Run \"WMIC /node:"
					+ ip
					+ " /user:"
					+ username
					+ " /password:"
					+ password
					+ " /output:"
					+ LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/"
					+ ip
					+ ".bak logicaldisk where drivetype=3 get DeviceID,Size,FreeSpace /format:csv\",vbhide\r\nWScript.Sleep 3000\r\n";
			System.out.println("打印vbs命令:\r\n" + impSQL);
			t.dos_vbs.writeBytes(new String(impSQL.getBytes(), "iso8859-1")
					+ "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (t.dos_vbs != null) {
				try {
					t.dos_vbs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * 
	 * @author wangrui
	 * 将升级导入命令写入批处理文件
	 */
	public void WriteUpdateCommd(String ip, String username, String password){
		
	}

}
	

class OSSearch extends Thread {

	private MainPanel t;

	OSSearch(MainPanel td) {
		t = td;
	}

	public void run() {

		t.jTextArea1.append("【" + getSystime() + "】︰" + "开始写入查询命令，请等待!" + "\n");
		t.jTextArea1.paintImmediately(t.jTextArea1.getBounds());

		// 查参数配置表并写入查询命令
		if(LoginMain.userType.getText().equals("0")){
			queryDeviceInfoAll(0);
		}else if(LoginMain.userType.getText().equals("1")){
			queryDeviceInfoAll(1);
		}

		// 执行vbs命令阶段
		System.out.println("执行vbs命令阶段!");
		File batFile = new File(LoginMain.app_path + "/log/"+SoFileCompare.getSystime()+"/"); // vbs的目录
		String[] cmd = new String[] { "wscript",
				LoginMain.app_path + "/log/"+SoFileCompare.getSystime()+"/vbsCommdOS.vbs" };

		System.out.println("vbs目录：" + LoginMain.app_path
				+ "/log/"+SoFileCompare.getSystime()+"/vbsCommdOS.vbs");
		final File[] batFiles = batFile.listFiles();
		if (batFiles != null) {

			Runtime rn = Runtime.getRuntime();
			Process p = null;

			try {
				p = rn.exec(cmd);
				int exitValue = p.waitFor();
				System.out.println("返回值：" + exitValue);// 执行后的返回值，0为正常结束，1为出现异常
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(null, "文件不存在!");
			t.jdiaLog.setVisible(false);// 出现异常后要去除进度条
		}

		// 文件写入后等待一下 以防止执行空文件
		try {
			Thread.sleep(5000);
			System.out.println("等待一下！");
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		// windows设备结果需要进行字符的处理
		changeTxtType();

		// 字符的处理后等待一下，然后插入数据库
		try {
			Thread.sleep(2000);
			System.out.println("等待一下！");
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		// 执行Windows查询结果插入数据库操作
		if(LoginMain.userType.getText().equals("0")){
			insertCommdToDatabase(0);
		}else if(LoginMain.userType.getText().equals("1")){
			insertCommdToDatabase(1);
		}

	}

	/*
	 * 将windows设备查询后数据插入数据库
	 */
	public void insertCommdToDatabase(int system_type) {

		File txtfile = new File(LoginMain.app_path
				+ "//log//"+SoFileCompare.getSystime()+"//queryresult_windows_os.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(",");
					String sql = "insert into deviceDisk (date,ip,deviceId,freespace,size,util,type,system_type) values(date_format(now(),'%Y%m%d'),'"
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
							+ "'*100/'"
							+ s[3]
							+ "',0)>= '"
							+ LoginMain.disk_util
							+ "' then '1'else '0' end ,"+system_type+")";

					System.out.println(s[0]);
					t.conn_insertCommdToDatabase.executeUpdate(sql);

				}
			}

			t.conn_insertCommdToDatabase.close();
			t.jdiaLog.setVisible(false); // 插入结束后，去除进度条，并提示查询完毕！
			JOptionPane.showMessageDialog(null, "查询完毕!");

			t.jTextArea.append("【" + getSystime() + "】︰" + "磁盘信息查询完成!" + "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());

			// 查询完成直接显示结果
			// 未分页
			// ChkResult t = new ChkResult();
			// t.setVisible(true);
			// t.setLocationRelativeTo(null);

			// 分页表格
			ChkResultTable t = new ChkResultTable();
			t.setLocationRelativeTo(null);

		} catch (Exception e) {
			e.printStackTrace();
			t.bar.setVisible(false);
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}

	}

	/*
	 * 将windows查询结果的bak文件转换为txt文件，因为查询结果的格式存在问题，所以必须进行转换
	 */
	public void changeTxtType() {
		File bakfile = new File(LoginMain.app_path + "//log//"+SoFileCompare.getSystime()+"//");
		File[] bakFiles = bakfile.listFiles();
		String content = ""; // content保存文件内容，　　　　
		BufferedReader reader = null; // 定义BufferedReader
		// 将bak文件的前两行去掉：bak文件第一行为空，第二行为字段名，只取其中的数据
		for (int j = 0; j < bakFiles.length; j++) {
			if (bakFiles[j].getName().endsWith(".bak")) {
				try {
					InputStreamReader in = new InputStreamReader(
							new FileInputStream(bakFiles[j]), "unicode");// 编码装换

					reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
					String line;
					int i = 0;
					while ((line = reader.readLine()) != null) {

						i++;
						if (i > 2) {
							content += line + "\r\n"; // 从第二行开始取数据
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
		}

		System.out.println("显示打印内容：\r\n" + content);

		// 将转换后的数据写入新的txt文件中
		try {
			FileWriter fw = new FileWriter(LoginMain.app_path
					+ "//log//"+SoFileCompare.getSystime()+"//queryresult_windows.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.flush(); // 将数据更新至文件
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取系统时间,在日志栏显示
	 */
	public static String getSystime() {

		Date date = new Date();
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM);

		return mediumDateFormat.format(date);
	}

	/*
	 * 获取要查询设备的信息，从device表中取，并存入bean中
	 */
	public List queryDeviceInfoAll(int system_type) {

		// 生成执行命令并写入vbs中
		try {
			File bakfile = new File(LoginMain.app_path
					+ "//log//"+SoFileCompare.getSystime()+"//vbsCommdtest.vbs");
			t.conn_query = new DBConnection();
			String sql = "select * from test.device where system_type = "+system_type+" order by ip desc";
			System.out.println("sql="+sql);
			t.rs_queryDeviceInfo = t.conn_query.executeQuery(sql);

			List deviceInfoList = new ArrayList();// 定义一个List
			DeviceBean devInfoBean = new DeviceBean();// 实例化一个DeviceBean

			while (t.rs_queryDeviceInfo.next()) {

				// 将rs结果集存入javaBean中
				devInfoBean.setIp(t.rs_queryDeviceInfo.getString("ip"));
				devInfoBean.setUsername(t.rs_queryDeviceInfo
						.getString("username"));
				devInfoBean.setPassword(t.rs_queryDeviceInfo
						.getString("password"));
				devInfoBean.setOs(t.rs_queryDeviceInfo.getString("os"));
				deviceInfoList.add(devInfoBean);// 将bean装入List中
				System.out.println("得到的设备信息：" + devInfoBean.getIp());

				if (t.rs_queryDeviceInfo.getString("os").equals("Windows"))// 如果该设备为Windows则通过WriteVbsCommd过程写入导入命令
				{
					WriteVbsCommd(t.rs_queryDeviceInfo.getString("ip"),
							t.rs_queryDeviceInfo.getString("username"),
							t.rs_queryDeviceInfo.getString("password"));
				} else if (t.rs_queryDeviceInfo.getString("os").equals("Linux"))// 如果该设备为Linux则直接查询并返回文件后通过insertLinuxCommdToDatabase过程写入数据库
				{
					System.out.println("Linux系统的查询操作");
					try {
						WriteLinuxInfo(t.rs_queryDeviceInfo.getString("ip"),
								t.rs_queryDeviceInfo.getString("username"),
								t.rs_queryDeviceInfo.getString("password"));

						insertLinuxCommdToDatabase(t.rs_queryDeviceInfo.getString("ip"),system_type);

					} catch (Exception e) {
						e.printStackTrace();
						t.bar.setVisible(false);
					}
				}

			}

			t.jTextArea.append("【" + getSystime() + "】︰" + "查询命令写入完成!" + "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());

			t.jTextArea.append("【" + getSystime() + "】︰" + "开始查询磁盘信息，请等待!"
					+ "\n");
			t.jTextArea.paintImmediately(t.jTextArea.getBounds());
			t.conn_query.close();
			return deviceInfoList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/*
	 * 将queryresult_liunx文件数据插入数据库
	 */
	public void insertLinuxCommdToDatabase(String ip,int system_type) {

		File txtfile = new File(LoginMain.app_path
				+ "/log/"+SoFileCompare.getSystime()+"/queryresult_linux.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into deviceDisk (date,ip,deviceId,freespace,size,util,type,system_type) values(date_format(now(),'%Y%m%d'),'"
							+ ip
							+ "','"
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
							+ "'),0)>= "+LoginMain.disk_util+" then '1'else '0' end ,"+system_type+")";

					System.out.println(s[0]);
					t.conn_insertLiunxCommdToDatabase.executeUpdate(sql);

				}
			}

			t.conn_insertLiunxCommdToDatabase.close();

		} catch (Exception e) {
			e.printStackTrace();
			t.bar.setVisible(false);
			JOptionPane.showMessageDialog(null, "查询命令写入失败，请联系开发人员！");
		}
	}

	/*
	 * 将Linux设备的查询命令写入文件
	 */
	public void WriteLinuxInfo(String ip, String username, String password)
			throws JSchException {
		// 通过Jsch对Linux设备进行通信，并执行Linux命令获取设备信息
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, ip, 22);
		session.setPassword(password);
		Properties pop = new Properties();
		pop.setProperty("StrictHostKeyChecking", "no");
		session.setConfig(pop);
		session.connect();
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel)
				.setCommand("df -k | sed -e '1d;/ /!N;s/\\( \\)\\{1,\\}/\\1/g;'");// 查询磁盘信息的Linux命令
		try {
			InputStream in = channel.getInputStream();
			// 查询结果返回到queryresult_linux.txt文件中
			File file = new File(LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/queryresult_linux.txt");
			FileOutputStream fos = new FileOutputStream(file);
			channel.connect();
			FileCopyUtils.copy(in, fos);// 利用easydbo.jar将结果存放至文件

			channel.disconnect();
			session.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 将Windows设备查询命令写入到vbs文件中
	 */
	public void WriteVbsCommd(String ip, String username, String password) {
		// 写入新的vbs文件
		try {
			t.dos_vbs = new RandomAccessFile(LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/" + "vbsCommd" + ".vbs", "rw");
			t.dos_vbs.seek(t.dos_vbs.length());

			String impSQL = "Set objWsh = CreateObject(\"WScript.Shell\")"
					+ "\r\n"
					+ "objWsh.Run \"WMIC /node:"
					+ ip
					+ " /user:"
					+ username
					+ " /password:"
					+ password
					+ " /output:"
					+ LoginMain.app_path
					+ "/log/"+SoFileCompare.getSystime()+"/"
					+ ip
					+ ".bak logicaldisk where drivetype=3 get DeviceID,Size,FreeSpace /format:csv\",vbhide\r\nWScript.Sleep 3000\r\n";
			System.out.println("打印vbs命令:\r\n" + impSQL);
			t.dos_vbs.writeBytes(new String(impSQL.getBytes(), "iso8859-1")
					+ "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (t.dos_vbs != null) {
				try {
					t.dos_vbs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}

public class MainPanel implements ActionListener {

	private JFrame frame;
	private JPanel ptop, pwest, pndisk,pmenu;
	private JButton button, button1;
	private JMenuBar menuBar;
	private JTree jTree;
	private JScrollPane jScrollPanel, jScrollPanel1, jScrollPanel2,jScrollTreePanel,
			jScrollPanel3;
	private JLabel mainLabel, systimeLabel, userTile;
	private JPanel pcenter;
	private T1 t1;
	static JTabbedPane tabbedPane;
	private SystTimeUpdateTimer s;
	private LoginMain login;
	public static String str="";

	// 因为要在类外部访问以下标签，所以要声明为包类型
	JProgressBar bar;
	JDialog jdiaLog;
	Boolean flag = false;
	int i = 0;
	JTextArea jTextArea, jTextArea1;
	DBConnection conn_query, conn_insertLiunxCommdToDatabase,
			conn_insertCommdToDatabase, conn_querySysConfig, conn_getFilePath,
			conn_getUtil;
	ResultSet rs_queryDeviceInfo, rs_querySysConfigInfo, rs_getFilePath,
			rs_getUtil;
	RandomAccessFile dos_vbs = null;

	public MainPanel() {
		//System.out.println(LoginMain.app_path);
		System.out.println(LoginMain.disk_util);
		// 先删除已存在的临时文件
		//File delbatFile = new File(LoginMain.app_path + "//"); // 生成bat的目录
		File delbatFile = new File(LoginMain.app_path + "//"); // 生成bat的目录
		final File[] delbatFiles = delbatFile.listFiles();
		for (int j = 0; j < delbatFiles.length; j++) {
			if (delbatFiles[j].getName().endsWith(".vbs")
					|| delbatFiles[j].getName().endsWith(".bak")
					|| delbatFiles[j].getName().endsWith(".txt")
					|| delbatFiles[j].getName().endsWith(".bat")) {

				delbatFiles[j].delete();
			}
		}

		setWindow();// 添加程序窗体
		addComponent();// 在窗体中增加组建
		addEvent();// 增加坚挺
		frame.setVisible(true);// 显示
	}

	/*
	 * 增加监听事件
	 */
	private void addEvent() {
		// TODO Auto-generated method stub
		button.addActionListener(this);

	}

	/*
	 * 构造菜单栏工具条
	 */
	private JMenuBar getMenu() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			final JMenu m1 = new JMenu();
			m1.setText("文件");

			final JMenu m6 = new JMenu();
			m6.setText("帮助");

			final JMenu m2 = new JMenu();
			m2.setText("查询");

			final JMenu m4 = new JMenu();
			m4.setText("配置");

			final JMenu m3 = new JMenu();
			m3.setText("用户管理");

			JMenuItem item3_1 = new JMenuItem();
			item3_1.setText("修改密码");
			//ImageIcon logo3_1=new ImageIcon(LoginMain.app_path+"/image/application_key.png");   //这里定义一个Icon图片
			ImageIcon logo3_1=new ImageIcon(LoginMain.app_path+"/image/application_key.png");
			item3_1.setIcon(logo3_1);  //这里设置Icon图片到JMenu

			JMenuItem item3_2 = new JMenuItem();
			item3_2.setText("增加用户");
			ImageIcon logo3_2=new ImageIcon(LoginMain.app_path+"/image/user_add.png");   //这里定义一个Icon图片
			item3_2.setIcon(logo3_2);  //这里设置Icon图片到JMenu

			JMenuItem item11 = new JMenuItem();
			item11.setText("退出");
			item11.setAccelerator(KeyStroke.getKeyStroke('Q', ActionEvent.CTRL_MASK)); //增加Crtl快捷键
			ImageIcon logo1=new ImageIcon(LoginMain.app_path+"/image/exit.png");   //这里定义一个Icon图片
			item11.setIcon(logo1);  //这里设置Icon图片到JMenu

			JMenuItem item1_1 = new JMenuItem();
			item1_1.setText("注销");
			item1_1.setAccelerator(KeyStroke.getKeyStroke('O', ActionEvent.CTRL_MASK)); //增加Crtl快捷键
			ImageIcon logo1_1=new ImageIcon(LoginMain.app_path+"/image/user_go.png");   //这里定义一个Icon图片
			item1_1.setIcon(logo1_1);  //这里设置Icon图片到JMenu

			JMenuItem item12 = new JMenuItem();
			item12.setText("磁盘信息查询");			
			item12.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK));  
			ImageIcon logo12=new ImageIcon(LoginMain.app_path+"/image/drive_disk.png");   //这里定义一个Icon图片
			item12.setIcon(logo12);  //这里设置Icon图片到JMenu
			
			JMenuItem item13 = new JMenuItem();
			item13.setText("升级记录查询");			
			item13.setAccelerator(KeyStroke.getKeyStroke('U', ActionEvent.CTRL_MASK)); 
			ImageIcon logo13=new ImageIcon(LoginMain.app_path+"/image/magnifier.png");   //这里定义一个Icon图片
			item13.setIcon(logo13);  //这里设置Icon图片到JMenu

			JMenuItem item31 = new JMenuItem();
			item31.setText("查询设备");
			ImageIcon logo31=new ImageIcon(LoginMain.app_path+"/image/magnifier.png");   //这里定义一个Icon图片
			item31.setIcon(logo31);  //这里设置Icon图片到JMenu

			JMenuItem item41 = new JMenuItem();
			item41.setText("系统参数");
			ImageIcon logo41=new ImageIcon(LoginMain.app_path+"/image/config.png");   //这里定义一个Icon图片
			item41.setIcon(logo41);  //这里设置Icon图片到JMenu
			
			JMenuItem item51 = new JMenuItem();
			item51.setText("升级设备");
			ImageIcon logo51=new ImageIcon(LoginMain.app_path+"/image/computer.png");   //这里定义一个Icon图片
			item51.setIcon(logo51);  //这里设置Icon图片到JMenu

			JMenuItem item21 = new JMenuItem();
			item21.setText("版本信息");
			ImageIcon logo21=new ImageIcon(LoginMain.app_path+"/image/information.png");   //这里定义一个Icon图片
			item21.setIcon(logo21);  //这里设置Icon图片到JMenu
			
			item21.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					//JOptionPane.showMessageDialog(null,
					///	"欢迎使用运维管理程序"+LoginMain.RIGHTINFO+"   Designed by wangrui16@gmail.com");
					AboutInfo aboutInfo = new AboutInfo();
					aboutInfo.setVisible(true);
				}
			});

			item11.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});

			item1_1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub

					// JOptionPane.showMessageDialog(null, "已退出！");
					frame.setVisible(false);
					login = new LoginMain();
					login.setLocationRelativeTo(null);
					login.setVisible(true);
				}
			});

			item12.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					// 未分页表格
					if(LoginMain.userType.getText().equals("0")){
						ChkResult t = new ChkResult(0);
						t.setLocationRelativeTo(null);
						t.setVisible(true);
					}else if(LoginMain.userType.getText().equals("1")){
						ChkResult t = new ChkResult(1);
						t.setLocationRelativeTo(null);
						t.setVisible(true);
					}
				}
			});
			
			item13.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(LoginMain.userType.getText().equals("0")){
					UpdateLog updateLogWin = new UpdateLog(0);
					updateLogWin.setVisible(true);
					}else if(LoginMain.userType.getText().equals("1")){
						UpdateLog updateLogWin = new UpdateLog(1);
						updateLogWin.setVisible(true);
					}
				}
			});

			item31.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(LoginMain.userType.getText().equals("0")){
						DeviceConfig deviceConfig = new DeviceConfig(0);
						deviceConfig.setLocationRelativeTo(null);
						deviceConfig.setVisible(true);	
					}else if(LoginMain.userType.getText().equals("1")){
						DeviceConfig deviceConfig = new DeviceConfig(1);
						deviceConfig.setLocationRelativeTo(null);
						deviceConfig.setVisible(true);	
					}
					
				}
			});

			item3_2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					AddUser addUserWin = new AddUser();
					addUserWin.setVisible(true);
				}
			});
			
			item41.addActionListener(new ActionListener() {   

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SystemConfig systemConfig = new SystemConfig();
					systemConfig.setLocationRelativeTo(null);
					systemConfig.setVisible(true);

				}
			});   
			
			item51.addActionListener(new ActionListener() {   

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(LoginMain.userType.getText().equals("0")){
						UpdateDeviceConfig systemConfig = new UpdateDeviceConfig(0);
						systemConfig.setLocationRelativeTo(null);
						systemConfig.setVisible(true);
					}else if(LoginMain.userType.getText().equals("1")){
						UpdateDeviceConfig systemConfig = new UpdateDeviceConfig(1);
						systemConfig.setLocationRelativeTo(null);
						systemConfig.setVisible(true);
					}
					

				}
			});
			
			item3_1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					ModifedPswd modifedPswWin= new ModifedPswd();
					modifedPswWin.setVisible(true);
				}
			});
			//按权限显示相应菜单
			if (LoginMain.userType.getText().equals("0")) {

				m2.add(item12);
				m2.add(item13);
				m1.add(item1_1);
				m1.addSeparator();
				m1.add(item11);
				m6.add(item21);
				m4.add(item41);
				m4.addSeparator();
				m4.add(item31);
				m4.addSeparator();
				m4.add(item51);
				m3.add(item3_1);
				m3.addSeparator();
				m3.add(item3_2);

				menuBar.add(m1);
				menuBar.add(m3);
				menuBar.add(m2);				
				menuBar.add(m4);
				menuBar.add(m6);

			} else if (LoginMain.userType.getText().equals("1")) {

				m2.add(item12);
				m1.addSeparator();
				m2.add(item13);
				m1.add(item1_1);
				m1.addSeparator();
				m1.add(item11);
				m6.add(item21);
				//m4.add(item41);
				m4.add(item51);
				m4.addSeparator();
				m4.add(item31);
				m3.add(item3_1);
				//m3.addSeparator();
				//m3.add(item3_2);

				menuBar.add(m1);
				menuBar.add(m3);
				menuBar.add(m2);
				menuBar.add(m4);
				menuBar.add(m6);
			}

		}
		return menuBar;
	}

	private JTree getTree() {
		if (jTree == null) {
			//根节点
			DefaultMutableTreeNode Root= new DefaultMutableTreeNode();	
			//第二节点
			DefaultMutableTreeNode root1 = new DefaultMutableTreeNode("其他功能");  
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("系统升级");		
			//第三节点
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("磁盘空间查询"); 
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("账户系统升级");
			DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("账户测试环境准备");
			DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("账户测试环境恢复");
			DefaultMutableTreeNode node5 = new DefaultMutableTreeNode("行情查询");
			DefaultMutableTreeNode node6 = new DefaultMutableTreeNode("Excel文件导入");
			DefaultMutableTreeNode node7 = new DefaultMutableTreeNode("融资融券系统升级");
			DefaultMutableTreeNode node8 = new DefaultMutableTreeNode("升级记录查询");
			DefaultMutableTreeNode node9 = new DefaultMutableTreeNode("文件比较");
			DefaultMutableTreeNode node10 = new DefaultMutableTreeNode("SO文件版本查询");
			
						
			
			

			if(LoginMain.userType.getText().equals("0")){
				Root.add(root);
				Root.add(root1);
				root1.add(node1);
				root.add(node2);
				root.add(node8);
				root.add(node9);
				root.add(node10);
				//root1.add(node4);
				root1.add(node5);
				root1.add(node6);
			}else if(LoginMain.userType.getText().equals("1")){
				Root.add(root);
				root.add(node1);
				root.add(node7);
				root.add(node9);
				root.add(node10);
			}

			jTree = new JTree(Root);
			jTree.setRootVisible(false); //设置跟节点不可见
			jTree.expandRow(1);//设置节点数全部展开
			jTree.expandRow(0);//设置节点数全部展开
			
			DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();

	        cellRenderer.setOpenIcon(new ImageIcon(LoginMain.app_path+"/image/node.gif"));
	        //cellRenderer.setOpenIcon(openIcon);
	        cellRenderer.setLeafIcon(new ImageIcon(LoginMain.app_path+"/image/items.gif")); 
	        
	        jTree.setCellRenderer(cellRenderer);
			
	        jTree.addTreeSelectionListener(new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {

					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
					str = selectNode.toString();

					if (str.equals("功能")) {
						return;
					} else if(str.equals("账户系统升级")){

						UpdateSummaryPanel updateSummaryPanel = new UpdateSummaryPanel();
						updateSummaryPanel.setVisible(true);
						// UpdatePanel updatePanel = new UpdatePanel();
			            // tabbedPane.addTab(str, new ImageIcon(LoginMain.app_path+"/items.gif"),updatePanel);
			            // tabbedPane.setSelectedComponent(updatePanel);// 新建后默认显示新建的tab
						// tabbedPane.getName();
						 
					}else if(str.equals("账户测试环境准备")){
						
						PreparePanel preparePanel = new PreparePanel();
						tabbedPane.add(str,preparePanel);
						tabbedPane.setSelectedComponent(preparePanel);
						tabbedPane.getName();
					}else if(str.equals("行情查询")){
						
						HqQuery hqQueryPanel = new HqQuery();
						hqQueryPanel.setVisible(true);
						//tabbedPane.add(str,hqQueryPanel);
						//tabbedPane.setSelectedComponent(hqQueryPanel);
						//tabbedPane.getName();
					}else if(str.equals("Excel文件导入")){
						
						ImportDBF importDBFPanel = new ImportDBF();
						importDBFPanel.setVisible(true);

					}else if(str.equals("融资融券系统升级")){
						
						UpdateSummaryRzrqPanel updateSummaryRzrqPanel = new UpdateSummaryRzrqPanel();
						updateSummaryRzrqPanel.setVisible(true);
						
						//UpdatePanel updatePanel = new UpdatePanel(1);
						//tabbedPane.add(str,updatePanel);
						//tabbedPane.setSelectedComponent(updatePanel);
						//tabbedPane.getName();

					}else if(str.equals("磁盘空间查询")){
						
						tabbedPane.addTab(str, pndisk);
						tabbedPane.setSelectedComponent(pndisk);
						tabbedPane.getName();
					}else if(str.equals("升级记录查询")){
						
						UpdateLog updateLog = new UpdateLog(0);
						updateLog.setVisible(true);
						//tabbedPane.addTab(str, updateLog);
						//tabbedPane.setSelectedComponent(updateLog);
						//tabbedPane.getName();
					}else if(str.equals("文件比较")){
						
						if(LoginMain.userType.getText().equals("0")){
							SoFileCompare soFileCompare = new SoFileCompare(0);
							soFileCompare.setVisible(true);
						}else if(LoginMain.userType.getText().equals("1")){
							SoFileCompare soFileCompare = new SoFileCompare(1);
							soFileCompare.setVisible(true);
						}
						
					}else if(str.equals("SO文件版本查询")){
						if(LoginMain.userType.getText().equals("0")){
							SoVersionSearch soVersionSearch = new SoVersionSearch(0);
							soVersionSearch.setVisible(true);
						}else if(LoginMain.userType.getText().equals("1")){
							SoVersionSearch soVersionSearch = new SoVersionSearch(1);
							soVersionSearch.setVisible(true);
						}
					}

				}
			});

		}

		return jTree;

	}

	/*
	 * 构造程序中用到的各个组建
	 */
	private void addComponent() {
		// TODO Auto-generated method stub
		ptop = new JPanel(); // 第一个Panel
		button = new JButton("开始查询");
		button1 = new JButton("开始查询1");
		pwest = new JPanel();
		pmenu = new JPanel();
		// pwest.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		// pwest.setBorder(new TitledBorder(""));
		pndisk = new JPanel(new BorderLayout(10, 10));
		pmenu.add(getMenu());
		ptop.setSize(600, 500);

		jTextArea = new JTextArea(14, 48);
		jTextArea.setMargin(new Insets(5, 5, 5, 5));
		jTextArea.setEditable(false);

		jTextArea1 = new JTextArea(14, 48);
		jTextArea1.setMargin(new Insets(5, 5, 5, 5));
		jTextArea1.setEditable(false);

		jScrollPanel = new JScrollPane(jTextArea);
		jScrollPanel.setName("磁盘");

		mainLabel = new JLabel("欢迎使用账户系统升级程序");
		mainLabel.setFont(new Font("华文楷体", Font.ITALIC, 24));
		// ptop.add(mainLabel);
		pndisk.add(jScrollPanel);
		pndisk.add(button, BorderLayout.SOUTH);

		systimeLabel = new JLabel("时间", Label.RIGHT);
		// systimeLabel.setBounds(new Rectangle(0, 0, 0, 40));
		s = new SystTimeUpdateTimer(systimeLabel);

		bar = new JProgressBar(0, 100);
		bar.setIndeterminate(true);
		bar.setString("正在查询，请稍等！");
		bar.setStringPainted(true);// 设置在进度条中显示百分比
		bar.setVisible(true);

		tabbedPane = new JTabbedPane();
		tabbedPane.setSize(600, 380);
		// tabbedPane.setBounds(0, 0, 500, 300);
		// tabbedPane.setToolTipText("双击关闭");
		tabbedPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					int i = tabbedPane.getSelectedIndex();
					System.out.println(i);
					if (i == 0) {
						return;
					} else
						tabbedPane.removeTabAt(i);
				}
			}
		});
		//if(LoginMain.userType.equals("0")){
		//	tabbedPane.addTab("磁盘信息查询", pndisk);
		//}
		
		pcenter = new JPanel(); // 第二个Panel
		userTile = new JLabel("当前用户：");

		Box box = Box.createHorizontalBox();
		box.add(userTile);
		pcenter.setLayout(new FlowLayout());
		pcenter.add(box);
		pcenter.add(LoginMain.userLabel);
		pcenter.add(LoginMain.userName);

		jScrollTreePanel = new JScrollPane();
		jScrollTreePanel.setViewportView(getTree());
		pwest.add(jScrollTreePanel);
		frame.setJMenuBar(getMenu());

		// TreeDemo t = new TreeDemo();

		frame.add(pmenu, BorderLayout.NORTH);
		frame.add(tabbedPane, BorderLayout.CENTER);
		frame.add(pcenter, BorderLayout.SOUTH);
		frame.add(getTree(), BorderLayout.WEST);

	}

	/*
	 * 设置程序主窗口
	 */
	private void setWindow() {
		// TODO Auto-generated method stub
		
		frame = new JFrame("运维管理程序");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon=new ImageIcon(LoginMain.app_path+"/image/application_form.png");//图标路径
	    frame.setIconImage(icon.getImage());
		frame.setSize(790, 440);
		frame.setLocationRelativeTo(null); // setLocationRelativeTo必须在setSize()下面
		frame.setResizable(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("开始查询")) {
			t1 = new T1(this);
			t1.start();
			jdiaLog = new JDialog(frame, "searching!", true);
			jdiaLog.add(bar);
			jdiaLog.setSize(300, 24);
			jdiaLog.setLocationRelativeTo(null);// 必须放到setSize()后面
			jdiaLog.setAlwaysOnTop(true);
			jdiaLog.setUndecorated(true); // 取消对话框上的关闭按钮
			jdiaLog.setVisible(true);
			button.disable();

		}
	}

	/*
	 * 程序入口
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainPanel();
				
			}
		});
	}

}
