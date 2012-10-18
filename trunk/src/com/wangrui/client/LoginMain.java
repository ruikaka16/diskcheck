package com.wangrui.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.sql.ResultSet;
import javax.swing.*;

import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.wangrui.server.DBConnection;



public class LoginMain extends JFrame {

	private JLabel mainLabel, usernameLabel, passwordLabel;
	public JTextField usernameField;
	public JPasswordField passwordField;
	private JPanel mainPanel;
	private JButton loginBt;
	private DBConnection conn_login;
	private ResultSet rs_login;
	private MainPanel appPanel;


	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {

		LoginMain inst = 
			new LoginMain();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);

	}

	public LoginMain() {
		initGUI();
	}

	private void initGUI() {

		
		Image icon = (new ImageIcon("D:/Program Files/DiskCheck/disk.gif")).getImage();
		setIconImage(icon);
		mainPanel = (JPanel) this.getContentPane();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 300);

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.WHITE);

		mainLabel = new JLabel("欢迎使用磁盘信息查询系统");
		mainLabel.setFont(new Font("华文楷体", Font.CENTER_BASELINE, 24));
		mainLabel.setBounds(new Rectangle(90, 20, 300, 40));
		mainPanel.add(mainLabel);
		
		usernameLabel = new JLabel("用户名:");
		usernameLabel.setBounds(new Rectangle(160,140,100,20));
		usernameLabel.setFont(new Font("华文楷体", Font.CENTER_BASELINE, 12));
		mainPanel.add(usernameLabel);
		
		passwordLabel = new JLabel("密    码:");
		passwordLabel.setBounds(new Rectangle(160,180,100,20));
		passwordLabel.setFont(new Font("华文楷体", Font.CENTER_BASELINE, 12));
		mainPanel.add(passwordLabel);

		usernameField = new JTextField("", 10);
		usernameField.setBounds(new Rectangle(220, 140, 100, 20));
		mainPanel.add(usernameField);

		passwordField = new JPasswordField("", 10);
		passwordField.setBounds(new Rectangle(220, 180, 100, 20));
		mainPanel.add(passwordField);
		passwordField.setPreferredSize(new java.awt.Dimension(200, 24));
		
		
		loginBt = new JButton("登录");
		loginBt.setBounds(new Rectangle(200,220,60,20));
		loginBt.setFont(new Font("华文楷体", Font.CENTER_BASELINE, 12));
		loginBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(usernameField.getText().length()==0){
					
					JOptionPane.showMessageDialog(null,"请输入用户名!");
					return;
				}
				else if(passwordField.getText().length()==0){
					JOptionPane.showMessageDialog(null, "请输入密码");
					return;
				}
				try {
					//System.out.println(username.getText()+password.getText());
					login(usernameField.getText(), passwordField.getText());
									
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		loginBt.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {}			
			@Override
			public void keyReleased(KeyEvent e) {}			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				  if(e.getKeyCode() == KeyEvent.VK_ENTER){
                      //如果按下回车键
						try {
							//System.out.println(username.getText()+password.getText());
							login(usernameField.getText(), passwordField.getText());
											
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				  }
                
			}
		});
		
		usernameField.addKeyListener(new KeyListener() {		
			@Override
			public void keyTyped(KeyEvent e) {}		
			@Override
			public void keyReleased(KeyEvent e) {}			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				passwordField.requestFocus(); //实现按下回车键换行的方法
			}
		});
		
		passwordField.addKeyListener(new KeyListener() {		
			@Override
			public void keyTyped(KeyEvent e) {}		
			@Override
			public void keyReleased(KeyEvent e) {}			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				loginBt.requestFocus();//实现按下回车键换行的方法
			}
		});
		mainPanel.add(loginBt);
		add(mainPanel);

	}
	
	public void login(String username, String password) throws Exception{
		// TODO Auto-generated constructor stub
		String sql = "select * from device where username = '"+username+"' and  password = '"+password+"'";
		DBConnection conn_login = new DBConnection();
		
		rs_login = conn_login.executeQuery(sql);
		if(rs_login.next()){

			setVisible(false);
			appPanel = new MainPanel();
		rs_login.close();
			conn_login.close();
		}

		else {
			JOptionPane.showMessageDialog(null,"用户名或密码错误，请重新输入！");
		    usernameField.setText("");
		    passwordField.setText("");
		}
				
	}
	
	

}
