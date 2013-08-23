package com.wangrui.reports;

import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.InputStream;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
import java.util.HashMap;  
import java.util.Map;  
  
import net.sf.jasperreports.engine.JRException;  
import net.sf.jasperreports.engine.JasperExportManager;  
import net.sf.jasperreports.engine.JasperFillManager;  
import net.sf.jasperreports.engine.JasperPrint;  
import net.sf.jasperreports.engine.JasperReport;  
  
public class Reports {  
  
    public static void main(String args[]) throws JRException, ClassNotFoundException, SQLException, FileNotFoundException {  
        String fileName = "R:\\wangrui\\clienttradejour.jrxml";//直接操作生成的jrxml文件  
        JasperReport jasperReport = null;  
        JasperPrint jasperPrint = null;  
//      jasperReport = JasperCompileManager.compileReport(fileName);//编译jrxml文件  
        InputStream inputStream = new FileInputStream("R:\\wangrui\\clienttradejour.jasper");  
        Map<String, Object> parameters = new HashMap<String, Object>();//需要传入报表中的参数  
        parameters.put("reportTitle", "我的");  
//      jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, getConnection());  
        jasperPrint = JasperFillManager.fillReport(inputStream, parameters, getConnection());  
        JasperExportManager.exportReportToHtmlFile(jasperPrint, "D:\\TEST.html"); //设置生成的文件类型 可以是pdf,html 
    }  
      
    public static Connection getConnection() throws ClassNotFoundException, SQLException {  
        Connection conn = null;  
        Class.forName("com.mysql.jdbc.Driver");  
        conn = DriverManager.getConnection("jdbc:mysql://168.100.8.47:3306/test", "root", "wangrui");  
        return conn;  
    }  
      
}  
