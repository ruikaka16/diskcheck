package com.wangrui.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.gjt.mm.mysql.Driver;

import com.wangrui.server.DBConnection;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class PreparePanel extends JPanel{

	private static final Logger logger = Logger.getLogger(UpdatePanel.class);
	
	private String ORACLE_SERVER_NAME_DB = "wsacdb";
	
	private String ORACLE_SERVER_NAME_BDB = "wsamdb";
	
	private DBConnection conn_getPrepareDeviceInfo;
	
	private ResultSet rs_getPrepareDeviceInfo;
	
	private String REMOTE_XML_PATH="/home/hundsun/Backup/workspace/";
	
	//private String  url = "jdbc:oracle:thin:@10.3.8.147:1521:WSAMDB_147";
	
	private String dbuser = "hs_user";

	private String dbpsw = "hundsun";
	
	private JDialog dialog; 
	
	public PreparePanel(){
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "1.配置修改",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "2.增加交易日",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "3.修改档案路径", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		
		GroupLayout gl_panel = new GroupLayout(this);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_1,
												GroupLayout.PREFERRED_SIZE,//宽
												550, Short.MAX_VALUE)
										.addComponent(panel_2,
												GroupLayout.PREFERRED_SIZE,
												550, Short.MAX_VALUE)
										.addComponent(panel_3,
												GroupLayout.PREFERRED_SIZE,
												550, Short.MAX_VALUE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 60,  //高
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 60,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 60,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(46, Short.MAX_VALUE)));
		
		this.setLayout(gl_panel);
		
		panel_1.setLayout(null);
		panel_2.setLayout(null);
		panel_3.setLayout(null);
		
		final JButton btn_11 = new JButton("备份配置");
		btn_11.setBounds(200, 20, 110, 23);
		panel_1.add(btn_11);
		
		final JButton btn_21 = new JButton("增加交易日");
		btn_21.setBounds(200, 20, 110, 23);
		//btn_21.setEnabled(false);
		panel_2.add(btn_21);
		
		final JButton btn_31 = new JButton("修改档案路径");
		btn_31.setBounds(200, 20, 110, 23);
		panel_3.add(btn_31);
		
		btn_11.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getPrepareDeviceInfo();
				btn_11.setText("已完成");
				btn_11.setEnabled(false);
				btn_21.setEnabled(true);
			}

		});
		
		btn_21.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(dialog == null){
					System.out.println(CollectSysConfig.dburl);
					dialog = new JDialog(); 
					dialog.setSize(350, 70);
					dialog.setTitle("设置初始化日期");
					dialog.setLayout(null);
					final JTextField jtextfield = new JTextField();
					jtextfield.setBounds(150, 10, 80, 20);
					JLabel lb = new JLabel("需初始化到的日期\uFF1A");
					lb.setBounds(20, 10,150, 20);
					JButton bt = new JButton("确定");
					bt.setBounds(250, 10, 70, 20);
					dialog.setModal(true); //设置父窗口不可操作
					dialog.setAlwaysOnTop(true);
					dialog.add(lb);
					dialog.add(jtextfield);
					dialog.add(bt);
					dialog.setLocationRelativeTo(null);
					bt.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							try {
								if(addExchangeDate(CollectSysConfig.dburl, dbuser, dbpsw, jtextfield.getText())){
									dialog.setVisible(false);
									JOptionPane.showMessageDialog(null, "增加交易日期成功！");
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "增加交易日期失败！");
							}
						}
					});
				}dialog.setVisible(true);
			  //openExe();	
			}
			
			
		});
		
	}
	
	public static int runSSH(String host, String username, String password,

			String cmd) throws IOException {

				if (logger.isDebugEnabled()) {

					logger.debug("running SSH cmd [" + cmd + "]");

				}

				Connection conn = getOpenedConnection(host, username, password);

				Session sess = conn.openSession();

				sess.execCommand(cmd);

				InputStream stdout = new StreamGobbler(sess.getStdout());

				BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

				while (true) {
					
					String line = br.readLine();

					if (line == null)

						break;

					if (logger.isDebugEnabled()) {

						logger.debug(line);

					}

				}

				sess.close();

				conn.close();

				return sess.getExitStatus().intValue();

			}

	private static Connection getOpenedConnection(String host, String username,

			String password) throws IOException {

				if (logger.isDebugEnabled()) {

					logger.debug("connecting to " + host + " with user " + username

					+ " and pwd " + password);

				}

				Connection conn = new Connection(host);

				conn.connect(); // make sure the connection is opened

				boolean isAuthenticated = conn.authenticateWithPassword(username,

				password);

				if (isAuthenticated == false)

					throw new IOException("认证失败!");

				return conn;

			}
	/***
	 * 
	 * 获取测试环境需修改配置的机器信息
	 */
	public void getPrepareDeviceInfo(){
		
		
		//String command = "sed -i \"s/"+ORACLE_SERVER_NAME_DB+"/"+ORACLE_SERVER_NAME_BDB+"/g\" /home/hundsun/Backup/workspace/"+xml_name+"";
		
		//String command1 = "cp -a "+xml_name+" "+xml_name+".OK";
		
		String sql = "select xml_name,xml_ip ,username ,password from prepare_device ,update_device where  xml_ip in (select ip  from update_device where prepare_flag = 1) group by xml_name";
		
		System.out.println("sql="+sql);
		
		conn_getPrepareDeviceInfo = new DBConnection();
		
		rs_getPrepareDeviceInfo = conn_getPrepareDeviceInfo.executeQuery(sql);
		
		try {
			while(rs_getPrepareDeviceInfo.next()){
				
				System.out.println(rs_getPrepareDeviceInfo.getString("xml_ip")+"  "+rs_getPrepareDeviceInfo.getString("xml_name")+" "+rs_getPrepareDeviceInfo.getString("username")+"  "+rs_getPrepareDeviceInfo.getString("password"));
				System.out.println("cp -a "+REMOTE_XML_PATH+""+rs_getPrepareDeviceInfo.getString("xml_name")+" "+REMOTE_XML_PATH+""+rs_getPrepareDeviceInfo.getString("xml_name")+".OK");
				try {
					runSSH(rs_getPrepareDeviceInfo.getString("xml_ip"), rs_getPrepareDeviceInfo.getString("username"), rs_getPrepareDeviceInfo.getString("password"), "cp -a "+REMOTE_XML_PATH+""+rs_getPrepareDeviceInfo.getString("xml_name")+" "+REMOTE_XML_PATH+""+rs_getPrepareDeviceInfo.getString("xml_name")+".OK");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showConfirmDialog(null, "备份配置.OK文件失败！");
				}
				   try {
					runSSH(rs_getPrepareDeviceInfo.getString("xml_ip"), rs_getPrepareDeviceInfo.getString("username"), rs_getPrepareDeviceInfo.getString("password"), "sed -i \"s/"+ORACLE_SERVER_NAME_DB+"/"+ORACLE_SERVER_NAME_BDB+"/g\" "+REMOTE_XML_PATH+""+rs_getPrepareDeviceInfo.getString("xml_name")+"");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showConfirmDialog(null, "修改数据库连接名称失败！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showConfirmDialog(null, "获取需修改配置设备信息失败！");
		}
		
	}
	 /***
	  * 调用其他的可执行文件
	  */
	  public  void openExe() {
	  Runtime rn = Runtime.getRuntime();
	  Process p = null;
	  try {
		  p = rn.exec("\"D:/Program Files/PLSQL Developer/plsqldev.exe\"");
	  } catch (Exception e) {
		  System.out.println("Error exec!");
	  	}
	  }
	  
	  /***
	   * 增加交易日期
	   * @throws SQLException 
	   */

	  public Boolean addExchangeDate(String url,String db_user,String db_pwd,String Init_date) throws SQLException{
		  
		  try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			java.sql.Connection conn = DriverManager.getConnection(url, db_user, db_pwd);
			String sql = "insert into hs_user.exchangedate select finance_type,exchange_type,"+Init_date+",' ' from hs_user.exchangedate where init_date="+Init_date+"-1";
		    System.out.println("sql="+sql);
		    Statement stmt = conn.createStatement();
		    stmt.executeUpdate(sql);
		    return true;
		  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		  
	  }
}
