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
import com.wangrui.client.UpdateDeviceConfig;
import com.wangrui.server.DBConnection;

public class ModifedUpdateDevice extends JDialog{

	 private javax.swing.ButtonGroup buttonGroup1;
	    private javax.swing.JButton jButton5;
	    private javax.swing.JButton jButton6;
	    public static javax.swing.JComboBox jComboBox1;
	    public static javax.swing.JComboBox jComboBox2;
	    public static javax.swing.JComboBox jComboBox3;
	    public static javax.swing.JComboBox jComboBox4;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JLabel jLabel5;
	    private javax.swing.JLabel jLabel6;
	    private javax.swing.JLabel jLabel7;
	    private javax.swing.JLabel jLabel8;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JPanel jPanel2;
	    public static javax.swing.JPasswordField jPasswordField1;
	    public static javax.swing.JPasswordField jPasswordField2;
	    public static javax.swing.JTextField jTextField1;
	    public static javax.swing.JTextField jTextField2;
	    private DBConnection conn_vaildInsert;
	    
	    
	public ModifedUpdateDevice(){
		
		initCompoments();
		
	}

	private void initCompoments() {
		// TODO Auto-generated method stub
		 buttonGroup1 = new javax.swing.ButtonGroup();
	        jPanel2 = new javax.swing.JPanel();
	        jTextField1 = new javax.swing.JTextField();
	        jTextField2 = new javax.swing.JTextField();
	        jPasswordField1 = new javax.swing.JPasswordField("",15);
	        jPasswordField2 = new javax.swing.JPasswordField("",15);
	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jButton5 = new javax.swing.JButton();
	        jButton6 = new javax.swing.JButton();
	        jPanel1 = new javax.swing.JPanel();
	        jComboBox1 = new javax.swing.JComboBox();
	        jComboBox2 = new javax.swing.JComboBox();
	        jComboBox3 = new javax.swing.JComboBox();
	        jComboBox4 = new javax.swing.JComboBox();
	        jLabel5 = new javax.swing.JLabel();
	        jLabel6 = new javax.swing.JLabel();
	        jLabel7 = new javax.swing.JLabel();
	        jLabel8 = new javax.swing.JLabel();

	        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	        setTitle("修改升级设备信息");

	        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("基本信息"));

	        jTextField1.setText("");

	        jTextField2.setText("");

	        jPasswordField1.setText("");

	        jPasswordField2.setText("");

	        jLabel1.setText("IP地址：");

	        jLabel2.setText("用户名：");

	        jLabel3.setText("密码：");

	        jLabel4.setText("再次输入密码：");

	        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
	        jPanel2.setLayout(jPanel2Layout);
	        jPanel2Layout.setHorizontalGroup(
	            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel2Layout.createSequentialGroup()
	                .add(52, 52, 52)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jLabel1)
	                    .add(jLabel3))
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jPanel2Layout.createSequentialGroup()
	                        .add(20, 20, 20)
	                        .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                    .add(jPanel2Layout.createSequentialGroup()
	                        .add(20, 20, 20)
	                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 59, Short.MAX_VALUE)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                    .add(jPanel2Layout.createSequentialGroup()
	                        .add(jLabel4)
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                        .add(jPasswordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                    .add(jPanel2Layout.createSequentialGroup()
	                        .add(jLabel2)
	                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
	                .add(64, 64, 64))
	        );
	        jPanel2Layout.setVerticalGroup(
	            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel2Layout.createSequentialGroup()
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jLabel1)
	                    .add(jLabel2))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jPasswordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jLabel3)
	                    .add(jLabel4))
	                .add(0, 9, Short.MAX_VALUE))
	        );

	        jButton5.setText("取消");

	        jButton6.setText("提交");

	        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("标志位信息"));

	        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "","是", "否"}));

	        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "","是", "否" }));

	        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "","是", "否" }));

	        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "","账户管理", "融资融券" }));

	        jLabel5.setText("客户端：");

	        jLabel6.setText("备份标志：");

	        jLabel7.setText("灾备标志：");

	        jLabel8.setText("系统标志");

	        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel1Layout.createSequentialGroup()
	                .add(54, 54, 54)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jLabel5)
	                    .add(jLabel7))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                    .add(jComboBox3, 0, 101, Short.MAX_VALUE)
	                    .add(jComboBox1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                .add(52, 52, 52)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jLabel6)
	                    .add(jLabel8))
	                .add(31, 31, 31)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
	                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(jPanel1Layout.createSequentialGroup()
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jLabel5)
	                    .add(jLabel6))
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
	                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                    .add(jLabel7)
	                    .add(jLabel8))
	                .add(0, 24, Short.MAX_VALUE))
	        );

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
	                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
	                            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                        .addContainerGap())))
	        );

	        layout.linkSize(new java.awt.Component[] {jButton5, jButton6}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

	        layout.setVerticalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                .add(18, 18, 18)
	                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
	                    .add(jButton5)
	                    .add(jButton6))
	                .addContainerGap())
	        );
	       
	        ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/computer_edit.png");//图标路径
	        setIconImage(icon.getImage());
	        
	        setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        setSize(550, 285);
	        setLocationRelativeTo(this);
			setResizable(false);
			
			jPasswordField2.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					System.out.println("两次密码输入验证");
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

			jButton5.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
				    setVisible(false);	
				}
			});
			
			jButton6.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(vaildUpdate(jTextField2.getText(), jPasswordField2.getText(), jComboBox1.getSelectedItem().toString(), jComboBox2.getSelectedItem().toString(), jComboBox3.getSelectedItem().toString(), jComboBox4.getSelectedItem().toString(), jTextField1.getText())){
					JOptionPane.showMessageDialog(null, "信息修改成功！");
					setVisible(false);
				}
					else
						JOptionPane.showMessageDialog(null, "信息修改失败！");
				}
			});
	}
	
public boolean vaildUpdate(String username,String password,String backup_flag,String updatedir_flag,String bdb_flag,String system_type,String ip){
		
		try{
			int i=0;
			String sql = "update test.update_device set username = '"+ username+ "',password = '"+ password+ "',backup_flag = case when +"+backup_flag.equals("是")+"  then 1 else 0  end, updatedir_flag= case when +"+jComboBox1.getSelectedItem().equals("是")+" then 1 else 0 end,bdb_flag=case when +"+jComboBox3.getSelectedItem().equals("是")+" then 1 else 0 end,system_type = case when +"+jComboBox4.getSelectedItem().equals("账户管理")+" then 0 else 1 end where ip = '"+jTextField1.getText()+"'";
			conn_vaildInsert = new DBConnection();
			conn_vaildInsert.executeUpdate(sql);
				//System.out.println("i="+i);
				//JOptionPane.showMessageDialog(null, "该设备已存在,请重新输入设备ip地址！");
			conn_vaildInsert.close();
		    return true;
		}catch(Exception e){
			
			e.printStackTrace();
			return false;
		}
		
		//return null;
	}

}
