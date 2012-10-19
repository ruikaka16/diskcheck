package com.wangrui.client;
import java.awt.BorderLayout;
import com.wangrui.client.CollectSysConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import com.wangrui.server.DBConnection;


public class ChkResult extends JFrame{

    private DefaultTableModel tableModel;   //表格模型对象
    private JTable table;
    private JTextField aTextField,cTextField;
    private JTextField bTextField,dTextField;
    DBConnection conn_num,conn_table;
	Statement stmt, stmt1;
	ResultSet rs, rs1,rs2;
	int num; //记录条数
	int i = 0;
	Float util;
	private Object tableVales[][];

    
    public ChkResult()
    {
    	
        //界面部分
        super();
        setTitle("磁盘信息查询结果");
        setBounds(100,100,800,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String[] columnNames = {"日期","ip地址","盘符","可用空间(G)","总空间(G)","利用率(%)","类型"};   //列名
        util = Float.parseFloat(CollectSysConfig.utilresult);
        
        System.out.println(util);
        //获得表中的数据条数记入num
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
		
		 //将表中的数据显示到jtable中
		conn_table = new DBConnection();
		String sql4 = "select date,ip,deviceid,freespace,size,util,type from test.deviceDisk1 where deviceid not in ('/boot')  order by date desc ";
		rs1 = conn_table.executeQuery(sql4);
		tableVales= new String [num][8];
		try {
			while(rs1.next()){
				//System.out.println(tableVales[i][1]);
				tableVales[i][0]=rs1.getString(1);
				tableVales[i][1]=rs1.getString(2);
				tableVales[i][2]=rs1.getString(3);
				tableVales[i][3]=rs1.getString(4);
				tableVales[i][4]=rs1.getString(5);
				tableVales[i][5]=rs1.getString(6);
				tableVales[i][6]=rs1.getString(7);
				i++;			
			}
			rs1.close();
			conn_table.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		//添加数据和表到jtable中
        tableModel = new DefaultTableModel(tableVales,columnNames){
			//设置表格不可编辑
			public boolean isCellEditable(int row, int column) { 
	            return false; 
			}
		};      
        table = new JTable(tableModel);
       //设置当利用率大于60%时，记录背景设置为红色
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {   
        	public Component getTableCellRendererComponent(JTable table,  	
        			Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        		{  				
        		JTextField text = new JTextField(value.toString());  
        		//当第6列的值（利用率）>60%时设置红色
        		if (Float.parseFloat(table.getValueAt(row, 5).toString())>util) {    				
        			text.setBackground(Color.RED);  		
        			text.setForeground(Color.BLACK); 
        			} else {  			
        				text.setBackground(Color.WHITE);  	
        				//text.setForeground(Color.CYAN);    
        				}    			
        		return text;  		
        		}    		
        	});   
                //table字段居中显示  
                DefaultTableCellRenderer cellRanderer = new DefaultTableCellRenderer();  
                cellRanderer.setHorizontalAlignment(JLabel.CENTER);
                table.setDefaultRenderer(Object.class,cellRanderer);
                
                System.out.println("数据库条数："+num);
      
        JScrollPane scrollPane = new JScrollPane(table);   //支持滚动
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        //排序:
        table.setRowSorter(new TableRowSorter(tableModel));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
        table.addMouseListener(new MouseAdapter(){    //鼠标事件
            public void mouseClicked(MouseEvent e){
                int selectedRow = table.getSelectedRow(); //获得选中行索引
                Object oa = tableModel.getValueAt(selectedRow, 0);
                Object ob = tableModel.getValueAt(selectedRow, 1);
                aTextField.setText(oa.toString());  //给文本框赋值
                bTextField.setText(ob.toString());
            }
        });
        
      
        
        scrollPane.setViewportView(table);
        final JPanel panel = new JPanel();
        getContentPane().add(panel,BorderLayout.SOUTH);
       // panel.add(new JLabel("ip地址: "));
        aTextField = new JTextField("",10);
       // panel.add(aTextField);
       // panel.add(new JLabel("盘符: "));
        bTextField = new JTextField("",10);
       // panel.add(bTextField);
        
       // panel.add(new JLabel("可用空间: "));
        cTextField = new JTextField("",10);
       // panel.add(cTextField);
        
       // panel.add(new JLabel("总空间: "));
        dTextField = new JTextField("",10);
       // panel.add(dTextField);

    }
    /**
     * @param args
     */
    public static void main(String[] args) {
//        // TODO Auto-generated method stub
        
      new ChkResult();
    }

}

