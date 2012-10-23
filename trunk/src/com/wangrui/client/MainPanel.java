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
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
		queryDeviceInfoAll();

		// 执行vbs命令阶段
		System.out.println("执行vbs命令阶段!");
		File batFile = new File(CollectSysConfig.filePathresult + "\\"); // vbs的目录
		String[] cmd = new String[] { "wscript",
				CollectSysConfig.filePathresult + "\\vbsCommd.vbs" };

		System.out.println("vbs目录：" + CollectSysConfig.filePathresult
				+ "\\vbsCommd.vbs");
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
		insertCommdToDatabase();

	}

	/*
	 * 将windows设备查询后数据插入数据库
	 */
	public void insertCommdToDatabase() {

		File txtfile = new File(CollectSysConfig.filePathresult
				+ "//queryresult_windows.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
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
							+ "'*100/'"
							+ s[3]
							+ "',0)>= '"
							+ CollectSysConfig.utilresult
							+ "' then '1'else '0' end)";

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
		File bakfile = new File(CollectSysConfig.filePathresult + "//");
		System.out.println(CollectSysConfig.filePathresult + "//");
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
			FileWriter fw = new FileWriter(CollectSysConfig.filePathresult
					+ "//queryresult_windows.txt", true);
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
	public List queryDeviceInfoAll() {

		// 生成执行命令并写入vbs中
		try {
			File bakfile = new File(CollectSysConfig.filePathresult
					+ "//vbsCommdtest.vbs");
			t.conn_query = new DBConnection();
			String sql = "select * from test.device order by ip desc";
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

						insertLinuxCommdToDatabase(t.rs_queryDeviceInfo
								.getString("ip"));

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
	public void insertLinuxCommdToDatabase(String ip) {

		File txtfile = new File(CollectSysConfig.filePathresult
				+ "//queryresult_linux.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into deviceDisk1 (date,ip,deviceId,freespace,size,util,type) values(date_format(now(),'%Y%m%d'),'"
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
							+ "'),0)>= 40 then '1'else '0' end)";

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
			File file = new File(CollectSysConfig.filePathresult
					+ "\\queryresult_linux.txt");
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
			t.dos_vbs = new RandomAccessFile(CollectSysConfig.filePathresult
					+ "\\" + "vbsCommd" + ".vbs", "rw");
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
					+ CollectSysConfig.filePathresult
					+ "\\"
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

class OSSearch extends Thread {

	private MainPanel t;

	OSSearch(MainPanel td) {
		t = td;
	}

	public void run() {

		t.jTextArea1.append("【" + getSystime() + "】︰" + "开始写入查询命令，请等待!" + "\n");
		t.jTextArea1.paintImmediately(t.jTextArea1.getBounds());

		// 查参数配置表并写入查询命令
		queryDeviceInfoAll();

		// 执行vbs命令阶段
		System.out.println("执行vbs命令阶段!");
		File batFile = new File(CollectSysConfig.filePathresult + "\\"); // vbs的目录
		String[] cmd = new String[] { "wscript",
				CollectSysConfig.filePathresult + "\\vbsCommdOS.vbs" };

		System.out.println("vbs目录：" + CollectSysConfig.filePathresult
				+ "\\vbsCommdOS.vbs");
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
		insertCommdToDatabase();

	}

	/*
	 * 将windows设备查询后数据插入数据库
	 */
	public void insertCommdToDatabase() {

		File txtfile = new File(CollectSysConfig.filePathresult
				+ "//queryresult_windows_os.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
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
							+ "'*100/'"
							+ s[3]
							+ "',0)>= '"
							+ CollectSysConfig.utilresult
							+ "' then '1'else '0' end)";

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
		File bakfile = new File(CollectSysConfig.filePathresult + "//");
		System.out.println(CollectSysConfig.filePathresult + "//");
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
			FileWriter fw = new FileWriter(CollectSysConfig.filePathresult
					+ "//queryresult_windows.txt", true);
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
	public List queryDeviceInfoAll() {

		// 生成执行命令并写入vbs中
		try {
			File bakfile = new File(CollectSysConfig.filePathresult
					+ "//vbsCommdtest.vbs");
			t.conn_query = new DBConnection();
			String sql = "select * from test.device order by ip desc";
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

						insertLinuxCommdToDatabase(t.rs_queryDeviceInfo
								.getString("ip"));

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
	public void insertLinuxCommdToDatabase(String ip) {

		File txtfile = new File(CollectSysConfig.filePathresult
				+ "//queryresult_linux.txt");

		try {
			BufferedReader bf = new BufferedReader(new FileReader(txtfile));
			String str;
			t.conn_insertLiunxCommdToDatabase = new DBConnection();
			while (null != (str = bf.readLine())) {
				if (!str.trim().isEmpty()) {
					String s[] = str.split(" ");
					String sql = "insert into deviceDisk1 (date,ip,deviceId,freespace,size,util,type) values(date_format(now(),'%Y%m%d'),'"
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
							+ "'),0)>= 40 then '1'else '0' end)";

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
			File file = new File(CollectSysConfig.filePathresult
					+ "\\queryresult_linux.txt");
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
			t.dos_vbs = new RandomAccessFile(CollectSysConfig.filePathresult
					+ "\\" + "vbsCommd" + ".vbs", "rw");
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
					+ CollectSysConfig.filePathresult
					+ "\\"
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
	private JPanel ptop, pwest, pndisk;
	private JButton button;
	private JMenuBar menuBar;
	private JTree jTree;
	private JScrollPane jScrollPanel, jScrollPanel1, jScrollTreePanel,
			jScrollPanel3;
	private JLabel mainLabel, systimeLabel, userTile;
	private JPanel pcenter;
	private T1 t1;
	private JTabbedPane tabbedPane;
	private SystTimeUpdateTimer s;
	public static JLabel userLabel;

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

		// 从systemconfig表中去参数配置

		final CollectSysConfig collectSysConfig = new CollectSysConfig();
		System.out.println(CollectSysConfig.filePathresult);

		// 先删除已存在的临时文件
		File delbatFile = new File(CollectSysConfig.filePathresult + "//"); // 生成bat的目录
		final File[] delbatFiles = delbatFile.listFiles();
		for (int j = 0; j < delbatFiles.length; j++) {
			if (delbatFiles[j].getName().endsWith(".vbs")
					|| delbatFiles[j].getName().endsWith(".bak")
					|| delbatFiles[j].getName().endsWith(".txt")) {

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
			JMenu m1 = new JMenu();
			m1.setText("文件");

			JMenu m6 = new JMenu();
			m6.setText("帮助");

			JMenu m2 = new JMenu();
			m2.setText("查询");

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
					JOptionPane.showMessageDialog(null,
							"磁盘查询程序V2.1，支持Windows、Linux系统！");
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

					// 未分页表格
					ChkResult t = new ChkResult();
					t.setVisible(true);
					t.setLocationRelativeTo(null);

					// 分页表格
					// ChkResultTable t = new ChkResultTable();
					// t.setLocationRelativeTo(null);
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

	private JTree getTree() {
		if (jTree == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("功能");
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("磁盘空间查询");
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("内存信息查询");
			DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("CPU信息查询");
			// DefaultMutableTreeNode node4 = new
			// DefaultMutableTreeNode("Readme");
			root.add(node1);
			root.add(node2);
			root.add(node3);
			// root.add(node4);

			// DefaultMutableTreeNode leafnode = new
			// DefaultMutableTreeNode("公司文件");
			// node1.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("私人文件");
			// node1.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("个人信件");
			//
			// leafnode = new DefaultMutableTreeNode("本机磁盘(C:)");
			// node2.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("本机磁盘(D:)");
			// node2.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("本机磁盘(E:)");
			// node2.add(leafnode);
			//
			// DefaultMutableTreeNode node31 = new
			// DefaultMutableTreeNode("网站列表");
			// node3.add(node31);
			//
			// leafnode = new DefaultMutableTreeNode("奇摩站");
			// node31.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("职棒消息");
			// node31.add(leafnode);
			// leafnode = new DefaultMutableTreeNode("网络书店");
			// node31.add(leafnode);

			jTree = new JTree(root);
			jTree.addTreeSelectionListener(new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {

					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) jTree
							.getLastSelectedPathComponent();
					String str = selectNode.toString();

					if (str.equals("功能")) {
						return;
					} else {
						jScrollPanel1 = new JScrollPane(jTextArea1);
						jScrollPanel1.setName("内存");

						System.out.println("str=" + str);

						tabbedPane.addTab(str, jScrollPanel1);
						jScrollPanel1.setName(str);
						tabbedPane.setSelectedComponent(jScrollPanel1);// 新建后默认显示新建的tab
						tabbedPane.getName();

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
		pwest = new JPanel();
		pwest.setSize(100, 200);
		pwest.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		pwest.setBorder(new TitledBorder(""));
		pndisk = new JPanel(new BorderLayout());

		jTextArea = new JTextArea(19, 48);
		jTextArea.setMargin(new Insets(5, 5, 5, 5));
		jTextArea.setEditable(false);

		jTextArea1 = new JTextArea(19, 48);
		jTextArea1.setMargin(new Insets(5, 5, 5, 5));
		jTextArea1.setEditable(false);

		jScrollPanel = new JScrollPane(jTextArea);
		jScrollPanel.setName("磁盘");

		mainLabel = new JLabel("欢迎使用磁盘空间查询系统");
		mainLabel.setFont(new Font("华文楷体", Font.ITALIC, 24));
		// ptop.add(mainLabel);
		pndisk.add(jScrollPanel, BorderLayout.CENTER);
		pndisk.add(button, BorderLayout.SOUTH);

		systimeLabel = new JLabel("时间", Label.RIGHT);
		// systimeLabel.setBounds(new Rectangle(0, 0, 0, 40));
		s = new SystTimeUpdateTimer(systimeLabel);

		bar = new JProgressBar(0, 100);
		bar.setIndeterminate(true);
		bar.setString("正在查询中，请稍等！");
		bar.setStringPainted(true);// 设置在进度条中显示百分比
		bar.setVisible(true);

		tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
		tabbedPane.setBounds(0, 0, 500, 300);
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
		tabbedPane.addTab("磁盘信息查询", pndisk);
		ptop.add(tabbedPane);

		// tabbedPane.addTab("内存信息查询", jScrollPanel1);
		// ptop.add(tabbedPane);

		pcenter = new JPanel(); // 第二个Panel
		userTile = new JLabel("当前用户：");
		// pcenter.setSize(300, 80);
		// pcenter.add(systimeLabel);
		// pcenter.setLayout(new BoxLayout(pcenter, BoxLayout.Y_AXIS));
		pcenter.add(userTile);
		pcenter.add(userLabel);

		jScrollTreePanel = new JScrollPane();
		jScrollTreePanel.getViewport().add(getTree(), null);
		pwest.add(jScrollTreePanel);

		// TreeDemo t = new TreeDemo();

		frame.add(getMenu(), BorderLayout.NORTH);
		frame.add(ptop, BorderLayout.CENTER);
		frame.add(pcenter, BorderLayout.SOUTH);
		frame.add(pwest, BorderLayout.WEST);

	}

	/*
	 * 设置程序主窗口
	 */
	private void setWindow() {
		// TODO Auto-generated method stub
		frame = new JFrame("磁盘空间查询");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 460);
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
			jdiaLog.setSize(300, 20);
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
