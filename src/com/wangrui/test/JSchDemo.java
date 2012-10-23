package com.wangrui.test;

import java.io.BufferedReader;   
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;   
import java.io.InputStreamReader;   
import java.nio.charset.Charset;   
  
import com.jcraft.jsch.Channel;   
import com.jcraft.jsch.ChannelExec;   
import com.jcraft.jsch.JSch;   
import com.jcraft.jsch.Session;   
import com.wangrui.client.CollectSysConfig;
  
public class JSchDemo   
{   
public void WriteLinuxInfo(String ip,String username,String password){
		
		String linuxCommd = "df -k | sed -e '1d;/ /!N;s/\\( \\)\\{1,\\}/\\1/g;'";  //查询命令
		String charset = "UTF-8"; 
		
		try
		{
		//利用JSch进行与Linux主机的ssh登陆及发送查询命令
		JSch jsch = new JSch();   
        Session session = jsch.getSession(username, ip, 22);   
        session.setPassword(password);   
        java.util.Properties config = new java.util.Properties();   
        config.put("StrictHostKeyChecking", "no");   
        session.setConfig(config);   
        session.connect();   
        
        Channel channel = session.openChannel("exec");   
        ((ChannelExec) channel).setCommand(linuxCommd);   
        channel.setInputStream(null);   
        ((ChannelExec) channel).setErrStream(System.err); 
        
        InputStream in = channel.getInputStream();  //获得输入流并将查询信息写入文件中 
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));   
        String buf = null; 
        
        while ((buf = reader.readLine()) != null)   
        {   
          System.out.println(buf); 
          FileWriter fw = new FileWriter("C:\\queryresult_linux", true);  
  		  BufferedWriter bw = new BufferedWriter(fw);
  		  bw.write(buf);
  		  bw.newLine();
  		  bw.flush(); //将数据更新至文件
  		  bw.close();
  		  fw.close();
        }   
        
        reader.close();   
       
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
    public static void main(String[] args) throws Exception   
    {
    	new JSchDemo().WriteLinuxInfo("10.1.0.22", "root", "xbzq2005");
    	
    }
}
       


