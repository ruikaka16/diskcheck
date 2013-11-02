package com.wangrui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.wangrui.server.DBConnection;

public class UpdateSummaryRzrqPanel extends JDialog{
	
	 private javax.swing.JButton jButton3;
	    private javax.swing.JButton jButton4;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JRadioButton jRadioButton1;
	    private javax.swing.JScrollPane jScrollPane1;
	    private javax.swing.JScrollPane jScrollPane2;
	    private javax.swing.JTextArea jTextArea1;
	    private javax.swing.JTextArea jTextArea2;
	    private int selected_flag=0;
	    
	public UpdateSummaryRzrqPanel(){
		
		initComponents();
	}

	private void initComponents() {
		// TODO Auto-generated method stub
	    jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        //jScrollPane1.setViewportView(jTextArea1);

        setTitle("融资融券系统升级概要");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(" 升级概要 "));
        jPanel1.setToolTipText("");

        jRadioButton1.setText("融资融券系统");
        jRadioButton1.setActionCommand("");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setEnabled(false);
        jScrollPane2.setViewportView(jTextArea2);
        
        jRadioButton1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if (jRadioButton1.isSelected()) {
					
					jTextArea2.setEnabled(true);
					selected_flag = selected_flag+1;
				}else{
					selected_flag = selected_flag-1;
					jTextArea2.setEnabled(false);
				}
			}
        	
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jRadioButton1)
                .add(18, 18, 18)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioButton1)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
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
                        .add(0, 0, Short.MAX_VALUE)
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
				UpdatePanel updatePanel = new UpdatePanel(1);
	            MainPanel.tabbedPane.addTab("融资融券系统升级", new ImageIcon(CollectSysConfig.filePathresult+"/items.gif"),updatePanel);
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
				UpdatePanel updatePanel = new UpdatePanel(1);
	            MainPanel.tabbedPane.addTab("融资融券系统升级", new ImageIcon(CollectSysConfig.filePathresult+"/items.gif"),updatePanel);
	            MainPanel.tabbedPane.setSelectedComponent(updatePanel);// 新建后默认显示新建的tab
	            MainPanel.tabbedPane.getName();
            
			}
		});
	}
	
	public boolean insertUpdateLog(String oc_date,int update_type,String update_content,String operator,String remark){
		
		try{
		DBConnection conn_insertUpdateLogRzrq = new DBConnection();		
		String sql = "insert into update_log (oc_date,update_type,update_content,operator,remark,system_type) values('"+oc_date+"',"+update_type+",'"+update_content+"','"+operator+"','"+remark+"',1)";
		conn_insertUpdateLogRzrq.executeUpdate(sql);
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertUpdateLog(){
		
		if(jTextArea2.isEnabled()){						
			insertUpdateLog(LoginMain.getSystemDate(), 0, jTextArea2.getText(),LoginMain.userLabel.getText(), jRadioButton1.getText());
		}
	}
}
