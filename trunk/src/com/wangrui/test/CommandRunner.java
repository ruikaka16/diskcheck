package com.wangrui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class CommandRunner
{
    private static final Logger logger = Logger.getLogger(CommandRunner.class);
    
    private CommandRunner()
    {
        
    }
    
    /** 

    * Get remote file through scp 

    * @param host 

    * @param username 

    * @param password 

    * @param remoteFile 

    * @param localDir 

    * @throws IOException 

    */
    
    public static void scpGet(String host, String username, String password,

    String remoteFile, String localDir)
        throws IOException
    {
        
        if (logger.isDebugEnabled())
        {
            
            logger.debug("spc [" + remoteFile + "] from " + host + " to " + localDir);
            
        }
        
        Connection conn = getOpenedConnection(host, username, password);
        
        SCPClient client = new SCPClient(conn);
        
       

        
        client.get(remoteFile, localDir);
        
        conn.close();
        
    }
    
    /** 

    * Put local file to remote machine. 

    * @param host 

    * @param username 

    * @param password 

    * @param localFile 

    * @param remoteDir 

    * @throws IOException 

    */
    
    public static void scpPut(String host, String username, String password,

    String localFile, String remoteDir)
        throws IOException
    {
        
        if (logger.isDebugEnabled())
        {
            
            logger.debug("spc [" + localFile + "] to " + host + remoteDir);
            
        }
       
        Connection conn = getOpenedConnection(host, username, password);
        
//        boolean isAuthenticated = conn.authenticateWithPassword(username,
//                password);
//        if (isAuthenticated == false) {
//        	
//            System.err.println("authentication failed");
//        }
        
        SCPClient client = new SCPClient(conn);
       
        client.put(localFile, remoteDir);
        
        conn.close();
        
    }
    
    /** 

    * Run SSH command. 

    * @param host 

    * @param username 

    * @param password 

    * @param cmd 

    * @return exit status 

    * @throws IOException 

    */
    
    public static int runSSH(String host, String username, String password,

    String cmd)
        throws IOException
    {
        
        if (logger.isDebugEnabled())
        {
            
            logger.debug("running SSH cmd [" + cmd + "]");
            
        }
        
        Connection conn = getOpenedConnection(host, username, password);
        
        Session sess = conn.openSession();
        
        sess.execCommand(cmd);
        
        InputStream stdout = new StreamGobbler(sess.getStdout());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        
        while (true)
        {
            
            // attention: do not comment this block, or you will hit NullPointerException 
            
            // when you are trying to read exit status 
            
            String line = br.readLine();
            
            if (line == null)
                
                break;
            
            if (logger.isDebugEnabled())
            {
                
                logger.debug(line);
                
            }
            
        }
        
        sess.close();
        
        conn.close();
        
        return sess.getExitStatus().intValue();
        
    }
    
    /** 

    * return a opened Connection 

    * @param host 

    * @param username 

    * @param password 

    * @return 

    * @throws IOException 

    */
    
    private static Connection getOpenedConnection(String host, String username,

    String password)
        throws IOException
    {
        
        if (logger.isDebugEnabled())
        {
            
            logger.debug("connecting to " + host + " with user " + username

            + " and pwd " + password);
            
        }
        
        Connection conn = new Connection(host);
        
        conn.connect(); // make sure the connection is opened 
        
        boolean isAuthenticated = conn.authenticateWithPassword(username,

        password);
        
        if (isAuthenticated == false)
            
            throw new IOException("Authentication failed.");
        
        return conn;
        
    }
    
    /** 

    * Run local command 

    * @param cmd 

    * @return exit status 

    * @throws IOException 

    */
    
    public static int runLocal(String cmd)
        throws IOException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("running local cmd [" + cmd + "]");
        }
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(cmd);
        InputStream stdout = new StreamGobbler(p.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        while (true)
        {
            String line = br.readLine();
            if (line == null)
                break;
            if (logger.isDebugEnabled())
            {
                logger.debug(line);
            }
        }
        return p.exitValue();
    }
    
    public static void ftp(String host, String username,String password){
    	
    }
    
    public static void main(String args[]) throws IOException{
    	
    	//scpPut("168.100.101.150", "hundsun", "hundsun", "C:\\Users\\wangrui\\Desktop\\answers.docx", "/home/hundsun/");
    	runSSH("168.100.101.150", "hundsun", "hundsun","mkdir Backup ;mkdir Backup/20130422;cp -a answers.docx Backup/20130422");
//    	FTPClient ftp = new FTPClient();
//    	ftp.connect("168.100.101.150", 21);
//    	ftp.login("hundsun", "hundsun");
    	
//    	try {
//			ftp.connect("168.100.101.150", 21);
//			if (!ftp.login("hundsun", "hundsun")) {
//				System.out.println("Ftp登陆失败");
//			}else{
//				System.out.println("Ftp登陆成功");
//				//ftp.setFileType(org.apache.commons.net.ftp.FTP.LOCAL_FILE_TYPE);
//				//ftp.storeUniqueFile("answers.docx",new FileInputStream("C:\\Users\\wangrui\\Desktop\\answers.docxC:\\Users\\wangrui\\Desktop\\answers.docx"));
//			}
//			if(ftp != null && ftp.isConnected())
//				try {
//					ftp.disconnect();
//				} catch (IOException e) {
//					
//				}
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


    
    
    }
    
}
