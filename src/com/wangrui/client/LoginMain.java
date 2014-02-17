package com.wangrui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.print.attribute.AttributeSetUtilities;
import javax.swing.*;


import com.sun.awt.AWTUtilities;
import com.wangrui.dbf.ReadHQ;
import com.wangrui.server.DBConnection;

public class LoginMain extends JFrame {

	public static JLabel mainLabel, usernameLabel, passwordLabel,userType,userLabel,userName,coypRight;
	public JTextField usernameField;
	public JPasswordField passwordField;
	private JPanel mainPanel;
	private BasicButton loginBt,exitBt;
	private DBConnection conn_login;
	private ResultSet rs_login;
	private MainPanel appPanel;
	private ReadHQ readHQ;
	private Image background;  //背景图片
	static String RIGHTINFO = "V3.9.3.25"; //版本信息
	static int count_device=0;

	/**
	 * Auto-generated main method to display this JFrame
	 * 
	 */
	public static void main(String[] args) {
		//Windows风格界面
		try{     
			//JFrame.setDefaultLookAndFeelDecorated(true); //加上此语句连同窗体外框也改变
			//JDialog.setDefaultLookAndFeelDecorated(true); //加上此语句会使弹出的对话框也改变
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  
			// UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
			
			 UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
			}catch(Exception e){       
				e.printStackTrace(); 
				}	
		// 从systemconfig表中去参数配置
	    final CollectSysConfig collectSysConfig = new CollectSysConfig();
	    //在log文件中建立当天日期的文件夹
	    File file = new File(CollectSysConfig.filePathresult+"/log/"+getSystemDate());
	    file.mkdirs();
	    
		LoginMain inst = new LoginMain();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);

		
	}

	public LoginMain(){
		initGUI();
	}
	
	public static String getSystemDate(){
		
		Date nowTime=new Date();
		SimpleDateFormat matter=new SimpleDateFormat("yyyyMMdd"); 
		String systemdate = matter.format(nowTime);
		//System.out.println(systemdate);
		return systemdate;
	}

	private void initGUI() {

		Image icon = (new ImageIcon(CollectSysConfig.filePathresult+"/image/title.gif")).getImage();
		setIconImage(icon);
		mainPanel = (JPanel) this.getContentPane();

		
		/**20130801  设置JFrame圆角**/
		this.setUndecorated(true); //设置角前必须设置这个属性
		Shape shape = null;  
		shape = new RoundRectangle2D.Double(0, 0, 460, 300,15D, 15D); 
       
        //JFrame增加圆角属性
		AWTUtilities.setWindowShape(this, shape);
		//增加窗体透明属性
		AWTUtilities.setWindowOpacity(this, 0.95F);  
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(460, 300));
		this.setTitle("欢迎使用运维管理程序");

		//增加登陆窗口背景图片
		mainPanel = new JPanel() {  
            @Override  
            protected void paintComponent(Graphics g) {  
                ImageIcon icon = new ImageIcon(CollectSysConfig.filePathresult+"/image/background.jpg"); 
               // System.out.println(CollectSysConfig.filePathresult+"/background.jpg");
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());  
           }  
        };  
		mainPanel.setLayout(null);

		ImageIcon userIcon=new ImageIcon(CollectSysConfig.filePathresult+"/image/user.gif");//图标路径
		ImageIcon pswIcon=new ImageIcon(CollectSysConfig.filePathresult+"/image/psw.gif");//图标路径	        
	        
		mainLabel = new JLabel("欢迎使用运维管理程序");
		mainLabel.setFont(new Font("仿宋", Font.CENTER_BASELINE, 28));
		mainLabel.setBounds(new Rectangle(80, 35, 400, 40));
		//mainPanel.add(mainLabel);
		
		
		usernameLabel = new JLabel("用户名:",userIcon,SwingConstants.RIGHT);
		usernameLabel.setBounds(new Rectangle(100,160,100,20));
		usernameLabel.setFont(new Font("华文琥珀", Font.CENTER_BASELINE, 12));
		usernameLabel.setHorizontalTextPosition(JLabel.LEFT); 
		mainPanel.add(usernameLabel);
		
		passwordLabel = new JLabel("密  码:",pswIcon,SwingConstants.RIGHT);
		passwordLabel.setBounds(new Rectangle(100,200,100,20));
		passwordLabel.setFont(new Font("华文琥珀", Font.CENTER_BASELINE, 12));
		passwordLabel.setHorizontalTextPosition(JLabel.LEFT); 
		mainPanel.add(passwordLabel);

		usernameField = new JTextField("", 10);
		usernameField.setBounds(new Rectangle(220, 160, 100, 20));
		mainPanel.add(usernameField);

		passwordField = new JPasswordField("", 10);
		passwordField.setBounds(new Rectangle(220, 200, 100, 20));
		mainPanel.add(passwordField);
		passwordField.setPreferredSize(new java.awt.Dimension(200, 24));
		
		coypRight = new JLabel(RIGHTINFO);
		coypRight.setBounds(new Rectangle(400, 270, 60, 20));
		mainPanel.add(coypRight);
		
		userType = new JLabel();
		userName = new JLabel();
		userLabel = new JLabel();
		
		loginBt = new BasicButton("登录");
		loginBt.setBounds(new Rectangle(155,260,60,20));
		
		exitBt = new BasicButton("退出");
		exitBt.setBounds(new Rectangle(250,260,60,20));
		
		loginBt.setFont(new Font("仿宋", Font.CENTER_BASELINE, 12));
		exitBt.setFont(new Font("仿宋", Font.CENTER_BASELINE, 12));
		
		loginBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(usernameField.getText().length()==0){
					
					JOptionPane.showMessageDialog(null,"请输入用户名!","",JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(passwordField.getText().length()==0){
					JOptionPane.showMessageDialog(null, "请输入密码","",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					//System.out.println(username.getText()+password.getText());
					//System.out.println(usernameField.getText()+" and "+passwordField.getText());
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
		
		exitBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
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
		mainPanel.add(exitBt);
		add(mainPanel);

	}

	public void login(String username, String password) throws Exception{
		// TODO Auto-generated constructor stub
		String sql = "select * from user where name = '"+username+"' and  password = password('"+password+"')";
		//System.out.println("登陆sql="+sql);
		DBConnection conn_login = new DBConnection();
		
		rs_login = conn_login.executeQuery(sql);
		if(rs_login.next()){

			userLabel = new JLabel();
			userType = new JLabel();
			userLabel.setText(rs_login.getString("name"));
			userType.setText(rs_login.getString("classes"));
			userName.setText(rs_login.getString("username"));
			setVisible(false);
			appPanel = new MainPanel();
			//readHQ = new ReadHQ(); 
		rs_login.close();
			conn_login.close();
		}

		else {
			JOptionPane.showMessageDialog(null,"用户名或密码错误，请重新输入！","",JOptionPane.ERROR_MESSAGE);
		    usernameField.setText("");
		    passwordField.setText("");
		    usernameField.requestFocus();
		}
				
	}
}
