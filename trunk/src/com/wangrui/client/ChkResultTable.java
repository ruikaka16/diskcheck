package com.wangrui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.wangrui.server.DBConnection;
import com.wangrui.test.Student;
import com.wangrui.test.StudentTable;
import com.wangrui.test.StudentTableTest;

public class ChkResultTable extends JFrame implements ActionListener{
	 private JScrollPane panel;
	 private JButton next,previous,add,delete;
	 private JLabel label1;
	 private ChkResult1 table;
	 private DBConnection conn_table,conn_num;
	 private ResultSet rs1,rs;
		int num;
		int i = 0;
	 private Object tableVales[][];
	 public ChkResultTable(){
	  super("磁盘信息查询结果");
	  initTableData();
	  initComponent();
	 }
	 private void initTableData() {
		  // TODO Auto-generated method stub
		

		  
		     conn_num = new DBConnection();
			 String sql3 = "select count(*) from test.deviceDisk1 ";
			 rs = conn_num.executeQuery(sql3);
			 try {
				while(rs.next()){num = rs.getInt(1);}
				rs.close();
				conn_num.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			conn_table = new DBConnection();
			String sql4 = "select date,ip,deviceid,freespace,size,util,type from test.deviceDisk1 where deviceid not in ('/boot') and date = date_format(now(),'%Y%m%d') order by date desc ";
			rs1 = conn_table.executeQuery(sql4);
			tableVales= new String [num][8];
			try {
				while(rs1.next()){
					
					  ChkResultBean s=new ChkResultBean(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5),rs1.getString(6),rs1.getString(7));
					  ChkResultBean.chkResultBean.add(s);
			
				}
				rs1.close();
				conn_table.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 }
	 
	 private void initComponent() {
		  // TODO Auto-generated method stub
		  this.setSize(800,500);
		  table=new ChkResult1();
		  panel=new JScrollPane(table);
		  panel.setBounds(10, 10, 760, 425);  //z最后是高 倒数第二是宽
		  previous=new JButton("上一页");
		  previous.setBounds(600, 440, 75,20);
		  next=new JButton("下一页");
		  next.setBounds(690, 440, 75, 20);


		  previous.addActionListener(this);
		  next.addActionListener(this);


		  label1=new JLabel("当前"+table.currentPage+"页|总共"+table.totalRowCount+"条记录");	
		  label1.setBounds(10, 440, 150, 30);
		  this.getContentPane().setLayout(null);
		  this.getContentPane().add(panel);
		  this.getContentPane().add(previous);
		  this.getContentPane().add(next);
		  this.getContentPane().add(label1);
		 // this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		  this.setLocationRelativeTo(null);
		  this.setVisible(true);
		 }
	 
	 /**
	  * 按钮事件
	  */
	 public void actionPerformed(ActionEvent e) {
	  // TODO Auto-generated method stub
	  JButton button=(JButton) e.getSource();
	  if(button.equals(previous)){
	   int i=table.getPreviousPage();
	   if(i==-1)return;
	  }
	  if(button.equals(next)){
	   int i=table.getNextPage();
	   if(i==-1)return;
	  }
	  if(button.equals(delete)){
	   int i=table.getSelectedRow();
	   if(i==-1)return ;
	   Integer id=(Integer) table.getValueAt(i,0);
	   if(id==null)return ;
	   Student s=null;
	   for(Student stu:Student.students){
	    if(stu.getId().equals(id))
	     s=stu;
	   }
	   int index=ChkResultBean.chkResultBean.indexOf(s);
	   ChkResultBean.chkResultBean.remove(index);
	   table.initTable();

	   label1.setText("当前"+table.currentPage+"页|总共"+table.totalRowCount+"条记录");	
	   return;
	  }

	  DefaultTableModel model=new DefaultTableModel(table.getPageData(),table.columnNames);
	  table.setModel(model);
	
	  label1.setText("当前"+table.currentPage+"页|总共"+table.totalRowCount+"条记录");	
	  
//    table.setDefaultRenderer(Object.class, new TableCellRenderer() {   
//	public Component getTableCellRendererComponent(JTable table,  	
//			Object value, boolean isSelected, boolean hasFocus, int row, int column) 
//		{  				
//		JTextField text = new JTextField(value.toString());  
//		//当第6列的值（利用率）>60%时设置红色
//		if (Float.parseFloat(table.getValueAt(row, 5).toString())>2.00) {    				
//			text.setBackground(Color.RED);  		
//			text.setForeground(Color.BLACK); 
//			} else {  			
//				text.setBackground(Color.WHITE);  	
//				//text.setForeground(Color.CYAN);    
//				}    			
//		return text;  		
//		}    		
//	}); 

	 }
	 /**
	  * @param args
	  */
	 public static void main(String[] args) {
	  // TODO Auto-generated method stub
	  new ChkResultTable();
	 }


}
