package com.wangrui.client.window;

import javax.swing.table.AbstractTableModel;

import com.wangrui.client.DataValue;
import com.wangrui.client.DTO.UpdateLogDateValue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Table_Model_Updatelog extends AbstractTableModel {

	private Vector content = null;

	String[] title_name = { "升级日期", "升级系统", "升级摘要", "操作员"}; // 列名

	public Table_Model_Updatelog(ArrayList a) {
		content = new Vector();
		Vector vec = new Vector(4);
		// ArrayList al=new ArrayList();
		String s;
		// int j=0;

		for (int i = 0; i < a.size(); i++) {
			UpdateLogDateValue value = (UpdateLogDateValue) a.get(i);
			addRow(value);
			//System.out.println("======="+value.getOc_date());
		}

	}

	public Table_Model_Updatelog() {

		content = new Vector();
		Vector vec = new Vector(5);
		UpdateLogDateValue value = new UpdateLogDateValue();
		for (int i = 0; i < 6; i++) {

			addRow(value);
		}
	}

	public void addRow(UpdateLogDateValue value) {
		Vector v = new Vector(4);
		if (value.getOc_date() != null) {
			v.add(0, value.getOc_date());
		} else {
			v.add(0, "  ");
		}
		if (value.getRemark() != null) {

			v.add(1, value.getRemark());
		} else {
			v.add(1, " ");
		}
		if (value.getUpdate_content() != null) {
			v.add(2, value.getUpdate_content());
		} else {
			v.add(2, " ");
		}

		if (value.getOperator() != null) {
			v.add(3, value.getOperator());
			//System.out.println("value.getOperator()="+value.getOperator());
		} else {
			v.add(3, " ");
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
