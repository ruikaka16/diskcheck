package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.wangrui.client.DTO.UpdateDevice;
import com.wangrui.server.DBConnection;
import com.wangrui.test.CommandRunner;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class UpdatePanel extends JPanel {

	public JLabel lblNewLabel, lblL, lblHsclient, zbLabel, linux386Lable;
	public JTextField textField, textField_1, textField_2, textField_3,textField_4;
	private JRadioButton rdbtnNewRadioButton_2,rdbtnNewRadioButton_3,rdbtnNewRadioButton_4;
	private JButton btnNewButton,btnNewButton_1,btnNewButton_2,btnNewButton_3,btnNewButton_4,btnNewButton_5,btnNewButton_6;
	private static final Logger logger = Logger.getLogger(UpdatePanel.class);
	private String comm_client = "", comm_xml = "", comm_appcom = "",
			comm_linux386 = "";
	DBConnection conn_query, conn_updateDeviceInfo, conn_updateZBDeviceInfo,
			conn_updateHsClientDeviceInfo, conn_updateZXDeviceInfo,
			conn_linux386, conn_insertUpdateFileInfo;
	ResultSet rs_getBackupDeviceInfo, rs_getUpdateDeviceInfo,
			rs_getUpdateZBDeviceInfo, rs_getHsClientUpdateDeviceInfo,
			rs_getUpdateZXDeviceInfo, rs_getLinux386;
	RandomAccessFile dos_bat_zh = null;
	RandomAccessFile dos_bat_rzrq = null;
	int selected_flag = 0;
	private String ZH_REMOTE_APPCOM_PATH = "/home/hundsun/Backup";
	private String ZH_REMOTE_XML_PATH = "/home/hundsun/Backup";
	private String ZH_REMOTE_CLIENT_PATH = "/home/hundsun/Backup/workspace/updatedir";
	private String ZH_REMOTE_Linux386_PATH = "/home/hundsun/Backup";
	private String RZRQ_REMOTE_APPCOM_PATH="/home/handsome/Backup";
	private String RZRQ_REMOTE_XML_PATH = "/home/handsome/Backup";
	private String RZRQ_REMOTE_Linux386_PATH="/home/handsome/Backup";
	private int exitValue = 0;
	static int count_device=0;
	private int updatedevice_count = 0;

	class T1 extends Thread {

		private UpdatePanel t;

		T1(UpdatePanel td) {
			t = td;
		}

		public void run() {

		}

	}

	public UpdatePanel(final int system_type) {

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "1.升级前备份",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "2.升级文件路径设置",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "3.升级", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));

		GroupLayout gl_panel = new GroupLayout(this);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_1,
												GroupLayout.PREFERRED_SIZE,
												600, Short.MAX_VALUE)
										.addComponent(panel_2,
												GroupLayout.PREFERRED_SIZE,
												600, Short.MAX_VALUE)
										.addComponent(panel_3,
												GroupLayout.PREFERRED_SIZE,
												600, Short.MAX_VALUE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 80,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 150,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 60,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(46, Short.MAX_VALUE)));

		this.setLayout(gl_panel);

		panel_1.setLayout(null);
		panel_2.setLayout(null);

		final JRadioButton rdbtnNewRadioButton = new JRadioButton("appcom文件");
		rdbtnNewRadioButton.setBounds(20, 20, 121, 23);
		panel_1.add(rdbtnNewRadioButton);

		final JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("中心配置文件");
		rdbtnNewRadioButton_1.setBounds(140, 20, 121, 23);
		panel_1.add(rdbtnNewRadioButton_1);

		btnNewButton = new JButton("开始备份");
		btnNewButton.setBounds(500, 20, 105, 23);
		panel_1.add(btnNewButton);

		btnNewButton_4 = new JButton("开始升级");
		btnNewButton_4.setEnabled(false);
		btnNewButton_4.setBounds(400, 18, 105, 23);
		panel_3.add(btnNewButton_4);
		if(system_type==0){
		
			rdbtnNewRadioButton_2 = new JRadioButton("HsClient文件");
			rdbtnNewRadioButton_2.setBounds(260, 20, 121, 23);
			panel_1.add(rdbtnNewRadioButton_2);
		
			rdbtnNewRadioButton_3 = new JRadioButton("灾备配置文件");
			rdbtnNewRadioButton_3.setBounds(375, 20, 121, 23);
			panel_1.add(rdbtnNewRadioButton_3);

			rdbtnNewRadioButton_4 = new JRadioButton("linux.i386");
			rdbtnNewRadioButton_4.setBounds(20, 50, 121, 23);
			panel_1.add(rdbtnNewRadioButton_4);
			
			btnNewButton_1 = new JButton("生成上传命令1");
			btnNewButton_1.setBounds(500, 18, 115, 23);
			btnNewButton_1.setEnabled(false);
			panel_2.add(btnNewButton_1);

			btnNewButton_2 = new JButton("生成上传命令2");
			btnNewButton_2.setBounds(500, 42, 115, 23);
			btnNewButton_2.setEnabled(false);
			panel_2.add(btnNewButton_2);

			btnNewButton_3 = new JButton("生成上传命令3");
			btnNewButton_3.setBounds(500, 66, 115, 23);
			btnNewButton_3.setEnabled(false);
			panel_2.add(btnNewButton_3);

			btnNewButton_5 = new JButton("生成上传命令4");
			btnNewButton_5.setBounds(500, 90, 115, 23);
			btnNewButton_5.setEnabled(false);
			panel_2.add(btnNewButton_5);

			btnNewButton_6 = new JButton("生成上传命令5");
			btnNewButton_6.setBounds(500, 114, 115, 23);
			btnNewButton_6.setEnabled(false);
			panel_2.add(btnNewButton_6);
			
			lblNewLabel = new JLabel("appcom\u6587\u4EF6\uFF1A");
			lblNewLabel.setBounds(30, 21, 80, 15);
			panel_2.add(lblNewLabel);

			textField = new JTextField();
			textField.setBounds(130, 18, 280, 21);
			textField.setVisible(false);
			panel_2.add(textField);
			textField.setColumns(10);

			lblL = new JLabel("中心配置文件\uFF1A");
			lblL.setBounds(30, 46, 84, 15);
			panel_2.add(lblL);

			textField_1 = new JTextField();
			textField_1.setBounds(130, 43, 280, 21);
			textField_1.setVisible(false);
			panel_2.add(textField_1);
			textField_1.setColumns(10);

			lblHsclient = new JLabel("HsClient\u6587\u4EF6\uFF1A");
			lblHsclient.setBounds(30, 71, 84, 15);
			panel_2.add(lblHsclient);

			textField_2 = new JTextField();
			textField_2.setBounds(130, 68, 280, 21);
			textField_2.setVisible(false);
			panel_2.add(textField_2);
			textField_2.setColumns(10);

			zbLabel = new JLabel("灾备配置文件\uFF1A");
			zbLabel.setBounds(30, 96, 84, 15);
			panel_2.add(zbLabel);
			//灾备textField_3
			textField_3 = new JTextField();
			textField_3.setBounds(130, 93, 280, 21);
			textField_3.setVisible(false);
			panel_2.add(textField_3);
			textField_3.setColumns(10);
			//textField_3.setText("what");

			linux386Lable = new JLabel("linux.i386\uFF1A");
			linux386Lable.setBounds(30, 121, 84, 15);
			panel_2.add(linux386Lable);
			//灾备linux.i386
			textField_4 = new JTextField();
			textField_4.setBounds(130, 118, 280, 21);
			textField_4.setVisible(false);
			panel_2.add(textField_4);
			textField_4.setColumns(10);
			//textField_4.setText("what");

			// 单选按钮设置
			rdbtnNewRadioButton.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton.isSelected()) {

						textField.setVisible(true);
						selected_flag = selected_flag + 1;
						// System.out.println("selected!  and select_flag = "+selected_flag);
						// btnNewButton_1.setEnabled(true);
						comm_appcom = "cp -a appcom Backup/" + getCurrentDay()
								+ ";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						comm_appcom = "";
						textField.setVisible(false);
						btnNewButton_1.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_1.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_1.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_1.setVisible(true);
						// btnNewButton_2.setEnabled(true);
						comm_xml = "cp -a workspace/*.xml Backup/"
								+ getCurrentDay() + ";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						comm_xml = "";
						textField_1.setVisible(false);
						btnNewButton_2.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_2.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_2.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_2.setVisible(true);
						// btnNewButton_3.setEnabled(true);
						comm_client = "cp -a workspace/updatedir/HsClient Backup/"
								+ getCurrentDay() + ";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						comm_client = "";
						textField_2.setVisible(false);
						btnNewButton_3.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_3.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_3.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_3.setVisible(true);
						// btnNewButton_3.setEnabled(true);
						// comm_xml =
						// "cp -a workspace/*.xml Backup/"+getCurrentDay()+";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						// comm_xml="";
						textField_3.setVisible(false);
						btnNewButton_5.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_4.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_4.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_4.setVisible(true);
						// btnNewButton_3.setEnabled(true);
						// comm_xml =
						// "cp -a workspace/*.xml Backup/"+getCurrentDay()+";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						// comm_xml="";
						textField_4.setVisible(false);
						btnNewButton_6.setEnabled(false);
					}
				}
			});
			
			// 文件路径栏设置
			textField.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField.setText(f.getPath());
						btnNewButton_1.setEnabled(true);
					}
				}
			});

			textField_1.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_1.setText(f.getPath());
						btnNewButton_2.setEnabled(true);
					}
				}
			});

			textField_2.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_2.setText(f.getPath());
						btnNewButton_3.setEnabled(true);
					}

				}
			});

			textField_3.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_3.setText(f.getPath());
						btnNewButton_5.setEnabled(true);
					}

				}
			});

			textField_4.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_4.setText(f.getPath());
						btnNewButton_6.setEnabled(true);
					}

				}
			});
			
			btnNewButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					if (selected_flag == 0) {
						JOptionPane.showMessageDialog(null, "请选择需要备份的项目");
					} else {
						Object[] options = { "确定", "取消" };
						int n = JOptionPane.showOptionDialog(null,
								"请确定需备份文件选择是否正确？", "提示", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, // do not use a
																	// custom Icon
								options, // the titles of buttons
								options[1]);
						if (n == 0) {
							getBackupDeviceInfo(comm_appcom, comm_xml, comm_client,0);
							btnNewButton.setText("已完成");
							btnNewButton.setEnabled(false);
							btnNewButton_4.setEnabled(true);
						} else {
							return;
						}
					}

				}
			});

			btnNewButton_1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_1.setText("已完成");
					btnNewButton_1.setEnabled(false);
					//System.out.println("获取文件路径" + textField.getText());
					if(system_type == 0){
					getUpdateDeviceInfo(textField.getText().toString(),ZH_REMOTE_APPCOM_PATH,0);
					}else if(system_type == 1){
						getUpdateDeviceInfo(textField.getText().toString(),RZRQ_REMOTE_APPCOM_PATH,1);
					}
					textField.setEnabled(false);
					// System.out.println(textField.getText());
					//System.out.println(str_replace("\\", "\\\\",textField.getText()));
					//System.out.println(countFile(str_replace("\\", "\\\\",textField.getText())));
					insertUpdateFileInfo(textField.getText(),0);

				}
			});

			btnNewButton_2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_2.setText("已完成");
					btnNewButton_2.setEnabled(false);
					//System.out.println("获取文件路径" + textField_1.getText());
					if(system_type == 0){
					getZXUpdateDeviceInfo(textField_1.getText().toString(),ZH_REMOTE_XML_PATH,0);
					}else if(system_type ==1){
						getZXUpdateDeviceInfo(textField_1.getText().toString(),RZRQ_REMOTE_XML_PATH,1);
					}
					textField_1.setEnabled(false);
					insertUpdateFileInfo(textField_1.getText(),0);

				}
			});

			btnNewButton_3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_3.setText("已完成");
					btnNewButton_3.setEnabled(false);
					//System.out.println("获取文件路径" + textField_2.getText());
					getHsClientUpdateDeviceInfo(textField_2.getText().toString(),
							ZH_REMOTE_CLIENT_PATH,0);
					textField_2.setEnabled(false);
					insertUpdateFileInfo(textField_2.getText(),0);
				}
			});

			btnNewButton_5.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_5.setText("已完成");
					btnNewButton_5.setEnabled(false);
					System.out.println("获取文件路径" + textField_3.getText());
					getZBUpdateDeviceInfo(textField_3.getText().toString(),
							ZH_REMOTE_XML_PATH,0);
					textField_3.setEnabled(false);
					insertUpdateFileInfo(textField_3.getText(),0);
				}
			});

			btnNewButton_6.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_6.setText("已完成");
					btnNewButton_6.setEnabled(false);
					//System.out.println("获取文件路径" + textField_4.getText());
					if(system_type==0){
					getlinux386(textField_4.getText().toString(),ZH_REMOTE_Linux386_PATH,0);
					}else if(system_type == 1){
						getlinux386(textField_4.getText().toString(),RZRQ_REMOTE_Linux386_PATH,1);	
					}
					textField_4.setEnabled(false);
					insertUpdateFileInfo(textField_4.getText(),0);
				}
			});
			
			btnNewButton_4.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(rdbtnNewRadioButton.isSelected()||rdbtnNewRadioButton_1.isSelected()||rdbtnNewRadioButton_4.isSelected()){
						checkEndFlag("select ip from update_device where system_type=0", 0);
						updatedevice_count=getDeviceCount("select count(*) as count_device from update_device where system_type=0", 0);
						System.out.println("appcom被选择,或者配置被选择，或者linux.i386被选择是需升级的数量="+updatedevice_count);
					}else if(!rdbtnNewRadioButton.isSelected()&&!rdbtnNewRadioButton_1.isSelected()&&!rdbtnNewRadioButton_4.isSelected()&&!rdbtnNewRadioButton_3.isSelected()&&rdbtnNewRadioButton_2.isSelected()){
						System.out.println("只用HsClient");
						checkEndFlag("select ip from update_device where updatedir_flag = 1 and system_type=0", 0);
						updatedevice_count=getDeviceCount("select count(*) as count_device from update_device where updatedir_flag = 1 and system_type=0", 0);
						System.out.println("只用HsClient是需升级的数量="+updatedevice_count);
					}
					Object[] options = { "确定", "取消" };
					int n = JOptionPane.showOptionDialog(null,
							"升级前，请先检查文件备份和生成导入命令文件是否正确！是否确定升级？", "提示",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, // do not use a
																// custom Icon
							options, // the titles of buttons
							options[1]);
					if (n == 0) {
						try {
							excuteUploadComm(0);

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						return;
					}
				}
			});
			
		}//如果是融资融券系统
		else if(system_type==1){
			
			rdbtnNewRadioButton_3 = new JRadioButton("灾备配置文件");
			rdbtnNewRadioButton_3.setBounds(260, 20, 121, 23);
			panel_1.add(rdbtnNewRadioButton_3);

			rdbtnNewRadioButton_4 = new JRadioButton("linux.i386");
			rdbtnNewRadioButton_4.setBounds(375, 20, 121, 23);
			panel_1.add(rdbtnNewRadioButton_4);
			
			btnNewButton_1 = new JButton("生成上传命令");
			btnNewButton_1.setBounds(500, 18, 105, 23);
			btnNewButton_1.setEnabled(false);
			panel_2.add(btnNewButton_1);

			btnNewButton_2 = new JButton("生成上传命令");
			btnNewButton_2.setBounds(500, 42, 105, 23);
			btnNewButton_2.setEnabled(false);
			panel_2.add(btnNewButton_2);

			btnNewButton_5 = new JButton("生成上传命令");
			btnNewButton_5.setBounds(500, 66, 105, 23);
			btnNewButton_5.setEnabled(false);
			panel_2.add(btnNewButton_5);

			btnNewButton_6 = new JButton("生成上传命令");
			btnNewButton_6.setBounds(500, 90, 105, 23);
			btnNewButton_6.setEnabled(false);
			panel_2.add(btnNewButton_6);
			
			lblNewLabel = new JLabel("appcom\u6587\u4EF6\uFF1A");
			lblNewLabel.setBounds(30, 21, 80, 15);
			panel_2.add(lblNewLabel);

			textField = new JTextField();
			textField.setBounds(130, 18, 280, 21);
			textField.setVisible(false);
			panel_2.add(textField);
			textField.setColumns(10);

			lblL = new JLabel("中心配置文件\uFF1A");
			lblL.setBounds(30, 46, 84, 15);
			panel_2.add(lblL);

			textField_1 = new JTextField();
			textField_1.setBounds(130, 43, 280, 21);
			textField_1.setVisible(false);
			panel_2.add(textField_1);
			textField_1.setColumns(10);

			zbLabel = new JLabel("灾备配置文件\uFF1A");
			zbLabel.setBounds(30, 71, 84, 15);
			panel_2.add(zbLabel);

			textField_3 = new JTextField();
			textField_3.setBounds(130, 68, 280, 21);
			textField_3.setVisible(false);
			panel_2.add(textField_3);
			textField_3.setColumns(10);

			linux386Lable = new JLabel("linux.i386\uFF1A");
			linux386Lable.setBounds(30, 96, 84, 15);
			panel_2.add(linux386Lable);

			textField_4 = new JTextField();
			textField_4.setBounds(130, 93, 280, 21);
			textField_4.setVisible(false);
			panel_2.add(textField_4);
			textField_4.setColumns(10);

			rdbtnNewRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton.isSelected()) {

						textField.setVisible(true);
						selected_flag = selected_flag + 1;
						// System.out.println("selected!  and select_flag = "+selected_flag);
						// btnNewButton_1.setEnabled(true);
						comm_appcom = "cp -a appcom Backup/" + getCurrentDay()
								+ ";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						comm_appcom = "";
						textField.setVisible(false);
						btnNewButton_1.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_1.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_1.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_1.setVisible(true);
						// btnNewButton_2.setEnabled(true);
						comm_xml = "cp -a workspace/*.xml Backup/"
								+ getCurrentDay() + ";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						comm_xml = "";
						textField_1.setVisible(false);
						btnNewButton_2.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_3.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_3.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_3.setVisible(true);
						// btnNewButton_3.setEnabled(true);
						// comm_xml =
						// "cp -a workspace/*.xml Backup/"+getCurrentDay()+";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						// comm_xml="";
						textField_3.setVisible(false);
						btnNewButton_5.setEnabled(false);
					}
				}
			});

			rdbtnNewRadioButton_4.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if (rdbtnNewRadioButton_4.isSelected()) {
						selected_flag = selected_flag + 1;
						// System.out.println("selected! and select_flag = "+selected_flag);
						textField_4.setVisible(true);
						// btnNewButton_3.setEnabled(true);
						// comm_xml =
						// "cp -a workspace/*.xml Backup/"+getCurrentDay()+";";
					} else {
						selected_flag = selected_flag - 1;
						// System.out.println("unselected!  and select_flag = "+selected_flag);
						// comm_xml="";
						textField_4.setVisible(false);
						btnNewButton_6.setEnabled(false);
					}
				}
			});
			
			// 文件路径栏设置
			textField.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField.setText(f.getPath());
						btnNewButton_1.setEnabled(true);
					}
				}
			});

			textField_1.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_1.setText(f.getPath());
						btnNewButton_2.setEnabled(true);
					}
				}
			});
			textField_3.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_3.setText(f.getPath());
						btnNewButton_5.setEnabled(true);
					}

				}
			});

			textField_4.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

					JFileChooser jf = new JFileChooser(CollectSysConfig.updatePath);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只选择文件夹
					int flag = jf.showOpenDialog(null);
					File f = null;
					if (flag == JFileChooser.APPROVE_OPTION) {
						f = jf.getSelectedFile();
						textField_4.setText(f.getPath());
						btnNewButton_6.setEnabled(true);
					}

				}
			});
			
			btnNewButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					if (selected_flag == 0) {
						JOptionPane.showMessageDialog(null, "请选择需要备份的项目");
					} else {
						Object[] options = { "确定", "取消" };
						int n = JOptionPane.showOptionDialog(null,
								"请确定需备份文件选择是否正确？", "提示", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, // do not use a
																	// custom Icon
								options, // the titles of buttons
								options[1]);
						if (n == 0) {
							getBackupDeviceInfo(comm_appcom, comm_xml, comm_client,1);
							btnNewButton.setText("已完成");
							btnNewButton.setEnabled(false);
							btnNewButton_4.setEnabled(true);
						} else {
							return;
						}
					}

				}
			});

			btnNewButton_1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_1.setText("已完成");
					btnNewButton_1.setEnabled(false);
					//System.out.println("获取文件路径" + textField.getText());
					if(system_type == 0){
					getUpdateDeviceInfo(textField.getText().toString(),ZH_REMOTE_APPCOM_PATH,0);
					}else if(system_type == 1){
						getUpdateDeviceInfo(textField.getText().toString(),RZRQ_REMOTE_APPCOM_PATH,1);
					}
					textField.setEnabled(false);
					// System.out.println(textField.getText());
					//System.out.println(str_replace("\\", "\\\\",textField.getText()));
					//System.out.println(countFile(str_replace("\\", "\\\\",textField.getText())));
					insertUpdateFileInfo(textField.getText(),1);

				}
			});

			btnNewButton_2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_2.setText("已完成");
					btnNewButton_2.setEnabled(false);
					//System.out.println("获取文件路径" + textField_1.getText());
					if(system_type == 0){
					getZXUpdateDeviceInfo(textField_1.getText().toString(),ZH_REMOTE_XML_PATH,0);
					}else if(system_type ==1){
						getZXUpdateDeviceInfo(textField_1.getText().toString(),RZRQ_REMOTE_XML_PATH,1);
					}
					textField_1.setEnabled(false);
					insertUpdateFileInfo(textField_1.getText(),1);

				}
			});

			btnNewButton_5.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_5.setText("已完成");
					btnNewButton_5.setEnabled(false);
					//System.out.println("获取文件路径" + textField_3.getText());
					getZBUpdateDeviceInfo(textField_3.getText().toString(),
							ZH_REMOTE_XML_PATH,1);
					textField_3.setEnabled(false);
					insertUpdateFileInfo(textField_3.getText(),1);
				}
			});

			btnNewButton_6.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnNewButton_6.setText("已完成");
					btnNewButton_6.setEnabled(false);
					//System.out.println("获取文件路径" + textField_4.getText());
					if(system_type==0){
					getlinux386(textField_4.getText().toString(),ZH_REMOTE_Linux386_PATH,0);
					}else if(system_type == 1){
						getlinux386(textField_4.getText().toString(),RZRQ_REMOTE_Linux386_PATH,1);	
					}
					textField_4.setEnabled(false);
					insertUpdateFileInfo(textField_4.getText(),1);
				}
			});
			
			btnNewButton_4.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					checkEndFlag("select ip from update_device where system_type=1", 1);
					updatedevice_count=getDeviceCount("select count(*) as count_device from update_device where system_type=1", 1);
					Object[] options = { "确定", "取消" };
					int n = JOptionPane.showOptionDialog(null,
							"升级前，请先检查文件备份和生成导入命令文件是否正确！是否确定升级？", "提示",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, // do not use a
																// custom Icon
							options, // the titles of buttons
							options[1]);
					if (n == 0) {
						try {
							excuteUploadComm(1);

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						return;
					}
				}
			});

		}
	}

	/**
	 * 写入备份命令
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
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

			throw new IOException("认证失败.");

		return conn;

	}

	/***
	 * 获取需要备份的设备信息并执行备份命令
	 */
	public List getBackupDeviceInfo(String comm_appcom, String comm_xml,String comm_client,int system_type) {
		// String COMMAND =
		// "mkdir Backup/"+getCurrentDay()+"; cp -a appcom Backup/"+getCurrentDay()+"; cp -a workspace/*.xml Backup/"+getCurrentDay()+";cp -a workspace/updatedir/HsClient Backup/"+getCurrentDay()+";";
		String COMMAND = "mkdir Backup/" + getCurrentDay() + ";" + comm_appcom
				+ comm_xml + comm_client;
		System.out.println("备份命令：" + COMMAND);
		conn_query = new DBConnection();
		String sql_zh = "select ip,username,password,backup_flag,updatedir_flag from update_device where backup_flag=1 and system_type = 0";
		String sql_rzrq = "select ip,username,password,backup_flag,updatedir_flag from update_device where backup_flag=1 and system_type = 1";
		if(system_type==0){
		rs_getBackupDeviceInfo = conn_query.executeQuery(sql_zh);
		}else if(system_type==1){
			rs_getBackupDeviceInfo = conn_query.executeQuery(sql_rzrq);
		}
		List updatedeviceInfoList = new ArrayList();// 定义一个List
		UpdateDevice updateDeviceBean = new UpdateDevice();// // 实例化一个Bean
		try {
			while (rs_getBackupDeviceInfo.next()) {
				System.out.println("备份设备：" + rs_getBackupDeviceInfo.getString("ip"));

				try {
					runSSH(rs_getBackupDeviceInfo.getString("ip"),
							rs_getBackupDeviceInfo.getString("username"),
							rs_getBackupDeviceInfo.getString("password"),
							COMMAND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							rs_getBackupDeviceInfo.getString("ip") + "备份失败！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "获取升级设备信息失败！");
		}
		return null;

	}

	/***
	 * 获取需要升级的设备信息并生成上传命令
	 */
	public List getUpdateDeviceInfo(String localpath, String remotepath,int system_type) {

		// String COMMAND =
		// "mkdir Backup/"+getCurrentDay()+"; cp -a appcom Backup/"+getCurrentDay()+"; cp -a workspace/*.xml Backup/"+getCurrentDay()+";cp -a workspace/updatedir/HsClient Backup/"+getCurrentDay()+";";
		// String COMMAND =
		// "mkdir Backup/"+getCurrentDay()+";"+comm_appcom+comm_xml+comm_client;
		// System.out.println("执行备份命令="+COMMAND);
		conn_updateDeviceInfo = new DBConnection();
		String sql_zh = "select ip,username,password,backup_flag,updatedir_flag from update_device where system_type = 0";
		String sql_rzrq = "select ip,username,password,backup_flag,updatedir_flag from update_device where system_type = 1";
		if(system_type==0){
		rs_getUpdateDeviceInfo = conn_updateDeviceInfo.executeQuery(sql_zh);
		}else if(system_type==1){
			rs_getUpdateDeviceInfo = conn_updateDeviceInfo.executeQuery(sql_rzrq);
		}
		List updatedeviceInfoList = new ArrayList();// 定义一个List
		UpdateDevice updateDeviceBean = new UpdateDevice();// // 实例化一个Bean
		try {
			while (rs_getUpdateDeviceInfo.next()) {
				updateDeviceBean.setIp(rs_getUpdateDeviceInfo.getString("ip"));
				updateDeviceBean.setUsername(rs_getUpdateDeviceInfo
						.getString("username"));
				updateDeviceBean.setPassword(rs_getUpdateDeviceInfo
						.getString("password"));
				updateDeviceBean.setBackup_flag(rs_getUpdateDeviceInfo
						.getInt("backup_flag"));
				System.out.println("升级设备：" + updateDeviceBean.getIp());

				try {
					// runSSH(rs_getBackupDeviceInfo.getString("ip"),
					// rs_getBackupDeviceInfo.getString("username"),
					// rs_getBackupDeviceInfo.getString("password"), COMMAND);
					if(system_type==0){
					createUploadComm(rs_getUpdateDeviceInfo.getString("ip"),
							rs_getUpdateDeviceInfo.getString("username"),
							rs_getUpdateDeviceInfo.getString("password"),
							localpath, remotepath,0);
					}else if(system_type==1){
						createUploadComm(rs_getUpdateDeviceInfo.getString("ip"),
								rs_getUpdateDeviceInfo.getString("username"),
								rs_getUpdateDeviceInfo.getString("password"),
								localpath, remotepath,1);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
							rs_getBackupDeviceInfo.getString("ip")
									+ "生成上传命令失败！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "获取升级设备信息失败！");
		}
		return null;

	}

	/***
	 * 获取当期日期，一遍command中使用
	 * 
	 * @return 当前日期yyyyMMDD
	 */
	public static String getCurrentDay() {

		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String c = sdf.format(current);
		return c;
	}

	/****
	 * 生成上传命令
	 * @throws IOException
	 */
	public void createUploadComm(String ip, String username, String password,
			String localpath, String remotepath,int system_type) throws IOException {

		//System.out.println("-------进入生成导入命令阶段-------");
		
			if(system_type==0){
				try {
			dos_bat_zh = new RandomAccessFile(CollectSysConfig.filePathresult
					+ "/log/" + getCurrentDay() + "/" + "zh_batCommd" + ".bat",
					"rw");
			dos_bat_zh.seek(dos_bat_zh.length());

			String upload_comm = "pscp -l " + username + " -pw " + password
					+ " -p -r " + localpath + " " + username + "@" + ip + ":"
					+ remotepath + " |mtee /c /+ "
					+ CollectSysConfig.filePathresult + "\\log\\"
					+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
					+ "_log.txt";
			String endflag = "@echo end |mtee /c /+ "
					+ CollectSysConfig.filePathresult + "\\log\\"
					+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
					+ "_log.txt";
			System.out.println("升级设备上传命令:\r\n" + upload_comm+"\r\n"+endflag);
			//System.out.println(ip.replaceAll("\\.", "_"));
			dos_bat_zh.writeBytes(new String(upload_comm.getBytes(), "iso8859-1")
					+ "\r\n");
			//dos_bat_zh.writeBytes(new String(endflag.getBytes(), "iso8859-1")
			//		+ "\r\n");
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "写入导入命令失败！");
				e.printStackTrace();
			} finally { // finally中将reader对象关闭　　　　　

				if (dos_bat_zh != null) {
					try {
						dos_bat_zh.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			}
			else if(system_type==1){
				try{
				dos_bat_rzrq = new RandomAccessFile(CollectSysConfig.filePathresult
						+ "/log/" + getCurrentDay() + "/" + "rzrq_batCommd" + ".bat",
						"rw");
				dos_bat_rzrq.seek(dos_bat_rzrq.length());

				String upload_comm = "pscp -l " + username + " -pw " + password
						+ " -p -r " + localpath + " " + username + "@" + ip + ":"
						+ remotepath + " |mtee /c /+ "
						+ CollectSysConfig.filePathresult + "\\log\\"
						+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
						+ "_log.txt";
				String endflag = "@echo end |mtee /c /+ "
						+ CollectSysConfig.filePathresult + "\\log\\"
						+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
						+ "_log.txt";
				System.out.println("打印融资融券bat命令:\r\n" +endflag);
				//System.out.println(ip.replaceAll("\\.", "_"));
				dos_bat_rzrq.writeBytes(new String(upload_comm.getBytes(), "iso8859-1")
						+ "\r\n");
				//dos_bat_rzrq.writeBytes(new String(endflag.getBytes(), "iso8859-1")
				//		+ "\r\n");
			}
				catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "写入导入命令失败！");
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (dos_bat_rzrq != null) {
				try {
					dos_bat_rzrq.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }

		}
	}

	/****
	 * 
	 * 执行上传批处理命令
	 * 
	 * @throws IOException
	 * 
	 */

	public void excuteUploadComm(final int system_type) throws IOException {

		System.out.println("升级设备数量："+updatedevice_count);
		File batFile = new File(CollectSysConfig.filePathresult + "/log/"
				+ getCurrentDay()); // bat的目录
		final File[] batFiles = batFile.listFiles();
		if (batFiles != null) {
					if(system_type==0){
						Runtime rn = Runtime.getRuntime();
						Process p = null;

						try {
							p = rn.exec("cmd.exe /c start " + CollectSysConfig.filePathresult + "/log/"
									+ getCurrentDay()+"//zh_batCommd.bat");
							//System.out.println(batFiles[i].getPath());
							try {
								exitValue = p.waitFor();

							} catch (Exception e) {
								e.printStackTrace();
							}
							if (p.exitValue() != 0) {
								p.destroy();
							}

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if(system_type==1){
						Runtime rn = Runtime.getRuntime();
						Process p = null;

						try {
							p = rn.exec("cmd.exe /c start " + CollectSysConfig.filePathresult + "/log/"
									+ getCurrentDay()+"//rzrq_batCommd.bat");
							//System.out.println(batFiles[i].getPath());
							try {
								exitValue = p.waitFor();

							} catch (Exception e) {
								e.printStackTrace();
							}
							if (p.exitValue() != 0) {
								p.destroy();
							}

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				

					// 设置等待一下，不让立即执行，以免找不到log文件
					try {
						Thread.sleep(1000);
						//System.out.println("等待一下！");
					} catch (InterruptedException e2) {

						e2.printStackTrace();
					}
					//设置计时器，遍历日志，根据上传完成的数量来判断批处理是否执行完毕，完毕后计时器cancel
					final Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							int j = 0;
							long length1 = 0;
							boolean oneZero = true; // 判断日志是否为空的标志
							boolean oneComplete = true; // 判断日志导入完成的标志，true代表完成
							File file = new File(CollectSysConfig.filePathresult + "//log//"+ getCurrentDay() + "//");
							File[] files = file.listFiles();
							if (files != null && files.length != 0) {
								for (int i = 0; i < files.length; i++) {
									if (files[i].getName().endsWith(".txt")) {
										length1 = files[i].length();
										length1 = length1 % 1024 == 0 ? length1 / 1024
												: length1 / 1024 + 1;
										//System.out.println("----------------------------");
										//System.out.println("文件大小：" + length1+ "K");
										//System.out.println(files[i].getPath());
										if (length1 == 0) { // 日志文件为空时，置标志oneZero为false
											oneZero = false;
										} else {
											// 读日志文件，判断时候正常导入完成，完成返回false，未完成返回true
											if (readFileByChars(files[i]
													.getPath()) == true) {
												oneComplete = false; // 日志检查未完成，置标志oneComplete为false
											} else {
												oneComplete = true;
												j++; // 完成后完成个数J参数加1
											}
										}
									}
								}
								if(system_type==0){
								if (oneZero && oneComplete && j == updatedevice_count) {
									timer.cancel();
									btnNewButton_4.setEnabled(false);
									btnNewButton_4.setText("升级完成");
									JOptionPane.showMessageDialog(null,"升级成功！");
									Object[] options1 = {"确定","取消"};
									int m = JOptionPane.showOptionDialog(null,
										    "是否需要打印升级报告？",
										    "提示",
										    JOptionPane.YES_NO_OPTION,
										    JOptionPane.QUESTION_MESSAGE,
										    null,     //do not use a custom Icon
										    options1,  //the titles of buttons
										    options1[1]);
									if(m == 0){
									
										UpdateReport updateReport = new UpdateReport(0);
										updateReport.setVisible(true);
									}else{
										return;
									}
								}
								}else if(system_type==1){
									if (oneZero && oneComplete && j == updatedevice_count) {
										timer.cancel();
										btnNewButton_4.setEnabled(false);
										btnNewButton_4.setText("升级完成");
										JOptionPane.showMessageDialog(null,"升级成功！");
										Object[] options1 = {"确定","取消"};
										int m = JOptionPane.showOptionDialog(null,
											    "是否需要打印升级报告？",
											    "提示",
											    JOptionPane.YES_NO_OPTION,
											    JOptionPane.QUESTION_MESSAGE,
											    null,     //do not use a custom Icon
											    options1,  //the titles of buttons
											    options1[1]);
										if(m == 0){
											
											UpdateReport updateReport = new UpdateReport(1);
											updateReport.setVisible(true);
										}else{
											return;
										}
									}
								}

							}
						}

					}, 0, 2000);

		} else {
			JOptionPane.showMessageDialog(null, "没有可执行的上传批处理命令！");
		}
	}

	public void getZBUpdateDeviceInfo(String localpath, String remotepath,int system_type) {

		conn_updateZBDeviceInfo = new DBConnection();
		String sql_zh = "select ip,username,password,backup_flag,updatedir_flag from update_device where bdb_flag = 1 and system_type = 0";
		String sql_rzrq = "select ip,username,password,backup_flag,updatedir_flag from update_device where bdb_flag = 1 and system_type = 1";
		if(system_type==0){
		rs_getUpdateZBDeviceInfo = conn_updateZBDeviceInfo.executeQuery(sql_zh);
		}else if(system_type==1){
			rs_getUpdateZBDeviceInfo = conn_updateZBDeviceInfo.executeQuery(sql_rzrq);
		}
		try {
			while (rs_getUpdateZBDeviceInfo.next()) {

				try {
					if(system_type==0){
					createUploadComm(rs_getUpdateZBDeviceInfo.getString("ip"),
							rs_getUpdateZBDeviceInfo.getString("username"),
							rs_getUpdateZBDeviceInfo.getString("password"),
							localpath, remotepath,0);
					}else if(system_type==1){
						createUploadComm(rs_getUpdateZBDeviceInfo.getString("ip"),
								rs_getUpdateZBDeviceInfo.getString("username"),
								rs_getUpdateZBDeviceInfo.getString("password"),
								localpath, remotepath,1);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs_getUpdateZBDeviceInfo.close();
				// conn_updateZBDeviceInfo.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getZXUpdateDeviceInfo(String localpath, String remotepath,int system_type) {

		conn_updateZXDeviceInfo = new DBConnection();
		String sql_zh = "select ip,username,password,backup_flag,updatedir_flag from update_device where bdb_flag = 0 and system_type = 0";
		String sql_rzrq = "select ip,username,password,backup_flag,updatedir_flag from update_device where bdb_flag = 0 and system_type = 1";
		if(system_type == 0){
		rs_getUpdateZXDeviceInfo = conn_updateZXDeviceInfo.executeQuery(sql_zh);
		}else if(system_type == 1){
			rs_getUpdateZXDeviceInfo = conn_updateZXDeviceInfo.executeQuery(sql_rzrq);
		}
		try {
			while (rs_getUpdateZXDeviceInfo.next()) {

				try {
					if(system_type==0){
					createUploadComm(rs_getUpdateZXDeviceInfo.getString("ip"),
							rs_getUpdateZXDeviceInfo.getString("username"),
							rs_getUpdateZXDeviceInfo.getString("password"),
							localpath, remotepath,0);
					}else if(system_type==1){
						createUploadComm(rs_getUpdateZXDeviceInfo.getString("ip"),
								rs_getUpdateZXDeviceInfo.getString("username"),
								rs_getUpdateZXDeviceInfo.getString("password"),
								localpath, remotepath,1);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs_getUpdateZXDeviceInfo.close();
				// conn_updateZXDeviceInfo.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getHsClientUpdateDeviceInfo(String localpath, String remotepath,int system_type) {

		conn_updateHsClientDeviceInfo = new DBConnection();
		String sql = "select ip,username,password,backup_flag,updatedir_flag from update_device where updatedir_flag = 1 and system_type = 0";
		rs_getHsClientUpdateDeviceInfo = conn_updateHsClientDeviceInfo
				.executeQuery(sql);
		try {
			while (rs_getHsClientUpdateDeviceInfo.next()) {

				try {
					if(system_type==0){
					createUploadComm(
							rs_getHsClientUpdateDeviceInfo.getString("ip"),
							rs_getHsClientUpdateDeviceInfo
									.getString("username"),
							rs_getHsClientUpdateDeviceInfo
									.getString("password"), localpath,
							remotepath,0);
					}else 
						return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs_getHsClientUpdateDeviceInfo.close();
				// conn_updateHsClientDeviceInfo.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getlinux386(String localpath, String remotepath,int system_type) {

		conn_linux386 = new DBConnection();
		String sql_zh = "select ip,username,password,backup_flag,updatedir_flag from update_device where system_type = 0";
		String sql_rzrq = "select ip,username,password,backup_flag,updatedir_flag from update_device where system_type = 1";
		if(system_type==0){
		rs_getLinux386 = conn_linux386.executeQuery(sql_zh);
		}else if(system_type == 1){
			rs_getLinux386 = conn_linux386.executeQuery(sql_rzrq);
		}
		try {
			while (rs_getLinux386.next()) {

				try {
					if(system_type==0){
					createUploadComm(rs_getLinux386.getString("ip"),
							rs_getLinux386.getString("username"),
							rs_getLinux386.getString("password"), localpath,
							remotepath,0);
					}else if(system_type==1){
						createUploadComm(rs_getLinux386.getString("ip"),
								rs_getLinux386.getString("username"),
								rs_getLinux386.getString("password"), localpath,
								remotepath,1);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// rs_getHsClientUpdateDeviceInfo.close();
				// conn_updateHsClientDeviceInfo.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/****
	 * 获得文件数量方法
	 * @param path  文件路径      
	 * @return 文件个数
	 */
	public static int countFile(String path) {
		int sum = 0;
		try {
			File file = new File(path);
			File[] list = file.listFiles();
			for (int i = 0; i < list.length; i++) {
				if (list[i].isFile()) {
					sum++;
				} else {
					sum += countFile(list[i].getPath());
				}
			}
		} catch (NullPointerException ne) {
			System.out.println("找不到指定路径！");
		}
		return sum;
	}

	/***
	 * 字符串替换方法
	 * @param from原字符
	 * @param to替换字符
	 * @param source字符串源
	 * @return 替换后的字符串
	 */
	public String str_replace(String from, String to, String source) {
		StringBuffer bf = new StringBuffer("");
		StringTokenizer st = new StringTokenizer(source, from, true);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (tmp.equals(from)) {
				bf.append(to);
			} else {
				bf.append(tmp);
			}
		}
		return bf.toString();
	}

	/*****
	 * 将升级文件的信息写入数据库中，为报表使用做准备
	 * 
	 * @param filePath
	 */
	public void insertUpdateFileInfo(String filePath,int system_type) {

		conn_insertUpdateFileInfo = new DBConnection();
		File file = new File(filePath);
		File[] lf = file.listFiles();
		for (int i = 0; i < lf.length; i++) {
			java.util.Date date = new java.util.Date(lf[i].lastModified());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String lastModifiedTime = df.format(date);
			if (lf[i].getName().endsWith(".so")) {
				try{
					String sql_zh = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',0,'so动态库',0)";
					String sql_rzrq = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
						+ getCurrentDay()
						+ "','"
						+ lf[i].getName()
						+ "','" + lastModifiedTime + "',0,'so动态库',1)";
					System.out.println(sql_rzrq);
					if(system_type==0){
						conn_insertUpdateFileInfo.executeUpdate(sql_zh);
					}else if(system_type==1){
						conn_insertUpdateFileInfo.executeUpdate(sql_rzrq);
					}
				}catch(Exception e){
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"shibai");
					System.out.println("so文件插入update_file表失败！");
				}
			} else if (lf[i].getName().endsWith(".dll")) {
				try{
					String sql_zh = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',1,'dll动态库',0)";
					String sql_rzrq = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',1,'dll动态库',1)";
					if(system_type==0){
						conn_insertUpdateFileInfo.executeUpdate(sql_zh);
					}else if(system_type==1){
						conn_insertUpdateFileInfo.executeUpdate(sql_rzrq);
					}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("dll文件插入update_file表失败！");
				}
			} else if (lf[i].getName().endsWith(".xml")) {
				try{
					String sql_zh = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',2,'配置文件',0)";
					String sql_rzrq = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',2,'配置文件',1)";
					if(system_type==0){
						conn_insertUpdateFileInfo.executeUpdate(sql_zh);
					}else if(system_type==1){
						conn_insertUpdateFileInfo.executeUpdate(sql_rzrq);
					}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("xml文件插入update_file表失败！");
				}
			} else {
				try{
					String sql_zh = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',3,'其他文件',0)";
					String sql_rzrq = "insert into update_file (oc_date,file_name,file_time,flag,remark,system_type)values('"
								+ getCurrentDay()
								+ "','"
								+ lf[i].getName()
								+ "','" + lastModifiedTime + "',3,'其他文件',1)";
					if(system_type==0){
						conn_insertUpdateFileInfo.executeUpdate(sql_zh);
					}else if(system_type==1){
						conn_insertUpdateFileInfo.executeUpdate(sql_rzrq);
					}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("其他文件插入update_file表失败！");
				}
					
			}
		}

	}

	/****
	 * 读生成的日志信息,判断上传是否完毕
	 * @param fileName 文件名
	 * @return
	 */
	public static boolean readFileByChars(String fileName) {

		File file = new File(fileName);
		String content = ""; // content保存文件内容，　　　　
		BufferedReader reader = null; // 定义BufferedReader　　　　　
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream(
					file), "gb2312");// 编码装换

			reader = new BufferedReader(in);// 按行读取文件并加入到content中。　　　　　　//当readLine方法返回null时表示文件读取完毕。　　　　　
			String line;
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
		}

		catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取日志文件失败！");
		} finally { // 最后要在finally中将reader对象关闭　　　　　

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		// System.out.println("文件内容：\n" + content);
		// System.out.println("文件字符：" + content.length());
		//System.out.println("文件最后一个字符："+ content.substring(content.length() - 5,content.length() - 2));
		if (content.substring(content.length() - 5, content.length() - 2).equals("end")) {
			//System.out.println("成功导入！");
			return false;
		} else {
			//System.out.println("导入还未完成！");
			return true;
		}

	}
	
	/***
	 * 获得升级设备数量
	 * @param system_type 系统类别0：账户，1：融资融券
 	 * @return
	 */
	public int getDeviceCount(String sql,int system_type){
		
		DBConnection conn_deviceCount = new DBConnection();
		//String sql = "select count(*) as count_device from  update_device where system_type = "+system_type+"";
		
		ResultSet rs_deviceCount = conn_deviceCount.executeQuery(sql);
		try {
			while(rs_deviceCount.next()){
				count_device = rs_deviceCount.getInt("count_device");
				//System.out.println("设备数量："+count_device);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("获得升级设备信息失败！");
		}
		
		return count_device;
		
	}

	public void writeUploadEndFlag(String ip,int system_type) throws IOException {

		//System.out.println("-------进入生成导入命令阶段-------");
		
			if(system_type==0){
				try {
			dos_bat_zh = new RandomAccessFile(CollectSysConfig.filePathresult
					+ "/log/" + getCurrentDay() + "/" + "zh_batCommd" + ".bat",
					"rw");
			dos_bat_zh.seek(dos_bat_zh.length());

			String endflag = "@echo end |mtee /c /+ "
					+ CollectSysConfig.filePathresult + "\\log\\"
					+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
					+ "_log.txt";
			System.out.println("打印账户bat命令:\r\n" + endflag);

			dos_bat_zh.writeBytes(new String(endflag.getBytes(), "iso8859-1")
					+ "\r\n");
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "写入导入命令失败！");
				e.printStackTrace();
			} finally { // finally中将reader对象关闭　　　　　

				if (dos_bat_zh != null) {
					try {
						dos_bat_zh.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			}
			else if(system_type==1){
				try{
				dos_bat_rzrq = new RandomAccessFile(CollectSysConfig.filePathresult
						+ "/log/" + getCurrentDay() + "/" + "rzrq_batCommd" + ".bat",
						"rw");
				dos_bat_rzrq.seek(dos_bat_rzrq.length());

				String endflag = "@echo end |mtee /c /+ "
						+ CollectSysConfig.filePathresult + "\\log\\"
						+ getCurrentDay() + "\\" + ip.replaceAll("\\.", "_")
						+ "_log.txt";
				System.out.println("打印融资融券bat命令:\r\n" +endflag);
				//System.out.println(ip.replaceAll("\\.", "_"));
				//dos_bat_rzrq.writeBytes(new String(upload_comm.getBytes(), "iso8859-1")
				//		+ "\r\n");
				dos_bat_rzrq.writeBytes(new String(endflag.getBytes(), "iso8859-1")
						+ "\r\n");
			}
				catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "写入导入命令失败！");
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (dos_bat_rzrq != null) {
				try {
					dos_bat_rzrq.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }

		}
	}
	
	public void checkEndFlag(String sql,int system_type){
		
		DBConnection conn_endflag = new DBConnection();		
		ResultSet rs_endflag = conn_endflag.executeQuery(sql);
		try {
			while(rs_endflag.next()){
				writeUploadEndFlag(rs_endflag.getString("ip"), system_type);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
}
