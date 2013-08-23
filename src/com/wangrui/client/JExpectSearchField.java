package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

public class JExpectSearchField extends JTextField {
	private CaretListener careLis;
	private myPopupMenu popupMenu;
	private List<Object> model;
	static Connection conn = null;
	static Statement stmt = null;
	private static ResultSet rs = null;
	static JExpectSearchField cb1;

	private boolean isCaretListener = true;

	public JExpectSearchField() {
		super(20);
		popupMenu = new myPopupMenu();
		addCaretListener(new myCaretListener(this));
		addKeyListener(new myKeyListener());
	}

	public JExpectSearchField(List<Object> model, int maxShowItemCount) {
		super(20);
		this.model = model;
		popupMenu = new myPopupMenu();
		addCaretListener(new myCaretListener(this));
		addKeyListener(new myKeyListener());
		setMaxShowItem(maxShowItemCount);
	}

	public void setMaxShowItem(int maxShowItem) {
		popupMenu.setMaxShowItem(maxShowItem);
	}

	public void setModel(List<Object> model) {
		this.model = model;
	}

	public void popupList(String key) {
		int count = popupMenu.updataPopupMenu(key);
		if (count > 0) {
			popupMenu.setPopupSize(this.getWidth(), this.getHeight() * count);
			popupMenu.show(this, 0, this.getHeight());
		} else {
			popupMenu.setVisible(false);
		}
	}

	public void setTextValue(String str) {
		isCaretListener = false;
		this.setText(str);
		isCaretListener = true;
	}

	private void setVisibleFalseAndLostFocus() {
		popupMenu.setVisible(false);
		this.transferFocus();
	}

	private class myPopupMenu extends JPopupMenu {
		private JList showList;
		private List<Object> listModel;
		private int maxShowItem = 5;   //最大显示条数

		public myPopupMenu() {
			super();
			listModel = new ArrayList<Object>(maxShowItem);
			showList = new JList();
			showList.addListSelectionListener(new myShowListSelectListener());
			showList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						setVisibleFalseAndLostFocus();
					}
				}
			});
			this.setLayout(new BorderLayout());
			this.add(showList, "Center");
			this.setFocusable(false);
		}

		public void setMaxShowItem(int m) {
			this.maxShowItem = m;
		}

		public int updataPopupMenu(String key) {
			if (model == null || maxShowItem == 0) {
				return 0;
			}
			int i = 0;
			listModel.clear();
			for (Object obj : model) {
				if (obj.toString().startsWith(key)) {
					listModel.add(obj);
					if (++i >= maxShowItem) {
						break;
					}
				}
			}
			showList.setListData(listModel.toArray());
			return listModel.size();
		}

		public void setSelectItem(int index) {
			showList.setSelectedIndex(index);
		}

		public int getItemCount() {
			return listModel.size();
		}

		public Object getSelectObj() {
			return showList.getSelectedValue();
		}
	}

	private class myCaretListener implements CaretListener {
		private JExpectSearchField myCB;
		int index = 0;

		public myCaretListener(JExpectSearchField myCB) {
			this.myCB = myCB;
		}

		public void caretUpdate(CaretEvent e) {
			if (isCaretListener && index != e.getDot()) {
				try {
					myCB.popupList(myCB.getText(0, e.getDot()));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				index = e.getDot();
			}
		}
	}

	private class myKeyListener extends KeyAdapter {
		private int index = -1;

		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_DOWN) {
				popupMenu.setVisible(true);
				index++;
				if (index >= popupMenu.getItemCount()) {
					index = 0;
				}
				popupMenu.setSelectItem(index);

			} else if (code == KeyEvent.VK_UP) {
				index--;
				if (index < 0) {
					index = popupMenu.getItemCount() - 1;
				}
				popupMenu.setSelectItem(index);
			} else if (code == KeyEvent.VK_ESCAPE) {
				popupMenu.setVisible(false);
			} else if (code == KeyEvent.VK_ENTER) {
				setVisibleFalseAndLostFocus();
			}
		}
	}

	private class myShowListSelectListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			// System.out.println(showList.getSelectedValue());
			Object o = popupMenu.getSelectObj();
			if (o != null) {
				setTextValue(o.toString());
			}
		}
	}

	public static void main(String args[]) {

		JFrame f = new JFrame("JTextField1");
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/test";
			conn = DriverManager.getConnection(url, "root", "wangrui");
			stmt = conn.createStatement();
			String sql = "SELECT hqzqdm From szhq;";
			rs = stmt.executeQuery(sql);

			List<Object> value1 = new ArrayList<Object>();
			try {
				while (rs.next()) {
					value1.add(rs.getString("hqzqdm"));

					cb1 = new JExpectSearchField(value1, 5);
				}
			} catch (Exception e) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		JPanel p1 = new JPanel();

		p1.add(cb1);


		contentPane.add(p1);
		f.setBounds(100, 100, 200, 300);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}

