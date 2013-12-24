package com.wangrui.test;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;

public class MyJTable {
	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Vector<String> v = new Vector<String>();
		Vector<Vector<String>> myVector = new Vector<Vector<String>>();
		try {
			FileReader fReader = new FileReader("c://22.txt");
			BufferedReader inFile = new BufferedReader(fReader);
			String input;
			String[] temp;
			// String[][] temp2 = new String[fReader.length][];
			while ((input = inFile.readLine()) != null) {
				temp = input.split(" ");
				for (int i = 0; i < temp.length; i++) {
					v.add(temp[8]);
					System.out.println(temp[8] + " added");
				}
				System.out.println("V is " + v);
				myVector.add(v);
				System.out.println("---------End of Line");
				Vector<String> columnNames = new Vector<String>();
				columnNames.addElement("Student Number");
				columnNames.addElement("Name");
				columnNames.addElement("Sex");
				columnNames.addElement("College");
				columnNames.addElement("Username");
				columnNames.addElement("Password");
				JTable table = new JTable(myVector, columnNames);
				JScrollPane scrollPane = new JScrollPane(table);
				frame.add(scrollPane, BorderLayout.CENTER);
				frame.setSize(600, 150);
				frame.setVisible(true);
			}
		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}
}
