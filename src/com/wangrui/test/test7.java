package com.wangrui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
public class test7 implements MouseListener {
JFrame f;
JPanel panel1;
JTable tab;
Object[][] rowData = { { "a", "a", "aa" }, { "2", "b", "bb" },
    { "3", "c", "cc" }, { "a", "a", "aa" }, { "2", "b", "bb" },
    { "3", "c", "cc" },{ "3", "c", "cc" },{ "3", "c", "cc" },{ "3", "c", "cc" } };
Object[] colName = { "num", "item1", "item2" };
public test7() {
   f = new JFrame("");
   panel1 = new JPanel();
   panel1.setPreferredSize(new Dimension(300, 200));
   tab = new JTable(rowData, colName);
   tab.addMouseListener(this);
   tab.setDefaultRenderer(Object.class, new TableRenderer());
   panel1.add(tab);
   f.add(panel1, BorderLayout.NORTH);
   f.pack();
   f.setVisible(true);
   f.setResizable(true);
}
public static void main(String[] args) {
   new test7();
}
public void mouseClicked(MouseEvent arg0) {
   // TODO Auto-generated method stub
   tab.setSelectionBackground(Color.blue);
   System.out.println(tab.getSelectedColumns().length);
}
public void mouseEntered(MouseEvent arg0) {
   // TODO Auto-generated method stub
}
public void mouseExited(MouseEvent arg0) {
   // TODO Auto-generated method stub
}
public void mousePressed(MouseEvent arg0) {
   // TODO Auto-generated method stub
}
public void mouseReleased(MouseEvent arg0) {
   // TODO Auto-generated method stub
}
}
class TableRenderer extends DefaultTableCellRenderer {
public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
   if (column > 0) {
    super.getTableCellRendererComponent(table, value, isSelected,
      hasFocus, row, column);
    return this;
   }
   for (int i = 0; i < table.getRowCount(); i++) {
    if (value.equals("a")) {//第一个属性列的值
     super.setBackground(Color.RED);
     super.getTableCellRendererComponent(table, value, isSelected,
       hasFocus, row, i);
    } else if (value.equals("2")) {
     super.setBackground(Color.BLUE);
     super.getTableCellRendererComponent(table, value, isSelected,
       hasFocus, row, i);
    } else {
     super.setBackground(Color.GREEN);
     super.getTableCellRendererComponent(table, value, isSelected,
       hasFocus, row, i);
    }
   }
   return this;
}
}