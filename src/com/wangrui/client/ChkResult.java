package com.wangrui.client;

import java.awt.BorderLayout;
import com.wangrui.client.CollectSysConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class ChkResult extends JFrame {

	private DefaultTableModel tableModel; // 表格模型对象
	private JTable table;
	private JPanel jp;
	private JButton searchBt;
	private JTextField aTextField, cTextField;
	private JTextField bTextField, dTextField;
	DBConnection conn_num, conn_table;
	Statement stmt, stmt1;
	ResultSet rs, rs1, rs2;
	int num; // 记录条数
	int i = 0;
	Float util;
	private Object tableVales[][];
	private JExpectSearchField jExpectSearchField, jExpectSearchField1;
	private JLabel searchLable, searchComboxLable;
	private JComboBox searchCombox;

	// private static String[] data = { "date", "ip" };// 20121019增加下拉框中的默认数据

	public ChkResult(final int system_type) {

		// 界面部分
		super();
		ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/drive_disk.png");//图标路径
	    setIconImage(icon.getImage());
		setTitle("磁盘信息查询结果");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String[] columnNames = { "日期", "ip地址", "盘符", "可用空间(G)", "总空间(G)",
				"利用率(%)", "类型" }; // 列名
		util = Float.parseFloat(CollectSysConfig.utilresult);
		jp = new JPanel();
		searchBt = new JButton("搜索");
		searchCombox = new JComboBox();
		searchLable = new JLabel("输入盘符：");
		searchComboxLable = new JLabel("对应设备ip地址 ：");

		searchCombox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				searchCombox = (JComboBox) e.getSource();
				String selString = (String) searchCombox.getSelectedItem();
				System.out.println("searchCombox=" + selString);
			}
		});

		// 获得表中的数据条数记入num
		conn_num = new DBConnection();
		String sql3 = "select count(*) from test.deviceDisk where system_type = "+system_type+"";
		rs = conn_num.executeQuery(sql3);
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

		if(num == 0){
			JOptionPane.showMessageDialog(null, "没有可用数据！");
			return;
		}else{
			// 将表中的数据显示到jtable中
			conn_table = new DBConnection();
			String sql4 = "select date,ip,deviceid,freespace,size,util,type from test.deviceDisk where deviceid not in ('/boot')  and system_type = "+system_type+" order by date desc ";
			ArrayList array = conn_table.executeQuery1(sql4);
			System.out.println("数据量="+array.size());
			Table_Model model1 = new Table_Model(array);

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
			table.setRowSorter(new TableRowSorter(model1));

			table.setDefaultRenderer(Object.class, new TableCellRenderer() {   
				public Component getTableCellRendererComponent(JTable table,  	
						Object value, boolean isSelected, boolean hasFocus, int row, int column) 
					{  				
					JTextField text = new JTextField(value.toString());  
					if (Integer.parseInt(table.getValueAt(row, 6).toString()) == 1) {    				
						//text.setText("超出阀值"); 
					// table.getCellEditor(row, 6).getTableCellEditorComponent(table, value, isSelected, row, column).setBackground(Color.RED);
						//int b = Integer.parseInt(a);
						//StringToInt("ok", 1);
						//table.setValueAt(Integer.parseInt((String)a), row, 6);
						text.setBackground(Color.RED); 
						} else {  			
							//text.setText("正常");  
							text.setBackground(Color.WHITE);  	
							//text.setForeground(Color.CYAN);    
							}    			
					return text;  		
					}    		
				}); 
			

			
			System.out.println("数据库条数：" + num);

			// googlesuggest搜索显示
			DBConnection conn_searchSuggest = new DBConnection();
			String sql = "select distinct deviceid from test.devicedisk where system_type="+system_type+"";
			ResultSet rs_search = conn_searchSuggest.executeQuery(sql);

			try {
				List<Object> value = new ArrayList<Object>();
				while (rs_search.next()) {

					value.add(rs_search.getString("deviceid"));
					jExpectSearchField = new JExpectSearchField(value, 50);

				}

			} catch (Exception e) {

			}

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
					System.out.println("device=" + jExpectSearchField.getText());
					searchCombox.removeAllItems();
					DBConnection conn_searchCombox = new DBConnection();
					String sql1 = "select distinct ip from devicedisk where deviceid = '"
							+ jExpectSearchField.getText() + "' and system_type="+system_type+"";
					ResultSet rs_searchCombox = conn_searchCombox
							.executeQuery(sql1);
					try {
						while (rs_searchCombox.next()) {

							searchCombox.addItem(rs_searchCombox.getString("ip"));
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
					String sql = "select * from test.devicedisk where deviceid ='"
							+ jExpectSearchField.getText() + "' and ip = '"
							+ searchCombox.getSelectedItem()
							+ "' and system_type="+system_type+" order by date desc";
					System.out.println("sql=" + sql);
					ArrayList searcharray = conn_table.executeQuery1(sql);
					Table_Model model2 = new Table_Model(searcharray);
					// table.remove(table);
					table.updateUI(); // 按搜索条件重绘table
					table.setModel(model2);
					table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
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
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						DBConnection conn_search = new DBConnection();
						String sql = "select * from test.devicedisk where deviceid ='"
								+ jExpectSearchField.getText()
								+ "' and ip = '"
								+ searchCombox.getSelectedItem()
								+ "' and system_type="+system_type+" order by date desc";
						System.out.println("sql=" + sql);
						ArrayList searcharray = conn_table.executeQuery1(sql);
						Table_Model model2 = new Table_Model(searcharray);
						// table.remove(table);
						table.updateUI(); // 按搜索条件重绘table
						table.setModel(model2);
						table.setRowSorter(new TableRowSorter(model2));// 搜索后的结果进行排序
					}
				}
			});

			JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
			jp.add(searchLable);
			jp.add(jExpectSearchField);
			jp.add(searchComboxLable);
			jp.add(searchCombox);
			// jp.add(jExpectSearchField1);
			jp.add(searchBt);
			add(jp, BorderLayout.NORTH);
			add(scrollPane, BorderLayout.CENTER);
			scrollPane.setViewportView(table);
		}
	}
	public static int StringToInt(String str, int def) {
        int intRet = def;
        try {
            if (str == null || str.trim().equals(""))
                str = def + "";
            intRet = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return intRet;
    }

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// // TODO Auto-generated method stub
//		new ChkResult();
//	}

}
