package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.ErrorManager;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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

import org.apache.log4j.Logger;

import com.wangrui.client.window.AddUpdateDevice;
import com.wangrui.client.window.ModifedUpdateDevice;
import com.wangrui.server.DBConnection;

/****
 * @author wangrui
 * 为jTable设置图标，直观显示设备的状态
 */
class MyTableCellRenderer implements TableCellRenderer {
	public CompoundIcon c;
	public JLabel label;
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// 根据特定的单元格设置不同的Renderer,假如你要在第2行第3列显示图标
		DBConnection conn = new DBConnection();
		String sql = "select ip,username,password from update_device";
		ResultSet rs = conn.executeQuery(sql);
		int i = 0;
		Icon[] deleteIcon = new Icon[] { new ImageIcon(
				"D:/Management/image/delete.png") };
		Icon[] acceptIcon = new Icon[] { new ImageIcon(
				"D:/Management/image/accept.png") };
		
		try {
			while (rs.next()) {
				
				if (column == 7 && row == i) {
					System.out.println("当前行数i="+i);
					if (checkDeviceStatus(rs.getString("ip"),
							rs.getString("username"), rs.getString("password")) == false) {

						System.out.println(rs.getString("ip") + "设备出现问题！");
						// JOptionPane.showMessageDialog(null, "升级设备存在问题，请检查！");

						c = new CompoundIcon(deleteIcon);
						label = new JLabel(c);
						label.setOpaque(false);
						return label;
					}else{
						System.out.println(rs.getString("ip") + "设备检查正常");
						c = new CompoundIcon(acceptIcon);
						label = new JLabel(c);
						label.setOpaque(false);
						return label;	
					}
				} 
				i++;
			}
			
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;

	}

	/****
	 * 检查设备用户名和密码是否正确
	 * @param host
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private boolean checkDeviceStatus(String host, String username,
			String password) throws IOException {

		final Logger logger1 = Logger.getLogger(UpdatePanel.class);
		if (logger1.isDebugEnabled()) {

			logger1.debug("connecting to " + host + " with user " + username

			+ " and pwd " + password);

		}

		ch.ethz.ssh2.Connection conn = new ch.ethz.ssh2.Connection(host);

		conn.connect(); // make sure the connection is opened

		boolean isAuthenticated = conn.authenticateWithPassword(username,

		password);

		if (isAuthenticated == false) {

			// JOptionPane.showMessageDialog(null, "升级设备认证失败或已关机，请检查！");
			// throw new IOException(host+"认证失败.");
			System.out.println("升级设备认证失败或已关机，请检查！");

		} else {
			return true;
		}
		return isAuthenticated;

	}
}

class MyTableCellRenderer1 implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// 根据特定的单元格设置不同的Renderer,假如你要在第2行第3列显示图标
		if (column == 7) {
			Icon[] icons = new Icon[] { new ImageIcon(
					"D:/Management/image/delete.png") };
			CompoundIcon c = new CompoundIcon(icons);
			JLabel l = new JLabel(c);
			l.setOpaque(false);
			return l;
		} else {
			return null;
		}
	}
}

public class UpdateDeviceConfig extends JDialog {

	public static DefaultTableModel tableModel; // 表格模型对象
	public static JTable table;
	private JTextField aTextField;
	private JTextField bTextField, dTextField, eTextField;
	private JPasswordField password;
	DBConnection conn_num, conn_table, conn_insert, conn_del, conn_modify;
	Statement stmt, stmt1;
	ResultSet rs, rs1, rs2;
	int num; // 记录条数
	int i = 0;
	private Object tableVales[][];

	// private static String[] data = { "是", "否" };

	public UpdateDeviceConfig(int system_type) {
		// 界面部分
		super();

		setTitle("系统升级设备配置");
		setModal(true); // 子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String[] columnNames = { "IP地址", "用户名", "密码", "备份标志", "客户端升级标志",
				"灾备标志", "系统标志", "设备状态" }; // 20121011列增加os字段对应操作系统
		ImageIcon icon = new ImageIcon(CollectSysConfig.filePathresult
				+ "/image/computer.png");// 图标路径
		setIconImage(icon.getImage());
		// 在下拉框中增加默认数据
		// for (int i = 0; i < data.length; i++) {
		// cb.addItem(data[i]);
		// }

		// 获得表中的数据条数记入num
		conn_num = new DBConnection();
		String sql3 = "select count(*) from test.update_device where system_type= "+system_type+"";
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
		String sql4 = "select ip,username,password,backup_flag,updatedir_flag,bdb_flag,system_type from test.update_device where system_type="+system_type+"";
		rs1 = conn_table.executeQuery(sql4);
		tableVales = new String[num][10];

		try {
			while (rs1.next()) {
				// 在表中获取数据
				tableVales[i][0] = rs1.getString(1);
				tableVales[i][1] = rs1.getString(2);
				tableVales[i][2] = rs1.getString(3);
				tableVales[i][3] = rs1.getString(4);
				tableVales[i][4] = rs1.getString(5);
				tableVales[i][5] = rs1.getString(6);
				tableVales[i][6] = rs1.getString(7);
				i++;

			}
			rs1.close();
			conn_table.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 添加数据和表到表中
		tableModel = new DefaultTableModel(tableVales, columnNames) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {// 双击后不可编辑
				return false;
			}
		};
		table = new JTable(tableModel);

		TableCellRenderer myRenderer1 = new MyTableCellRenderer1();
		table.getColumnModel().getColumn(7).setCellRenderer(myRenderer1);
		// DefaultTableCellRenderer cellRanderer = new
		// DefaultTableCellRenderer();
		// cellRanderer.setHorizontalAlignment(JLabel.CENTER);
		// table.setDefaultRenderer(Object.class, cellRanderer);

		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		// 排序:
		// table.setGridColor(Color.BLACK);
		// table.setRowSorter(new TableRowSorter(tableModel));
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选

		table.addMouseListener(new MouseAdapter() { // 鼠标事件
			public void mouseClicked(MouseEvent e) {
				int selectedRow = table.getSelectedRow(); // 获得选中行索引
				Object oa = tableModel.getValueAt(selectedRow, 0);
				Object ob = tableModel.getValueAt(selectedRow, 1);
				Object oc = tableModel.getValueAt(selectedRow, 2);
				Object od = tableModel.getValueAt(selectedRow, 3);
				Object oe = tableModel.getValueAt(selectedRow, 4);
				Object of = tableModel.getValueAt(selectedRow, 5);
				Object og = tableModel.getValueAt(selectedRow, 6);
				// Object oh = tableModel.getValueAt(selectedRow, 7);

				if (e.getClickCount() == 2) {

					// System.out.println("双击表格");
					ModifedUpdateDevice modifedUpdateDeviceWin = new ModifedUpdateDevice();
					// System.out.println("jTextField1.setText(oa.toString()="+oa.toString());
					modifedUpdateDeviceWin.jTextField1.setText(oa.toString());
					modifedUpdateDeviceWin.jTextField1.setEnabled(false);
					modifedUpdateDeviceWin.jTextField2.setText(ob.toString());

					if (od.toString().equals("0")) {
						modifedUpdateDeviceWin.jComboBox1.setSelectedItem("否");
						// System.out.println("备份标志 1"+modifedUpdateDeviceWin.jComboBox1.getSelectedItem());
					} else {
						modifedUpdateDeviceWin.jComboBox1.setSelectedItem("是");
						// System.out.println("备份标志 1"+modifedUpdateDeviceWin.jComboBox1.getSelectedItem());
					}
					if (oe.toString().equals("0")) {
						modifedUpdateDeviceWin.jComboBox2.setSelectedItem("否");
						// System.out.println("客户端标志 2"+modifedUpdateDeviceWin.jComboBox2.getSelectedItem());
					} else {
						modifedUpdateDeviceWin.jComboBox2.setSelectedItem("是");
						// System.out.println("客户端标志 2"+modifedUpdateDeviceWin.jComboBox2.getSelectedItem());
					}
					if (of.toString().equals("0")) {
						modifedUpdateDeviceWin.jComboBox3.setSelectedItem("否");
						// System.out.println("灾备标志 3"+modifedUpdateDeviceWin.jComboBox3.getSelectedItem());
					} else {
						modifedUpdateDeviceWin.jComboBox3.setSelectedItem("是");
						// System.out.println("灾备标志 3"+modifedUpdateDeviceWin.jComboBox3.getSelectedItem());
					}
					if (og.toString().equals("0")) {
						modifedUpdateDeviceWin.jComboBox4
								.setSelectedItem("账户管理");
						// System.out.println("系统标志"+modifedUpdateDeviceWin.jComboBox4.getSelectedItem());
					} else {
						modifedUpdateDeviceWin.jComboBox4
								.setSelectedItem("融资融券");
						// System.out.println("系统标志"+modifedUpdateDeviceWin.jComboBox4.getSelectedItem());
					}
					modifedUpdateDeviceWin.setVisible(true);

				} else {

					aTextField.setText(oa.toString()); // 给文本框赋值

				}
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
		aTextField = new JTextField("", 15);
		panel.add(aTextField);

		// 添加查询设备
		final JButton addButton = new JButton("添加"); // 添加按钮
		addButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {

						AddUpdateDevice addupdateWin = new AddUpdateDevice();
						addupdateWin.setVisible(true);
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

							Object[] options = { "确定", "取消" };
							int n = JOptionPane.showOptionDialog(null,
									"是否要删除该设备？", "提示",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, 
									options, 
									options[1]);
							if (n == 0) {
								// 后台数据库操作
								try {
									conn_del = new DBConnection();
									String sql1 = "delete from test.update_device where ip ='"
											+ aTextField.getText() + "'";
									conn_del.executeUpdate(sql1);
									conn_del.close();

									JOptionPane.showMessageDialog(null, "该表"
											+ aTextField.getText() + "已删除");
									aTextField.setText("");
									int selectedRow = table.getSelectedRow();// 获得选中行的索引
									if (selectedRow != -1) // 存在选中行
									{
										tableModel.removeRow(selectedRow); // 删除行
									}

								} catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null,
											"查询设备删除失败，请联系开发人员！");
								}
							} else {
								return;
							}

						}
					}
				});
		panel.add(delButton);

		JButton testButton = new JButton("测试连接");
		testButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				TableCellRenderer myRenderer = new MyTableCellRenderer();
				table.getColumnModel().getColumn(7).setCellRenderer(myRenderer);
				table.repaint();
			}
		});
		panel.add(testButton);
		// 修改选中设备信息
		final JButton updateButton = new JButton("修改");
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// 对IP地址进行验证 定义正则表达式
				String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
						+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
						+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
						+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
				// 判断ip地址是否与正则表达式匹配
				if (!aTextField.getText().matches(regex)) {
					// 返回判断信息
					JOptionPane.showMessageDialog(null, aTextField.getText()
							+ "不是一个合法的IP地址！");
					return;
				} else {

					try {
						String sql = "update update_device set username = '"
								+ bTextField.getText() + "',password = '"
								+ password.getText() + "' ,backup_flag = "
								+ dTextField.getText() + " ,updatedir_flag = "
								+ eTextField.getText() + " where ip = '"
								+ aTextField.getText() + "'";
						System.out.println("updatesql=" + sql);
						conn_modify = new DBConnection();
						conn_modify.executeUpdate(sql);
						conn_modify.close();
						JOptionPane.showMessageDialog(null, "该设备信息已经修改！");
						bTextField.setText("");
						aTextField.setText("");
						password.setText("");
						dTextField.setText("");
						eTextField.setText("");
						setVisible(false);

					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane
								.showMessageDialog(null, "设备信息修改失败，请联系开发人员！");
					}
				}

			}
		});
		// panel.add(updateButton);

	}
	/****
	 * 插入新设备前检查是否已存在
	 * @param ip
	 * @return
	 */

	public Boolean vaildInsert(String ip) {

		try {
			int i = 0;
			String sql = "select * from test.update_device where ip = '" + ip
					+ "'";
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			if (rs_vaildInsert.next()) {
				// System.out.println("i="+i);
				JOptionPane.showMessageDialog(null, "该设备已存在,请重新输入设备ip地址！");
				return true;
			}

			else {

				return false;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		new UpdateDeviceConfig(0).setVisible(true);

	}

}
