package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.ErrorManager;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import com.wangrui.server.DBConnection;

public class DeviceConfig extends JFrame {

	private DefaultTableModel tableModel; // 表格模型对象
	private JTable table;
	private JTextField aTextField, cTextField;
	private JTextField bTextField, dTextField;
	private JPasswordField password;
	DBConnection conn_num, conn_table, conn_insert, conn_del, conn_modify;
	Statement stmt, stmt1;
	ResultSet rs, rs1, rs2;
	private JComboBox cb; // 20121011 增加下拉框
	private static String[] data = { "Windows", "Linux" };// 20121011增加下拉框中的默认数据
	int num; // 记录条数
	int i = 0;
	private Object tableVales[][];

	public DeviceConfig() {
		// 界面部分
		super();
		ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/magnifier.png");//图标路径
        setIconImage(icon.getImage());
		setTitle("查询设备配置");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String[] columnNames = { "IP地址", "用户名", "密码", "操作系统" }; // 20121011列增加os字段对应操作系统
		cb = new JComboBox();

		// 在下拉框中增加默认数据
		for (int i = 0; i < data.length; i++) {
			cb.addItem(data[i]);
		}

		// 获得表中的数据条数记入num
		conn_num = new DBConnection();
		String sql3 = "select count(*) from test.device";
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

		// 将表中的数据显示到jtable中
		conn_table = new DBConnection();
		String sql4 = "select * from test.device  order by os";
		rs1 = conn_table.executeQuery(sql4);
		tableVales = new String[num][7];
		try {
			while (rs1.next()) {
				// 在表中获取数据
				tableVales[i][0] = rs1.getString(1);
				tableVales[i][1] = rs1.getString(2);
				tableVales[i][2] = rs1.getString(3);
				tableVales[i][3] = rs1.getString(4);
				i++;

			}
			rs1.close();
			conn_table.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 添加数据和表到表中
		tableModel = new DefaultTableModel(tableVales, columnNames);
		table = new JTable(tableModel);

		DefaultTableCellRenderer cellRanderer = new DefaultTableCellRenderer();
		cellRanderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, cellRanderer);

		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		// 排序:
		table.setGridColor(Color.BLACK);
		// table.setRowSorter(new TableRowSorter(tableModel));
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object oa = tableModel.getValueAt(selectedRow, 0);
				Object ob = tableModel.getValueAt(selectedRow, 1);
				Object oc = tableModel.getValueAt(selectedRow, 2);
				Object od = tableModel.getValueAt(selectedRow, 3);
				aTextField.setText(oa.toString()); // 给文本框赋值
				bTextField.setText(ob.toString());
				password.setText(oc.toString());
				// 设置添加设备下来框中根据选择的设备信息在Jcombox显示
				if (od.toString().equals("Windows")) {
					cb.removeAllItems();
					cb.addItem(data[0]);
					cb.addItem(data[1]);
					cb.setSelectedItem("Windows");
				} else if (od.toString().equals("Linux")) {
					cb.removeAllItems();
					cb.addItem(data[0]);
					cb.addItem(data[1]);
					cb.setSelectedItem("Linux");
				}
				System.out.println(data[1]);
			}
		});

		// 设置表格密码字段table中表格显示为******
		table.getColumn("密码").setCellRenderer(new DefaultTableCellRenderer() {
			// 重写 setValue 方法
			public void setValue(Object value) {
				String password = "";
				int wordLong = value.toString().length();

				for (int i = 0; i < wordLong; i++)
					password += "*";

				super.setValue(password);
			}
		});

		scrollPane.setViewportView(table);
		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.add(new JLabel("IP地址: "));
		aTextField = new JTextField("", 8);
		panel.add(aTextField);
		panel.add(new JLabel("用户名: "));
		bTextField = new JTextField("", 8);
		panel.add(bTextField);

		panel.add(new JLabel("密码: "));
		cTextField = new JTextField("", 10);
		password = new JPasswordField("", 8);
		// panel.add(cTextField);
		panel.add(password);

		panel.add(new JLabel("操作系统: "));
		panel.add(cb);

		// 添加查询设备
		final JButton addButton = new JButton("添加"); // 添加按钮
		addButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						
						System.out.println("ip="+aTextField.getText());

						if (aTextField.getText().length() == 0) {
							JOptionPane
									.showMessageDialog(null, "请输入查询设备的IP地址！");
							return;
						}
						// 对IP地址进行验证
						if (aTextField.getText() != null
								&& !aTextField.getText().isEmpty()) {
							// 定义正则表达式
							String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
									+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
							// 判断ip地址是否与正则表达式匹配
							if (!aTextField.getText().matches(regex)) {
								// 返回判断信息
								JOptionPane.showMessageDialog(null,
										aTextField.getText() + "不是一个合法的IP地址！");
								return;
							}
						}
						System.out.println("获得Jcombox的当前值："
								+ cb.getSelectedItem());

						if (bTextField.getText().length() == 0) {
							JOptionPane.showMessageDialog(null, "请输入查询设备的用户名！");
							return;
						}
						if (password.getText().length() == 0) {

							JOptionPane
									.showMessageDialog(null, "请输入查询设备的登录密码！");
							return;
						}
						if(aTextField.getText()!=null&&bTextField.getText()!=null&&password.getText()!=null)
							{
							   if(vaildInsert(aTextField.getText())){
								   aTextField.setText("");
								   bTextField.setText("");
								   password.setText("");
								   return ;
							   }
							   else {

								// 后台数据库操作
								try {
									String sql5 = "insert into test.device (ip,username,password,os) values('"
											+ aTextField.getText()
											+ "','"
											+ bTextField.getText()
											+ "','"
											+ password.getText()
											+ "','"
											+ cb.getSelectedItem() + "')";
									conn_insert = new DBConnection();
									conn_insert.executeUpdate(sql5);
									JOptionPane.showMessageDialog(null, "该设备"
											+ aTextField.getText() + "已添加");
									String[] rowValues = { aTextField.getText(),
											bTextField.getText(),
											password.getText(),
											cb.getSelectedItem().toString() };
									tableModel.addRow(rowValues); // 添加一行
									int rowCount = table.getRowCount() + 1;// 行数加上1

								}

								catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null,"插入数据失败！");
									
								}
								
								conn_insert.close();
							   }
							}					

					}
				});
		panel.add(addButton);

		// 删除选中设备
		final JButton delButton = new JButton("删除");
		delButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {

						if (aTextField.getText().equals("")) {

							JOptionPane.showMessageDialog(null, "请先选择删除的内容！");

						} else {
							int selectedRow = table.getSelectedRow();// 获得选中行的索引
							if (selectedRow != -1) // 存在选中行
							{
								tableModel.removeRow(selectedRow); // 删除行
							}
							// 后台数据库操作
							try {
								conn_del = new DBConnection();
								String sql1 = "delete from test.device where ip ='"
										+ aTextField.getText() + "'";
								conn_del.executeUpdate(sql1);
								conn_del.close();

								JOptionPane.showMessageDialog(null, "该表"
										+ aTextField.getText() + "已删除");
								bTextField.setText("");
								aTextField.setText("");
								cTextField.setText("");

							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null,
										"查询设备删除失败，请联系开发人员！");
							}

						}
					}
				});
		panel.add(delButton);
		// 修改选中设备信息
		final JButton updateButton = new JButton("修改");
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// 对IP地址进行验证
				
					// 定义正则表达式
					String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
							+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
							+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
							+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
					// 判断ip地址是否与正则表达式匹配
					if (!aTextField.getText().matches(regex)) {
						// 返回判断信息
						JOptionPane.showMessageDialog(null,
								aTextField.getText() + "不是一个合法的IP地址！");
						return;
					} else {

					try {
						String sql = "update device set username = '"
								+ bTextField.getText() + "',password = '"
								+ password.getText() + "',os = '"
								+ cb.getSelectedItem() + "' where ip = '"
								+ aTextField.getText() + "'";
						conn_modify = new DBConnection();
						conn_modify.executeUpdate(sql);
						conn_modify.close();
						JOptionPane.showMessageDialog(null, "该设备信息已经修改！");
						bTextField.setText("");
						aTextField.setText("");
						password.setText("");
						setVisible(false);

					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane
								.showMessageDialog(null, "设备信息修改失败，请联系开发人员！");
					}
				}

			}
		});
		panel.add(updateButton);

	}

	public Boolean vaildInsert(String ip){
		
		try{
			int i=0;
			String sql = "select * from test.device where ip = '"+ip+"'";
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			if(rs_vaildInsert.next()){
				//System.out.println("i="+i);
				JOptionPane.showMessageDialog(null, "该设备已存在,请重新输入设备ip地址！");
				return true;
			}
	
			else {
				
				return false;
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {

		new DeviceConfig();

	}

}
