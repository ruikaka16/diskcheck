package com.wangrui.client.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.LoginMain;
import com.wangrui.client.MainPanel;
import com.wangrui.server.DBConnection;

public class ModifedPswd extends JDialog{

	 private javax.swing.ButtonGroup buttonGroup1;
	    private javax.swing.JButton jButton5;
	    private javax.swing.JButton jButton6;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel10;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel9;
	    private javax.swing.JPanel jPanel2;
	    private javax.swing.JTextField jTextField1;
	    private javax.swing.JPasswordField jPasswordField2;
	    private javax.swing.JPasswordField jPasswordField3;
	    private javax.swing.JPasswordField jPasswordField4;
	    private FocusListener focuslistener;
	    
	public ModifedPswd(){
		
		initCompoments();
	}

	private void initCompoments() {
		// TODO Auto-generated method stub
		focuslistener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(vaildPswd(jTextField1,jPasswordField2.getText())){
					JOptionPane.showMessageDialog(null, "原密码输入错误！");
					jPasswordField2.setText("");
					jPasswordField2.requestFocus();
					return;
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jPasswordField4 = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        //jButton6 = new javax.swing.JButton();

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("修改密码");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("基本信息"));

        jLabel1.setText("操作员号：");

        jLabel3.setText("原密码：");

        jLabel9.setText("新密码：");

        jLabel10.setText("再次输入新密码：");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(52, 52, 52)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel3)
                    .add(jLabel9)
                    .add(jLabel10))
                .add(18, 18, 18)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .add(jPasswordField2)
                    .add(jPasswordField3)
                    .add(jPasswordField4))
                .addContainerGap(40, Short.MAX_VALUE))
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
                    .add(jPasswordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jPasswordField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jPasswordField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10))
                .add(0, 15, Short.MAX_VALUE))
        );

        jButton6.setText("提交");

        //jButton6.setText("提交");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                       .add(0, 0, Short.MAX_VALUE)
                       // .add(jButton6)
                       // .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton6)
                        .add(14, 14, 14))
                    .add(layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        layout.linkSize(new java.awt.Component[] {jButton6}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 59, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton6))
                    //.add(jButton6))
                .addContainerGap())
        );

        ImageIcon icon=new ImageIcon(LoginMain.app_path+"/image/application_key.png");//图标路径
        setIconImage(icon.getImage());
        
        setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(340, 230);
        setLocationRelativeTo(this);
		setResizable(false);
		
addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				//System.out.println("窗口打开");
				jPasswordField2.addFocusListener(focuslistener);
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				
				if(jPasswordField2.isFocusable()){
					dispose();
					//System.out.println("有聚焦");
					jPasswordField2.removeFocusListener(focuslistener);

				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
	
		
		jTextField1.setText(LoginMain.userLabel.getText());
		jTextField1.setEnabled(false);
		
	
		
		//两次密码输入验证
		jPasswordField4.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(!jPasswordField4.getText().equals(jPasswordField3.getText())){
					JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入！");
					jPasswordField3.setText("");
					jPasswordField4.setText("");
					jPasswordField3.requestFocus();
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
		
		jButton6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();			  
			}
		});
		
		jButton6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(jPasswordField2.getText().equals("")||jPasswordField3.getText().equals("")||jPasswordField4.getText().equals("")){
					JOptionPane.showMessageDialog(null, "信息输入不完整，请重新输入！");
				}else {
				if(!updatePsw(jPasswordField4.getText().toString(),jTextField1.getText())){
					JOptionPane.showMessageDialog(null, "密码修改失败！");
					setVisible(false);
				}else{
					JOptionPane.showMessageDialog(null, "密码修改成功,请重新登录！");
				 }
				}
			}
		});
		
	}
	
	public boolean vaildPswd(JTextField jTextField,String password){
		
		try{
			int i=0;
			String sql = "select count(*) from test.user where name = '"+jTextField.getText()+"' and password = password('"+password+"') ";
			//System.out.println(sql);
			DBConnection conn_vaildInsert = new DBConnection();
			ResultSet rs_vaildInsert = conn_vaildInsert.executeQuery(sql);
			while(rs_vaildInsert.next()){
				//System.out.println("i="+i);
				if(rs_vaildInsert.getString("count(*)").equals("0")){
					
					return true;
				}else{
					return false;
				}				
			}	return true;
		}catch(Exception e){
			
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updatePsw(String password,String username){
		
		DBConnection conn_updatepswd = new DBConnection();
		String sql = "update user set password = password('" + password
				+ "') where name = " + username + "";
		try{
		conn_updatepswd.executeUpdate(sql);
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
