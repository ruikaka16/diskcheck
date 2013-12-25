package com.wangrui.client;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

import com.easyjf.util.FileCopyUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wangrui.dbf.DBFException;
import com.wangrui.dbf.DBFReader;
import com.wangrui.dbf.ReadHQ;
import com.wangrui.server.DBConnection;

public class HqQuery extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private JExpectSearchField jTextField1;
	private DBConnection conn_getHq;
	private ResultSet rs_getHq;
	private Timer timer, timer1, timer2, timer3;

	public HqQuery() {

		initComponents();
	}

	private void initComponents() {
		// TODO Auto-generated method stub
		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new JExpectSearchField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();

		// jLabel3.setFont(new Font("宋体",Font.BOLD,11));
		// jLabel11.setFont(new Font("宋体",Font.BOLD,11));

		int w = (Toolkit.getDefaultToolkit().getScreenSize().width) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height) / 2;

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("实时行情查询");

		jLabel1.setText("股票代码：");

		jButton1.setText("行情转入");

		jButton2.setText("查找");

		jButton3.setText("导出");

		jButton4.setText("监控");

		jLabel2.setText("当前价格：");

		jLabel4.setText("开盘价格：");

		jLabel5.setText("涨停价格：");

		jLabel6.setText("跌停价格：");

		// jLabel3.setText("label3");

		// jLabel7.setText("jLabel7");

		// jLabel8.setText("jLabel8");

		// jLabel9.setText("jLabel9");

		jLabel10.setText("涨跌幅%：");

		// jLabel11.setText("jLabel11");

		DBConnection conn_searchSuggest = new DBConnection();
		String sql = "select a.s1,a.s2 from (SELECT s1,s2 FROM shhq union select hqzqdm ,hqzqjc from szhq)a";
		ResultSet rs_search = conn_searchSuggest.executeQuery(sql);

		try {
			List<Object> value = new ArrayList<Object>();
			while (rs_search.next()) {

				value.add(rs_search.getString("s1") + "  "
						+ rs_search.getString("s2"));
				// System.out.println(rs_search.getString("HQZQDM"));
				jTextField1 = new JExpectSearchField(value, 5);

			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "获取证券代码失败！");
		}

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(layout
										.createSequentialGroup()
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING,
														false)
												.add(jLabel1,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.add(jLabel2,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(layout
														.createSequentialGroup()
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED)
														.add(jTextField1,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																127,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
												.add(layout
														.createSequentialGroup()
														.add(6, 6, 6)
														.add(jLabel3))
												.add(layout
														.createSequentialGroup()
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED)
														.add(jLabel12)
														.add(40, 40, 40)
														.add(jLabel10)
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.UNRELATED)
														.add(jLabel11)))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												36, Short.MAX_VALUE)
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING,
														false)
												.add(jButton1,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.add(jButton4,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.add(org.jdesktop.layout.GroupLayout.TRAILING,
														jButton2,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)))
								.add(layout
										.createSequentialGroup()
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(layout
														.createSequentialGroup()
														.add(jLabel5)
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED)
														.add(jLabel8))
												.add(layout
														.createSequentialGroup()
														.add(jLabel6)
														.addPreferredGap(
																org.jdesktop.layout.LayoutStyle.RELATED)
														.add(jLabel9)))
										.add(0, 0, Short.MAX_VALUE))
								.add(layout
										.createSequentialGroup()
										.add(jLabel4)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(jLabel7)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).add(jButton4)))
						.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(jLabel1)
								.add(jTextField1,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(jButton1))
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(jButton2).add(jLabel2).add(jLabel3)
								.add(jLabel10).add(jLabel11).add(jLabel12))
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.UNRELATED)
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(layout
										.createSequentialGroup()
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.BASELINE)
												.add(jLabel4).add(jLabel7))
										.add(14, 14, 14)
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.BASELINE)
												.add(jLabel5).add(jLabel8))
										.add(18, 18, 18)
										.add(layout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.BASELINE)
												.add(jLabel6).add(jLabel9)))
								.add(jButton4))
						.addContainerGap(37, Short.MAX_VALUE)));

		// setAlwaysOnTop(true);
		ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/hq.gif");//图标路径
        setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLocation(500, 300);
		pack();

		/*
		 * jButton3.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * File reportFile = new File("R:\\wangrui\\report2.jasper");
		 * JasperReport jasperReport = null; try { jasperReport = (JasperReport)
		 * JRLoader.loadObject(reportFile.getPath()); } catch (JRException e1) {
		 * // TODO Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * Map parameters = new HashMap<String, String>();
		 * parameters.put("barcode", "564789"); try {
		 * Class.forName("com.mysql.jdbc.Driver"); Connection conn
		 * =DriverManager
		 * .getConnection("jdbc:mysql://localhost:3306/test","root", "wangrui");
		 * } catch (ClassNotFoundException e2) { // TODO Auto-generated catch
		 * block e2.printStackTrace(); } catch (SQLException e3) { // TODO
		 * Auto-generated catch block e3.printStackTrace(); }
		 * 
		 * try { JasperPrint print = JasperFillManager.fillReport(jasperReport,
		 * parameters); try { OutputStream output = new FileOutputStream(new
		 * File("R:\\wangrui\\report2.pdf")); } catch (FileNotFoundException e1)
		 * { // TODO Auto-generated catch block e1.printStackTrace(); }
		 * JasperExportManager.exportReportToPdfFile(print,
		 * "R:\\wangrui\\report2.pdf"); } catch (JRException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * } });
		 */
		jButton4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (jButton4.getText().equals("监控")) {

					jButton4.setText("监控中");
					timer3 = new Timer();
					timer3.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							DBConnection dbconn = new DBConnection();
							String sql = "select hqcjbs from szhq where hqzqdm = '000000'";
							final ResultSet rs = dbconn.executeQuery(sql);
							System.out.println("监控的时间=" + jLabel13.getText());
							try {
								while (rs.next()) {

									if (jLabel13.getText().equals(
											rs.getString("hqcjbs"))) {
										JOptionPane.showMessageDialog(null,
												"行情中断");
										timer3.cancel();
										jButton4.setText("监控");
									}
									jLabel13.setText(rs.getString("hqcjbs"));
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}, 0, 5000);

				} else if (jButton4.getText().equals("监控中")) {
					timer3.cancel();
					jButton4.setText("监控");
				}
			}
		});

		jButton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer2 = new Timer();
				timer2.schedule(new TimerTask() {
					public void run() {
						getHqCurrentPrice(jTextField1.getText());
						// System.out.println(jTextField1.getText().substring(jTextField1.getText().length()-6,
						// jTextField1.getText().length()));
					}
				}, 0, 1000);
			}

		});

		jButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (jButton1.getText().equals("行情转入")) {
					// ReadHQ readHq = new ReadHQ();
					jButton1.setText("运行中");
					jButton1.setSelected(true);
					timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							try {
								// System.out.println("开始执行读DBF文件！");
								long start = System.currentTimeMillis();
								readDBF("D:/hq/szhq/sjshq.dbf", "C:/szhq.txt");
								readDBF("D:/hq/shhq/show2003.dbf",
										"C:/shhq.txt");
								long end = System.currentTimeMillis();
								// System.out.println("耗时="+(end-start));

							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								System.out.println("DBF文件没找到！");
								e1.printStackTrace();
							} catch (DBFException e1) {
								// TODO Auto-generated catch block
								System.out.println("读取DBF文件失败！");
								e1.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							executeCommd("C:/szhq.txt", "szhq");
							executeCommd("C:/shhq.txt", "shhq");
						}

					}, 0, 1500);

				} else if (jButton1.getText().equals("运行中")) {
					timer.cancel();
					jButton1.setText("行情转入");
				}
				// ReadHQ readHq = new ReadHQ();
				// jButton1.setText("运行中");
				// ReadHQ.timer.cancel();
			}
		});

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				if (jButton1.getText().equals("运行中")) {
					timer.cancel();
				} else if (jButton4.getText().equals("监控中")) {
					timer3.cancel();
				}
				timer2.cancel();
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});

	}

	private void getHqCurrentPrice(final String stk_code) {
		// TODO Auto-generated method stub
		// timer1 = new Timer();
		// timer1.schedule(new TimerTask() {
		// public void run() {
		conn_getHq = new DBConnection();
		String sql = "select * from (SELECT s1,s4,s8,round(s3*1.1,2) as ztjg,round(s3*0.9,2) as dtjg ,round((s8-s3)*100/s3,2) as zdf FROM shhq union select hqzqdm,hqjrkp,hqzjcj,round(hqzrsp*1.1,2) ,round(hqzrsp*0.9 ,2) ,round((hqzjcj-hqzrsp)*100/hqzrsp,2) from szhq) a where a.s1 = '"
				+ stk_code + "'";
		// System.out.println(sql);
		rs_getHq = conn_getHq.executeQuery(sql);
		try {
			while (rs_getHq.next()) {
				jLabel3.setText(rs_getHq.getString("s8"));
				jLabel7.setText(rs_getHq.getString("s4"));
				jLabel8.setText(rs_getHq.getString("ztjg"));
				jLabel9.setText(rs_getHq.getString("dtjg"));
				jLabel11.setText(rs_getHq.getString("zdf"));
				if (Double.parseDouble(rs_getHq.getString("zdf")) > 0) {
					jLabel3.setForeground(Color.RED);
					jLabel11.setForeground(Color.RED);
				} else if (Double.parseDouble(rs_getHq.getString("zdf")) < 0) {
					jLabel3.setForeground(Color.GREEN);
					jLabel11.setForeground(Color.GREEN);
				}
				jLabel8.setForeground(Color.RED);
				jLabel9.setForeground(Color.GREEN);
				// System.out.println(rs_getHq.getString("s8"));
			}
			rs_getHq.close();
			conn_getHq.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		// }, 0, 1000);
	}

	public static void readDBF(String dbfpath, String txtpath)
			throws IOException {

		InputStream fis = new FileInputStream(dbfpath);
		// 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息
		DBFReader dbfreader = new DBFReader(fis);
		dbfreader.setCharactersetName("GB2312"); // 设置编码方式
		File txtFile = new File(txtpath);
		FileWriter fw = new FileWriter(txtFile);
		BufferedWriter bw = new BufferedWriter(fw);

		int i;

		// for (i=0; i<dbfreader.getFieldCount(); i++) {
		// System.out.print(dbfreader.getField(i).getName()+"  ");
		// }

		Object[] aobj;
		// aobj = dbfreader.nextRecord();
		while ((aobj = dbfreader.nextRecord()) != null) {

			String inputStr = new String();
			for (int j = 0; j < aobj.length; j++) {
				if (aobj[j].equals("")) {
					inputStr += "";
					// System.out.println(aobj[j - 1].toString());
				} else {
					inputStr += aobj[j] + "|";
				}
			}
			inputStr += "\n";
			fw.write(inputStr);
			// bw.write(inputStr);
		}
		fw.close();

		// executeCommd("c:/szhq.txt");
		// executeCommd("c:/shhq.txt");
	}

	public static void executeCommd(String txtpath, String table) {

		// String SQLStr =
		// "LOAD DATA INFILE '"+txtpath+"' REPLACE INTO TABLE szhq FIELDS TERMINATED BY '|';";
		String SQLStr = "LOAD DATA INFILE '" + txtpath
				+ "' REPLACE INTO TABLE " + table
				+ " FIELDS TERMINATED BY '|';";
		// System.out.println(SQLStr);

		Statement stmt = null;
		// CallableStatement cstmt = null;
		DBConnection conn = new DBConnection();

		try {
			// cstmt = conn.prepareCall("{call update_szhq()}"); //
			// 调用update_szhq()存储过程
			// cstmt.execute();
			stmt = conn.execute(SQLStr);
			// stmt.execute(SQLStr);
			// System.out.println("导入完成！");

		} finally {
			try {

				// cstmt.close();
				stmt.close();
				conn.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {

		HqQuery a = new HqQuery();
		a.setVisible(true);
	}
}
