package com.wangrui.client;

import javax.swing.table.AbstractTableModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Table_Model extends AbstractTableModel {

	private Vector content = null;

	// private Vector tabs=null;

	// String[] title_name={"日期","机器名","测试开始时间","测试脚本名","测试输入数据","提示信息","是否通过"};
	String[] title_name = { "日期", "ip地址", "盘符", "可用空间(G)", "总空间(G)", "利用率(%)","类型" }; // 列名

	public Table_Model(ArrayList a) {
		content = new Vector();
		Vector vec = new Vector(7);
		// ArrayList al=new ArrayList();
		String s;
		// int j=0;

		for (int i = 0; i < a.size(); i++) {
			DataValue value = (DataValue) a.get(i);
			addRow(value);
			// System.out.println(value);
		}

	}

	public Table_Model() {

		content = new Vector();
		Vector vec = new Vector(8);
		DataValue value = new DataValue();
		for (int i = 0; i < 5; i++) {

			addRow(value);
		}
	}

	public void addRow(DataValue value) {
		Vector v = new Vector(8);
		if (value.getDate() != null) {
			v.add(0, value.getDate());
		} else {
			v.add(0, " ");
		}
		if (value.getIp() != null) {

			v.add(1, value.getIp());
		} else {
			v.add(1, " ");
		}
		if (value.getDeviceid() != null) {
			v.add(2, value.getDeviceid());
		} else {
			v.add(2, " ");
		}

		if (value.getFreespace() != null) {
			v.add(3, value.getFreespace());
			System.out.println(value.getFreespace());
		} else {
			v.add(3, " ");
		}
		if (value.getSize() != null) {
			v.add(4, value.getSize());
		} else {
			v.add(4, " ");
		}
		if (value.getUtil() != null) {
			v.add(5, value.getUtil());
		} else {
			v.add(5, " ");
		}
		if (value.getType() != null) {
			v.add(6, value.getType());
		} else {
			v.add(6, " ");
		}
		if (value.getDate() != null) {
			v.add(7, value.getDate());
			System.out.println(value.getDate());
		} else {
			v.add(7, " ");
		}
		content.add(v);
	}

	public void removeRow(int row) {
		content.remove(row);
	}

	public void removeRows(int row, int count) {
		for (int i = 0; i < count; i++) {
			if (content.size() > row) {
				content.remove(row);
			}
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		((Vector) content.get(row)).remove(col);
		((Vector) content.get(row)).add(col, value);
		this.fireTableCellUpdated(row, col);
	}

	public String getColumnName(int col) {

		return title_name[col];
	}

	public int getColumnCount() {
		return title_name.length;
	}

	public int getRowCount() {
		return content.size();
	}

	public Object getValueAt(int row, int col) {
		// Object jjj=null;
		Object ll = null;
		try {

			ll = ((Vector) content.get(row)).get(col);
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		return ll;
	}

	public Class getColumnClass(int col) {

		return getValueAt(0, col).getClass();

	}
}
