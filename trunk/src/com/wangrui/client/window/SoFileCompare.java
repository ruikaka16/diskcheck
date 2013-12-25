package com.wangrui.client.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.MainPanel;
import com.wangrui.client.ReadLinuxFile;
import com.wangrui.server.DBConnection;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class SoFileCompare extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object tableVales[][];
	private int num;
	private int i = 0;
	private JTable jTable3;
	private JTable jTable4;
	private ResultSet rs3;

	/** Creates new form SoFileCompare */
	public SoFileCompare(final int system_type) {

		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTable2 = new javax.swing.JTable();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jComboBox3 = new javax.swing.JComboBox();
		jComboBox4 = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		label_3 = new JLabel("比较结果");
		label_3.setForeground(Color.RED);

		final String[] columnNames = { "文件名", "文件大小", "修改时间" };

		jPanel1.setBorder(javax.swing.BorderFactory
				.createTitledBorder("  "));


		jComboBox4.addItem("");
		jComboBox3.addItem("");
		
			DBConnection conn_combox = new DBConnection();
			String sql5 = "select ip  from update_device where system_type =0";
			String sql5_1 = "select ip  from update_device where system_type=1";
			
			if(system_type==0){
				 rs3 = conn_combox.executeQuery(sql5);
			}else if(system_type==1){
				 rs3 = conn_combox.executeQuery(sql5_1);
			}
	
		try {
			while(rs3.next()){
				jComboBox4.addItem(rs3.getString("ip"));
				jComboBox3.addItem(rs3.getString("ip"));
			}
			rs3.close();
			conn_combox.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jComboBox4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				jComboBox4 = (JComboBox) arg0.getSource();
				String selString = (String) jComboBox4.getSelectedItem();
				DefaultTableModel tableModel = new DefaultTableModel(getFileInfo(selString), columnNames);
				jTable1 = new JTable(tableModel);
				jScrollPane1.setViewportView(jTable1);
				jLabel3.setText(String.valueOf(getNum(selString)));
				
			}
		});
		
		jComboBox3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				jComboBox3 = (JComboBox) arg0.getSource();
				String selString = (String) jComboBox3.getSelectedItem();
				DefaultTableModel tableModel_1 = new DefaultTableModel(getFileInfo(selString), columnNames);
				jTable2 = new JTable(tableModel_1);		
				jScrollPane2.setViewportView(jTable2);
				jLabel4.setText(String.valueOf(getNum(selString)));
				
			}
		});

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433,
				Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1,
				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0,
				Short.MAX_VALUE));

		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder("  "));


		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433,
				Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433,
				Short.MAX_VALUE));

		jButton3.setText("关闭");
		jButton4.setText("开始比较");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
				
				if((Integer.parseInt(jLabel3.getText()))==(Integer.parseInt(jLabel4.getText()))){				
					label_3.setText("文件数量一致");
					if(compareFileName(jComboBox3.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString())==0){
						label_3.setText("文件数量一致,文件名一致");
						    if(executeCompare(jComboBox3.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString())==0){		
						    		label_3.setText("文件数量一致,文件名一致,文件大小一致");
							}else{
								label_3.setText("文件数量一致,文件名一致,有"+executeCompare(jComboBox3.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString())+"个文件大小存在不一致");
							}
					}else{
						label_3.setText("文件数量一致,有"+compareFileName(jComboBox3.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString())+"个文件名不一致");
					}
				}else{
					label_3.setText("文件数量不一致");
				}
			}
		});

		jLabel1.setText("IP地址：");

		jLabel2.setText("IP地址：");
		
		JLabel label = new JLabel("总计数量：");
		
		JLabel label_1 = new JLabel("总计数量：");
		
		final JButton button = new JButton("数据归集");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(isSoFileIn()){
					System.out.println("判断结果"+isSoFileIn());
					Object[] options = { "确定", "取消" };
					int n = JOptionPane.showOptionDialog(null,
							"已存在当天数据，是否要重新归集？", "提示",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,options[1]);
					if(n==0){
						CallableStatement cstmt = null;
						DBConnection c = new DBConnection();
						try {
							Connection conn = c.getConnection();						  
							cstmt = conn.prepareCall("{call del_soinfo()}");
							cstmt.execute();
							new ReadLinuxFile(system_type);
							button.setText("数据归集完成");
							button.setEnabled(false);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} //				
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						return;
					}
									
				}else{
					new ReadLinuxFile(system_type);
					button.setText("数据归集完成");
					button.setEnabled(false);
				}
			}
		});

		GroupLayout layout = new GroupLayout(

						getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
							.addGap(6)
							.addComponent(label_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel4)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
							.addGap(6)
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel3)
							.addPreferredGap(ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
							.addComponent(jButton4)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jButton3)
							.addGap(27))
						.addGroup(layout.createSequentialGroup()
							.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 0, Short.MAX_VALUE)))
					.addContainerGap())
				.addGroup(layout.createSequentialGroup()
					.addGap(34)
					.addComponent(button)
					.addGap(34)
					.addComponent(jLabel1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(label_3)
					.addPreferredGap(ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
					.addComponent(jLabel2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(185))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel1)
						.addComponent(jLabel2)
						.addComponent(label_3)
						.addComponent(button))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(10)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jButton4)
								.addComponent(jButton3)))
						.addGroup(layout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(label_1)
								.addComponent(jLabel4)
								.addComponent(jLabel3))))
					.addContainerGap())
		);
		layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jButton3, jButton4});
		getContentPane().setLayout(layout);
		this.setTitle("文件比较");
		//pack();
		ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/report.gif");//图标路径
        setIconImage(icon.getImage());
			setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        setSize(927, 560);
	        setLocationRelativeTo(this);
			setResizable(false);
	}// </editor-fold>

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		try{     
			//JFrame.setDefaultLookAndFeelDecorated(true); //加上此语句连同窗体外框也改变
			//JDialog.setDefaultLookAndFeelDecorated(true); //加上此语句会使弹出的对话框也改变
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  
			// UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
			
			 UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
			}catch(Exception e){       
				e.printStackTrace(); 
				}
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SoFileCompare(0).setVisible(true);
			}
		});
	}
	
	private static TableModel getFileStats(File a) {
		  String data;
		  Object[] object = null;
		  int columnCount = 0;//文件中最大行的列数
		  DefaultTableModel dt = new DefaultTableModel();
		  try {
		   BufferedReader br = new BufferedReader(new FileReader(a));
		   //不是文件尾一直读
		   while ((data = br.readLine()) != null) {
		    object = data.split(" ");
		    //如果这行的列数大于最大的，那么再增加一列
		    if (object.length > columnCount) {
		     for(int i=0;i<object.length-columnCount;i++){
		      dt.addColumn("column".concat(String.valueOf(i)));
		     }
		     columnCount = object.length;
		    }
		    //添加一行
		    dt.addRow(object);
		   }
		   ;
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return dt;
		 }


	// Variables declaration - do not modify
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JComboBox jComboBox3;
	private javax.swing.JComboBox jComboBox4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTable jTable1;
	private javax.swing.JTable jTable2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel label_3;
	
	/****
	 * 获取表的数据内容
	 * @param ip
	 * @return 返回所有文件
	 */
	private  Object[][] getFileInfo(String ip){
		
		Object tableInfo[][]=null;
		int j=0;
		// 将表中的数据显示到jtable中
		DBConnection conn_table = new DBConnection();
		String sql4 = "select file_name,file_size,file_time from test.update_sofilelist where ip = '"+ip+"' and calc_time = '"+getSystime()+"'";
		ResultSet rs1 = conn_table.executeQuery(sql4);
		tableInfo = new String[getNum(ip)][3];
		System.out.println("获取的文件数量="+getNum(ip));
		try {
			while (rs1.next()) {
				// 在表中获取数据
				tableInfo[j][0] = rs1.getString(1);
				tableInfo[j][1] = rs1.getString(2);
				tableInfo[j][2] = rs1.getString(3);
				j++;

			}
			rs1.close();
			conn_table.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableInfo;
		
	}
	/****
	 * 获取表记录数量
	 * @param ip
	 * @return 返回当天当前ip的文件个数
	 */
	private int getNum(String ip){
		
		int number = 0;
		DBConnection conn_num = new DBConnection();
		String sql3 = "select count(*) from test.update_sofilelist where ip = '"+ip+"' and calc_time = '"+getSystime()+"'";
		ResultSet rs = conn_num.executeQuery(sql3);
		try {
			while (rs.next()) {
				number = rs.getInt(1);
				//jLabel3.setText(String.valueOf(num));
				System.out.println(number);
			}
			rs.close();
			conn_num.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	/****
	 * 比较单一文件大小
	 * @param ip1
	 * @param ip2
	 * @param file_name
	 * @return 返回1为不一致 返回0为一致
	 */
	public int compareSize(String ip1,String ip2,String file_name){
		int i=0;
		DBConnection conn = new DBConnection();
		String sql1 = "select count(*) from (select * from update_sofilelist where file_name = '"+file_name+"' and ip = '"+ip1+"' and calc_time = '"+getSystime()+"') a where a.file_size not in (select b.file_size from update_sofilelist b where b.file_name = file_name and b.ip = '"+ip2+"' and calc_time = '"+getSystime()+"')";
		ResultSet rs = conn.executeQuery(sql1);
		try {
			while(rs.next()){
				i = i+Integer.parseInt(rs.getString("count(*)"));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}	
	/****
	 * 比较文件名
	 * @param ip1
	 * @param ip2
	 * @return 返回不一致记录数
	 */	
	public int compareFileName(String ip1,String ip2){
		int i=0;
		DBConnection conn = new DBConnection();
		String sql1 = "select count(*) from update_sofilelist where ip='"+ip1+"' and calc_time = '"+getSystime()+"' and file_name not in (select file_name from update_sofilelist where ip = '"+ip2+"' and calc_time = '"+getSystime()+"')";
		ResultSet rs = conn.executeQuery(sql1);
		try {
			while(rs.next()){
				i = i+Integer.parseInt(rs.getString("count(*)"));
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return i;
	}
	/****
	 * 比较所有文件的大小
	 * @param ip1
	 * @param ip2
	 * @return 返回不一致记录数
	 */
	public  int  executeCompare(String ip1,String ip2){
		String file_name = "";
		DBConnection conn = new DBConnection();
		int i=0;
		String sql = "select file_name from update_sofilelist where ip = '"+ip1+"' and calc_time = '"+getSystime()+"'";
		ResultSet rs = conn.executeQuery(sql);
		try {
			while(rs.next()){
				file_name = rs.getString("file_name");
				i=i+compareSize(ip1,ip2,file_name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return i;
	}
	/****
	 * 获取当前时间
	 * @return
	 */
	public static String getSystime() {

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);	
	}
	
	public boolean isSoFileIn(){
		try{
			int i=0;
			String sql = "select count(*) from test.update_sofilelist where calc_time = date_format(now(),'%Y%m%d')";
			System.out.println(sql);
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			while(rs_vaildInsert.next()){
				//System.out.println("i="+i);
				if(rs_vaildInsert.getString("count(*)").equals("0")){
					//JOptionPane.showMessageDialog(null, "该操作员已被占用");
					return false;
				}else{
					return true;
				}				
			}	return false;
		}catch(Exception e){
			
			e.printStackTrace();
			return true;
		}
	}
}
