package com.wangrui.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class PasswordTable extends JFrame {

	// 表头信息
	private String[] colname = { "姓名", "性别", "年龄", "帐号", "密码" };
	// 表内容
	private Object[][] data = {
			{ "张三", new Boolean(true), "18", "Zhang3", "12345" },
			{ "李四", new Boolean(true), "22", "Li4", "54321" },
			{ "王五", new Boolean(false), "23", "Wang5", "52052" },
			{ "啊明", new Boolean(true), "19", "A_ming", "28865" },
			{ "珊珊", new Boolean(false), "21", "Sam3", "33333" } };

	// 界面组件----------------------//
	private JButton showPassword = new JButton("显示密码");

	private JScrollPane scroPanel = new JScrollPane(); // 中层滚动面板
	private DefaultTableModel model; // 列表默认TableModel
	private JTable table;

	private JTextArea show = new JTextArea(6, 10);
	private JScrollPane showPanel = new JScrollPane(show); // 底层滚动面板

	// -----------------------------------------------------------//

	/**
	 * 构造方法 PasswordTable()
	 */
	public PasswordTable() {
		makeFace(); // 界面构建
		addListener(); // 添加监听
		showFace(); // 界面显示
	}

	// -----------------------------------------------------------//

	/**
	 * 方法: 界面构建 makeFace()
	 */
	private void makeFace() {
		JPanel upPanel = new JPanel();
		upPanel.add(showPassword);

		table = new JTable(model = new DefaultTableModel(data, colname) {
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		});

		// 设置密码格编辑器-------------------//
		JPasswordField passwordEdit = new JPasswordField();
		passwordEdit.setBorder(null);
		table.getColumn("密码")
				.setCellEditor(new DefaultCellEditor(passwordEdit));

		// 设置密码格表现器-------------------//
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

		scroPanel.getViewport().setBackground(Color.white);
		scroPanel.getViewport().add(table);

		JPanel downPanel = new JPanel();
		downPanel.setLayout(new BorderLayout());
		downPanel.add(showPanel);

		// 总体界面布局------------------------//
		getContentPane().add(upPanel, BorderLayout.NORTH);
		getContentPane().add(scroPanel, BorderLayout.CENTER);
		getContentPane().add(downPanel, BorderLayout.SOUTH);
	}

	/**
	 * 方法: 界面显示 showFace()
	 */
	private void showFace() {
		setTitle("JTable 密码格示例");
		setSize(500, 300);
		Toolkit tmpTK = Toolkit.getDefaultToolkit();
		Dimension dime = tmpTK.getScreenSize();

		setLocation(dime.width / 2 - 250, dime.height / 2 - 200);
		show();
	}

	/**
	 * 方法: 添加事件监听 addListener()
	 */
	private void addListener() {
		// 添加窗口关闭事件
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new JFrame().setVisible(false);
				dispose();
				System.exit(0);
			}
		});

		// 显示密码
		showPassword.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String txt = "张三 >>> " + table.getValueAt(0, 4) + "n"
						+ "李四 >>> " + table.getValueAt(1, 4) + "n" + "王五 >>> "
						+ table.getValueAt(2, 4) + "n" + "啊明 >>> "
						+ table.getValueAt(3, 4) + "n" + "珊珊 >>> "
						+ table.getValueAt(4, 4) + "n";
				show.setText(txt);
			}
		});
	}

	// ----------------------------------------------------------//

	/**
	 * 程序入口 main(String args[])
	 */
	public static void main(String args[]) {

		// 获取设置系统风格-------------------//
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
		}

		// 全局字体设置-----------------------//
		Font font1 = new Font("宋体", Font.PLAIN, 12);
		Font font2 = new Font("宋体", Font.PLAIN, 15);
		UIManager.put("Button.font", font1);
		UIManager.put("Table.font", font1);
		UIManager.put("TableHeader.font", font1);

		// 启动示例--------------------------//
		new PasswordTable();
	}
}
