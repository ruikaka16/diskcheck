package com.wangrui.test;

import java.io.FileInputStream;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;


public class CheckCertValid {
        public static void main(String args[]) throws Exception{
 
        	  String alias="mytest";
              String name="d:/mykeystore";
              //获取X509Certificate的对象
              //从命令行中读入需要验证的证书文件
              CertificateFactory of=CertificateFactory.getInstance("X.509");
              FileInputStream in=new FileInputStream(name);
              java.security.cert.Certificate ceof=of.generateCertificate(in);
              in.close();
              X509Certificate c = null;
			X509Certificate t=(X509Certificate)c;
 
              //获取日期
              //验证证书在某个日期是否有效，从命令行读入年月日，由此生成Date()对象。有Date类的很多设置年月日的方法已经不提倡使用，因此改用Calendar类，Calendar类也是一个工厂类，通过getInstance()方法获得对象，然后使用set()方法设置时间，最后通过其getTime()方法获得Date()对象由于Calendar类的set()方法参数是整数，因此对命令行参数读入的年月日字符串使用Integer.parseInt()方法转换为整数。由于Calendar类的set()方法设置月份时从0开始，0代表1月，11代表12月，因此命令读入的月份要减去1
              Calendar cld=Calendar.getInstance();
              int year=Integer.parseInt(args[1]);
              int month=Integer.parseInt(args[2])-1;
              int day=Integer.parseInt(args[3]);
              cld.set(year,month,day);
              Date d=cld.getTime();
              System.out.print(d);
            try{
                //检验证书
              t.checkValidity(d);
              System.out.print("OK");
            }

             //处理CertificateExpiredException异常
             //若证书在指定日期已经过期，则产生CertificateExpiredException异常，在cath语句中作相关处理
             catch(CertificateExpiredException e){
             System.out.println("Sxpired");
             System.out.println(e.getMessage());
             }
 
             //处理CertificateNorYetValidException异常
             //若证书在指定日期尚未生效，则产生CertificateNorYetValidException异常，在cath语句中作相关处理
            catch(CertificateNotYetValidException e){
            System.out.println("Too early");
            System.out.println(e.getMessage());
            }
    }
}
