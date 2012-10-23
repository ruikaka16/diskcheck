package com.wangrui.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class T {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JTable table = new JTable(5, 5);
		table.setRowHeight(20);
		table.setDefaultEditor(Object.class, new MyEditor());
		JScrollPane sp = new JScrollPane(table);
		JFrame f = new JFrame();
		f.getContentPane().add(sp, BorderLayout.CENTER);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}

class MyEditor extends AbstractCellEditor implements TableCellEditor {
	private JTextField tf = new JTextField();
	private JComboBox cb = new JComboBox();
	private JButton btn = new JButton("...");
	private JFileChooser jfc = null;
	private String filePath;
	private Component currentEditorComp = null;

	public MyEditor() {
		cb.addItem("Hello World - 1");
		cb.addItem("Hello World - 2");
		cb.addItem("Hello World - 3");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jfc == null) {
					jfc = new JFileChooser();
				}
				int r = jfc.showOpenDialog(btn);
				if (r == JFileChooser.APPROVE_OPTION) {
					filePath = jfc.getSelectedFile().getPath();
				}
			}
		});
	}

	public boolean isCellEditable(EventObject e) {
		if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) e;
			if (me.getClickCount() >= 2) {
				return true;
			}
			return false;
		}
		return true;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (row % 3 == 0) {
			currentEditorComp = tf;
			tf.setText(value == null ? "" : value.toString());
		} else if (row % 3 == 1) {
			currentEditorComp = cb;
			cb.setSelectedItem(value);
		} else {
			currentEditorComp = btn;
		}
		return currentEditorComp;
	}

	public Object getCellEditorValue() {
		if (currentEditorComp == tf) {
			return tf.getText();
		} else if (currentEditorComp == cb) {
			return cb.getSelectedItem();
		} else {
			return filePath;
		}
	}
}