package com.wangrui.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.wangrui.server.ConnectUtil;
import com.wangrui.server.DBConnection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class UpdateReport extends JFrame {
	
	private DBConnection conn_getUpdateSummary;
	private ResultSet rs_updateSummary; 

	public UpdateReport(int system_type) {

		// TODO Auto-generated method stub
		setSize(1024,700);
		setTitle("报告");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JEditorPane editorPane = new JEditorPane();

		// 放到滚动窗格中才能滚动查看所有内容
		JScrollPane scrollPane = new JScrollPane(editorPane);
		// 设置是显示网页 html 文件,可选项
		editorPane.setContentType("text/html");
		// 设置成只读，如果是可编辑，你会看到显示的样子也是不一样的，true显示界面
		editorPane.setEditable(false);
		// 要能响应网页中的链接，则必须加上超链监听器
		// editorPane.addHyperlinkListener(this);
		String path = "file:///"+CollectSysConfig.filePathresult+"/report/"+MainPanel.str+"文件报告.html";
		System.out.println(path);
		try {
			if(system_type==0){
				System.out.println("-----取账户系统升级报告----");
			getCompileReport(0);
			
			}else if(system_type==1){
				System.out.println("-----取融资融券系统升级报告----");
				getCompileReport(1);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "报表生成失败！"+e1);
		}
		try {
			editorPane.setPage(path);
		} catch (IOException e) {
			System.out.println("读取页面 " + path + " 出错. " + e.getMessage());
			JOptionPane.showMessageDialog(null, e);
		}
		Container container = getContentPane();

		// 让editorPane总是填满整个窗体
		container.add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(this);
		setResizable(true);
	}
	
	public void getCompileReport(int system_type)throws IOException{

		try {
		     
			Connection conn = null;
			//DBConnection conn1 = null;
			conn = ConnectUtil.getConnection();
			JasperReport jasperReport = null;  
		        JasperPrint jasperPrint = null;  
//		      jasperReport = JasperCompileManager.compileReport(fileName);//编译jrxml文件  
		        InputStream inputStream = new FileInputStream(CollectSysConfig.filePathresult+"/jasper/updateFileInfo.jasper");  
		        Map<String, Object> parameters = new HashMap<String, Object>();//需要传入报表中的参数  
				parameters.put("title",MainPanel.str+"文件报告");
				parameters.put("operator", LoginMain.userLabel.getText());
				parameters.put("current_date", LoginMain.getSystemDate());
				//if(system_type==0){
					parameters.put("summary",getUpdateSummary(0) );	
				//	System.out.println("账户系统升级概要："+getUpdateSummary(0));
				//}else if(system_type==1){
				//	parameters.put("summary", getUpdateSummary(1));	
				//	System.out.println("融资融券系统升级概要："+getUpdateSummary(1));
				//}
			
		        jasperPrint = JasperFillManager.fillReport(inputStream, parameters, conn);  
		        JasperExportManager.exportReportToHtmlFile(jasperPrint, CollectSysConfig.filePathresult+"/report/"+MainPanel.str+"文件报告.html");
		       // 
		}catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}		
	public String getUpdateSummary(int system_type){
		//String sql = "select group_concat(remark,update_content) as summary from update_log where oc_date = date_format(now(),'%Y%m%d')";
		String sql = "select group_concat(remark,update_content) as summary from update_log where oc_date = date_format(now(),'%Y%m%d') and system_type = "+system_type+"";
		String summary = "";
		conn_getUpdateSummary = new DBConnection();
		rs_updateSummary = conn_getUpdateSummary.executeQuery(sql);
		try {
			while(rs_updateSummary.next()){
				summary = rs_updateSummary.getString("summary");
				System.out.println("summary="+summary);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summary;
	
	}
}
