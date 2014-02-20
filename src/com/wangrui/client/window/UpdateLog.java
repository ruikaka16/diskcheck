package com.wangrui.client.window;

import java.awt.BorderLayout;
import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.JExpectSearchField;
import com.wangrui.client.LoginMain;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.TabExpander;

import com.wangrui.server.DBConnection;

public class UpdateLog extends JDialog{

	private DefaultTableModel tableModel; // 表格模型对象
	private JTable table;
	private JPanel jp;
	private JButton searchBt,printBt;
	private DBConnection conn_num, conn_getUpdateLog;
	Statement stmt, stmt1;
	ResultSet rs, rs1, rs2;
	int num; // 记录条数
	int i = 0;
	Float util;
	private Object tableVales[][];
	private JExpectSearchField jExpectSearchField, jExpectSearchField1;
	private JLabel searchLable, searchComboxLable;
	private JComboBox searchCombox,searchCombox1;
	
	public UpdateLog(int system_type){
		
		// 界面部分
		super();
		ImageIcon icon=new ImageIcon(LoginMain.app_path+"/image/magnifier.png");//图标路径
	    setIconImage(icon.getImage());
	    
	   // setModal(true);
		setTitle("升级记录查询");
		//setBounds(100, 100, 800, 600);
		 setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	     setSize(800, 600);
	     setLocationRelativeTo(this);
	     
	     setResizable(false);
			
		String[] columnNames = { "升级日期", "升级系统", "升级摘要", "操作员"}; // 列名
		jp = new JPanel();
		searchBt = new JButton("搜索");
		printBt = new JButton("打印");
		searchCombox = new JComboBox();
		searchLable = new JLabel("输入升级日期:");
		searchComboxLable = new JLabel("升级系统 ：");

		searchCombox1 = new JComboBox();
		searchCombox1.addItem("账户系统升级");
		searchCombox1.addItem("转融通升级");
		
		searchCombox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				searchCombox = (JComboBox) e.getSource();
				String selString = (String) searchCombox.getSelectedItem();
				//System.out.println("searchCombox=" + selString);
			}
		});

		
		if(getNum(system_type)==0){
			JOptionPane.showMessageDialog(null, "没有可用数据！");
			//System.out.println("zh系统"+getNum(system_type));
			return;
			
		}else{
			// 将表中的数据显示到jtable中
			conn_getUpdateLog = new DBConnection();
			String sql4 = "select oc_date,remark,update_content ,operator from update_log where system_type = "+system_type+" order by oc_date desc";
			//String sql4_1 = "select oc_date,remark,update_content ,operator from update_log where system_type = 1 order by oc_date desc";
			
		
			ArrayList array = conn_getUpdateLog.executeQuery3(sql4);
			Table_Model_Updatelog model1 = new Table_Model_Updatelog(array);
			table = new JTable(model1) { // 表格不允许被编辑
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};
			
			// table字段居中显示
			DefaultTableCellRenderer cellRanderer = new DefaultTableCellRenderer();
			cellRanderer.setHorizontalAlignment(JLabel.CENTER);
			table.setDefaultRenderer(Object.class, cellRanderer);
			setVisible(true);
			// table排序:


			// googlesuggest搜索显示
			DBConnection conn_searchSuggest = new DBConnection();
			String sql = "select distinct oc_date from test.update_log where system_type = "+system_type+"";
			ResultSet rs_search = conn_searchSuggest.executeQuery(sql);

			try {
				List<Object> value = new ArrayList<Object>();
				while (rs_search.next()) {

					value.add(rs_search.getString("oc_date"));
					jExpectSearchField = new JExpectSearchField(value, 50);

				}

			} catch (Exception e) {

			}

			jExpectSearchField.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					if(jExpectSearchField.getText().equals("")){
						searchCombox.removeAllItems();
					}
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {}
			});
			
			jExpectSearchField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						searchBt.requestFocus(); // 实现按下回车键换行的方法
				}
			});
			//jExpectSearchField增加监听，jcombox根据盘符来联动显示对应ip地址
			jExpectSearchField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//System.out.println("device=" + jExpectSearchField.getText());
					searchCombox.removeAllItems();
					DBConnection conn_searchCombox = new DBConnection();
					String sql1 = "select distinct remark from update_log where oc_date = '"
							+ jExpectSearchField.getText() + "'";
					ResultSet rs_searchCombox = conn_searchCombox
							.executeQuery(sql1);
					try {
						while (rs_searchCombox.next()) {

							searchCombox.addItem(rs_searchCombox.getString("remark"));
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});

			jExpectSearchField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						searchCombox.requestFocus(); // 实现按下回车键换行的方法
				}
			});

			searchCombox.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						searchBt.requestFocus(); // 实现按下回车键换行的方法
				}
			});

			// 搜索按钮监听
			searchBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					DBConnection conn_search = new DBConnection();
					String sql = "select oc_date,remark,update_content ,operator from test.update_log where oc_date ='"
							+ jExpectSearchField.getText() + "' and remark = '"
							+ searchCombox.getSelectedItem()
							+ "' order by oc_date";
					String sql5 = "select oc_date,remark,update_content ,operator from update_log";
					
					
					ArrayList searcharray = null;
					if(jExpectSearchField.getText().equals("")){
						 searcharray = conn_getUpdateLog.executeQuery3(sql5);
						 //System.out.println("sql5=" + sql5);
							Table_Model_Updatelog model2 = new Table_Model_Updatelog(searcharray);
							// table.remove(table);
							table.updateUI(); // 按搜索条件重绘table
							table.setModel(model2);
							table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
					}else{
						 searcharray = conn_getUpdateLog.executeQuery3(sql);
						 //System.out.println("sql=" + sql);
							Table_Model_Updatelog model2 = new Table_Model_Updatelog(searcharray);
							// table.remove(table);
							table.updateUI(); // 按搜索条件重绘table
							table.setModel(model2);
							table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
					}
				
				}
			});

			searchBt.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					DBConnection conn_search = new DBConnection();
					String sql = "select oc_date,remark,update_content ,operator from test.update_log where oc_date ='"
							+ jExpectSearchField.getText() + "' and remark = '"
							+ searchCombox.getSelectedItem()
							+ "' order by oc_date";
					String sql5 = "select oc_date,remark,update_content ,operator from update_log";
					
					
					ArrayList searcharray = null;
					if(jExpectSearchField.getText().equals("")){
						 searcharray = conn_getUpdateLog.executeQuery3(sql5);
						 //System.out.println("sql5=" + sql5);
							Table_Model_Updatelog model2 = new Table_Model_Updatelog(searcharray);
							// table.remove(table);
							table.updateUI(); // 按搜索条件重绘table
							table.setModel(model2);
							table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
					}else{
						 searcharray = conn_getUpdateLog.executeQuery3(sql);
						 //System.out.println("sql=" + sql);
							Table_Model_Updatelog model2 = new Table_Model_Updatelog(searcharray);
							// table.remove(table);
							table.updateUI(); // 按搜索条件重绘table
							table.setModel(model2);
							table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
					}
				
				}
			});
			
			printBt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub

					MessageFormat footer = new MessageFormat("- {0} -"); //页脚加页码
					MessageFormat header = new MessageFormat("Printed: " + new Date()); //页眉加时间
					PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
					aset.add(OrientationRequested.PORTRAIT); //横排列打印，改为OrientationRequested.LANDSCAPE为竖排列
					 
					try {
						table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, aset, true);
					} catch (HeadlessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PrinterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
			//jp.add(searchCombox1);
			jp.add(searchLable);
			jp.add(jExpectSearchField);
			jp.add(searchComboxLable);
			jp.add(searchCombox);
			// jp.add(jExpectSearchField1);
			jp.add(searchBt);
			jp.add(printBt);
			add(jp, BorderLayout.NORTH);
			add(scrollPane, BorderLayout.CENTER);
			scrollPane.setViewportView(table);
			
			//setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	       
		}
	}
	public int getNum(int system_type){
		
		// 获得表中的数据条数记入num
		conn_num = new DBConnection();
		String sql3 = "select count(*)  from test.update_log where system_type=0";
		String sql3_1 = "select count(*) from test.update_log where system_type=1";
		if(system_type == 0){
		rs = conn_num.executeQuery(sql3);
		}else if(system_type == 1){
			rs = conn_num.executeQuery(sql3_1);
		}
		try {
			while (rs.next()) {
				num = rs.getInt(1);
			}
			rs.close();
			conn_num.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return num;
	}
}
