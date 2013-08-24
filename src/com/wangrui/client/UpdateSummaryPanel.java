package com.wangrui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import oracle.jdbc.util.Login;

import com.wangrui.server.DBConnection;

public class UpdateSummaryPanel extends JDialog{

	private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    int selected_flag = 0;
    private DBConnection conn_insertUpdateLog;
    
	public UpdateSummaryPanel(){
		
		initComponents();
	}

	private void initComponents() {
		// TODO Auto-generated method stub
		 jPanel1 = new javax.swing.JPanel();
	        jTextField1 = new javax.swing.JTextField();
	        jTextField2 = new javax.swing.JTextField();
	        jRadioButton1 = new javax.swing.JRadioButton();
	        jRadioButton2 = new javax.swing.JRadioButton();
	        jRadioButton3 = new javax.swing.JRadioButton();
	        jRadioButton4 = new javax.swing.JRadioButton();
	        jRadioButton5 = new javax.swing.JRadioButton();
	        jTextField7 = new javax.swing.JTextField();
	        jTextField10 = new javax.swing.JTextField();
	        jTextField11 = new javax.swing.JTextField();
	        jButton3 = new javax.swing.JButton();
	        jButton4 = new javax.swing.JButton();
	        
	        jTextField1.setEnabled(false);
	        jTextField2.setEnabled(false);
	        jTextField7.setEnabled(false);
	        jTextField11.setEnabled(false);
	        jTextField10.setEnabled(false);

	       
	        setTitle("账户系统升级概要");

	        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("升级概要描述"));

	        jRadioButton1.setText("账户系统升级");
	       
	        jRadioButton2.setText("统一认证升级");

	        jRadioButton3.setText("转融通升级");

	        jRadioButton4.setText("非现场开户升级");

	        jRadioButton5.setText("多金融产品升级");
	        
	        

	        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jRadioButton1)
	                    .add(jRadioButton2)
	                    .add(jRadioButton3)
	                    .add(jRadioButton4)
	                    .add(jRadioButton5))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                    .add(jTextField11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
	                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                        .add(jTextField10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
	                        .add(jTextField7)
	                        .add(jTextField1)
	                        .add(jTextField2)))
	                .addContainerGap())
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel1Layout.createSequentialGroup()
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jRadioButton1))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jRadioButton2))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jRadioButton3)
	                    .add(jTextField7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jRadioButton4)
	                    .add(jTextField10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jRadioButton5)
	                    .add(jTextField11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addContainerGap())
	        );

	        jButton3.setText("取消");

	        jButton4.setText("提交");

	        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
	                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .add(layout.createSequentialGroup()
	                        .add(0, 366, Short.MAX_VALUE)
	                        .add(jButton4)
	                        .add(8, 8, 8)
	                        .add(jButton3)
	                        .addContainerGap())))
	        );

	        layout.linkSize(new java.awt.Component[] {jButton3, jButton4}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

	        layout.setVerticalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(layout.createSequentialGroup()
	                .addContainerGap()
	                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
	                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jButton3)
	                    .add(jButton4))
	                .addContainerGap())
	        );

	        jRadioButton1.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					if (jRadioButton1.isSelected()) {
						
						jTextField1.setEnabled(true);
						selected_flag = selected_flag+1;
					}else{
						selected_flag = selected_flag-1;
						jTextField1.setEnabled(false);
					}
				}
	        	
	        });
	        
	        jRadioButton2.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					if (jRadioButton2.isSelected()) {
						
						jTextField2.setEnabled(true);
						selected_flag = selected_flag+1;
					}else{
						selected_flag = selected_flag-1;
						jTextField2.setEnabled(false);
					}
				}
	        	
	        });
	        
	        jRadioButton3.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					if (jRadioButton3.isSelected()) {
						
						jTextField7.setEnabled(true);
						selected_flag = selected_flag+1;
					}else{
						selected_flag = selected_flag-1;
						jTextField7.setEnabled(false);
					}
				}
	        	
	        });
	        
	        jRadioButton4.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					if (jRadioButton4.isSelected()) {
						
						jTextField10.setEnabled(true);
						selected_flag = selected_flag+1;
					}else{
						selected_flag = selected_flag-1;
						jTextField10.setEnabled(false);
					}
				}
	        	
	        });
	        
	        jRadioButton5.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					if (jRadioButton5.isSelected()) {
						
						jTextField11.setEnabled(true);
						selected_flag = selected_flag+1;
					}else{
						selected_flag = selected_flag-1;
						jTextField11.setEnabled(false);
					}
				}
	        	
	        });

	        setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        setSize(550, 250);
	        setLocationRelativeTo(this);
			setResizable(false);

			jButton4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					insertUpdateLog();
					setVisible(false);
					UpdatePanel updatePanel = new UpdatePanel(0);
		            MainPanel.tabbedPane.addTab("账户系统升级", new ImageIcon(CollectSysConfig.filePathresult+"/items.gif"),updatePanel);
		            MainPanel.tabbedPane.setSelectedComponent(updatePanel);// 新建后默认显示新建的tab
		            MainPanel.tabbedPane.getName();
				}
			});
			
			jButton3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					//insertUpdateLog();
					dispose();
					//UpdatePanel updatePanel = new UpdatePanel(0);
		            //MainPanel.tabbedPane.addTab(MainPanel.str, new ImageIcon(CollectSysConfig.filePathresult+"/image/items.gif"),updatePanel);
		           // MainPanel.tabbedPane.setSelectedComponent(updatePanel);// 新建后默认显示新建的tab
		           // MainPanel.tabbedPane.getName();
		            
				}
			});
			
	}
	
	public boolean insertUpdateLog(String oc_date,int update_type,String update_content,String operator,String remark){
		
		try{
		conn_insertUpdateLog = new DBConnection();		
		String sql = "insert into update_log (oc_date,update_type,update_content,operator,remark,system_type) values('"+oc_date+"',"+update_type+",'"+update_content+"','"+operator+"','"+remark+"',0)";
		conn_insertUpdateLog.executeUpdate(sql);
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertUpdateLog(){
		
		if(jTextField1.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 0, jTextField1.getText(),LoginMain.userLabel.getText(), jRadioButton1.getText());
		}if(jTextField2.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 1, jTextField2.getText(),LoginMain.userLabel.getText(), jRadioButton2.getText());
		}if(jTextField7.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 2, jTextField7.getText(),LoginMain.userLabel.getText(), jRadioButton3.getText());
		}if(jTextField10.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 3, jTextField10.getText(),LoginMain.userLabel.getText(), jRadioButton4.getText());
		}if(jTextField11.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 4, jTextField11.getText(),LoginMain.userLabel.getText(), jRadioButton5.getText());
		}
	}
}
