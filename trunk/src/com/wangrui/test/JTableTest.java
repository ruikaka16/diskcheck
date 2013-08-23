package com.wangrui.test;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.wangrui.server.DBConnection;
 
public class JTableTest extends JFrame {
 
 class TableTableModel extends DefaultTableModel {
  /**
   * 
   */
  private static final long serialVersionUID = 679265889547674796L;
  public final String[] COLUMN_NAMES = { "IP地址", "用户名", "密码" ,"备份标志","客户端升级标志","灾备标志","系统标志"}; // 20121011列增加os字段对应操作系统
   
  public TableTableModel()
  {
  }
 
  public int getColumnCount() {
   return COLUMN_NAMES.length;
  }
  public String getColumnName(int columnIndex) {
   return COLUMN_NAMES[columnIndex];
  }
  // 将Table设成只读的
  public boolean isCellEditable(int row,
                int column)
  {
   return false;
  }
 }
 
 private JTable table;
 private int i = 0;
 private int j = 0;
 private int rowI = -1;
 /**
  * Launch the application
  * @param args
  */
 public static void main(String args[]) {
  try {
   JTableTest frame = new JTableTest();
   frame.setVisible(true);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 
 /**
  * Create the frame
  */
 public JTableTest() {
  super();
  setTitle("JTable Test");
  getContentPane().setLayout(null);
  setBounds(100, 100, 500, 375);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
  final JScrollPane scrollPane = new JScrollPane();
  scrollPane.setBounds(10, 28, 460, 271);
  getContentPane().add(scrollPane);
 
  table = new JTable();
  table.setShowGrid(true);
  table.addMouseListener(new UserMouseAdapter() {
   /** *//**
      * 鼠标单击事件
      * @param e 事件源参数
      */
   public void mouseSingleClicked(MouseEvent e){
       //System.out.println("Single Clicked!");
    rowI  = table.rowAtPoint(e.getPoint());// 得到table的行号
    if (rowI > -1)
        System.out.println("单击鼠标 "+((TableTableModel)table.getModel()).getValueAt(rowI, 0));
     }
 
     /** *//**
      * 鼠标双击事件
      * @param e 事件源参数
      */
     public void mouseDoubleClicked(MouseEvent e){
       //System.out.println("Doublc Clicked!");
      rowI  = table.rowAtPoint(e.getPoint());// 得到table的行号
     if (rowI > -1)
         System.out.println("双击鼠标 "+((TableTableModel)table.getModel()).getValueAt(rowI, 0));
     }
      
  });
  
	DBConnection conn_table = new DBConnection();
	String sql4 = "select ip,username,password,backup_flag,updatedir_flag,bdb_flag,system_type from test.update_device";
	ResultSet rs1 = conn_table.executeQuery(sql4);
	Object tableVales[][];
	tableVales = new String[10][10];
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
	//tableModel = new DefaultTableModel(tableVales, COLUMN_NAMES);
	//table = new JTable(tableModel);
  table.setModel(new TableTableModel());
  scrollPane.setViewportView(table);
 }
 
}
