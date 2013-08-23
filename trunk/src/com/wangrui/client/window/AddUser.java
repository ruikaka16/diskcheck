package com.wangrui.client.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.server.DBConnection;

public class AddUser extends JDialog{

	 private javax.swing.ButtonGroup buttonGroup1;
	    private javax.swing.JButton jButton5;
	    private javax.swing.JButton jButton6;
	    private javax.swing.JComboBox jComboBox1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel9;
	    private javax.swing.JPanel jPanel2;
	    private javax.swing.JPasswordField jPasswordField1;
	    private javax.swing.JPasswordField jPasswordField2;
	    private javax.swing.JTextField jTextField1;
	    
	public AddUser(){
		
		initComponments();
	}

	private void initComponments() {
		// TODO Auto-generated method stub
		  buttonGroup1 = new javax.swing.ButtonGroup();
	        jPanel2 = new javax.swing.JPanel();
	        jTextField1 = new javax.swing.JTextField();
	        jLabel1 = new javax.swing.JLabel();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel9 = new javax.swing.JLabel();
	        jPasswordField1 = new javax.swing.JPasswordField();
	        jPasswordField2 = new javax.swing.JPasswordField();
	        jLabel2 = new javax.swing.JLabel();
	        jComboBox1 = new javax.swing.JComboBox();
	        jButton5 = new javax.swing.JButton();
	        jButton6 = new javax.swing.JButton();
	        
	        setTitle("增加用户");

	        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("添加操作员"));

	        jLabel1.setText("操作员号：");

	        jLabel3.setText("密码：");

	        jLabel9.setText("再次输入密码：");

	        jPasswordField1.setColumns(15);

	        jPasswordField2.setColumns(15);

	        jLabel2.setText("权限：");

	        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "账户管理", "融资融券" }));

	        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
	        jPanel2.setLayout(jPanel2Layout);
	        jPanel2Layout.setHorizontalGroup(
	            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel2Layout.createSequentialGroup()
	                .add(26, 26, 26)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                    .add(jPanel2Layout.createSequentialGroup()
	                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                            .add(jLabel1)
	                            .add(jLabel3))
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
	                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
	                        .add(jLabel2)
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
	                        .add(jLabel9)
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                        .add(jPasswordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(36, Short.MAX_VALUE))
	        );
	        jPanel2Layout.setVerticalGroup(
	            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel2Layout.createSequentialGroup()
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jLabel1))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jLabel3)
	                    .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jLabel9)
	                    .add(jPasswordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jLabel2)
	                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .add(0, 18, Short.MAX_VALUE))
	        );

	        jButton5.setText("取消");
	        jButton5.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                
	            	setVisible(false);
	            }
	        });

	        jButton6.setText("提交");

	        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(layout.createSequentialGroup()
	                .addContainerGap()
	                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(layout.createSequentialGroup()
	                        .add(0, 0, Short.MAX_VALUE)
	                        .add(jButton6)
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                        .add(jButton5)
	                        .add(14, 14, 14))
	                    .add(layout.createSequentialGroup()
	                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                        .addContainerGap(19, Short.MAX_VALUE))))
	        );

	        layout.linkSize(new java.awt.Component[] {jButton5, jButton6}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

	        layout.setVerticalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jButton5)
	                    .add(jButton6))
	                .addContainerGap())
	        );
	        
	        ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/user_add.png");//图标路径
	        setIconImage(icon.getImage());
	        
	        setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        setSize(285, 230);
	        setLocationRelativeTo(this);
			setResizable(false);
			
			//两次密码输入验证
			jPasswordField2.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					if(!jPasswordField2.getText().equals(jPasswordField1.getText())){
						JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入！");
						jPasswordField1.setText("");
						jPasswordField2.setText("");
						jPasswordField1.requestFocus();
					}
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {}
			});
			
			jTextField1.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					if(validUserId(jTextField1.getText())){
						JOptionPane.showMessageDialog(null, "该操作员号已被占用");
						jTextField1.setText("");
						jTextField1.requestFocus();
					}else{
						return;
					}
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {}
			});
			
			jButton6.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(jTextField1.getText().equals("")||jPasswordField1.getText().equals("")||jPasswordField2.getText().equals("")||jComboBox1.getSelectedItem().toString().equals("")){
						JOptionPane.showMessageDialog(null, "信息输入不完整，请重新输入！");
					}else{
						if(insertUser(jPasswordField2.getText(),jTextField1.getText())){
							 JOptionPane.showMessageDialog(null, "操作员添加成功！");
							 setVisible(false);
							}else{
								JOptionPane.showMessageDialog(null, "操作员添加失败！");
							}
					}
					
				}
			});
	}
	public boolean validUserId(String userid){
		try{
			int i=0;
			String sql = "select count(*) from test.user where name = '"+userid+"'  ";
			System.out.println(sql);
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			while(rs_vaildInsert.next()){
				//System.out.println("i="+i);
				if(rs_vaildInsert.getString("count(*)").equals("0")){
					//JOptionPane.showMessageDialog(null, "该操作员已被占用");
					return false;
				}else{
					return true;
				}				
			}	return false;
		}catch(Exception e){
			
			e.printStackTrace();
			return true;
		}
	}
public Boolean insertUser(String password,String username){
		
	
		DBConnection insertUser = new DBConnection();
		String sql = "insert into user (name,password,openday,status,username,classes) values('"+username+"',password('" + password+ "'),now(),0,'"+jComboBox1.getSelectedItem()+"',case when "+jComboBox1.getSelectedItem().equals("账户管理")+" then 0 else 1 end);";
		System.out.println(sql);
		try{
			insertUser.executeUpdate(sql);

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
