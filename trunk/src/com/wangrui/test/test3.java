package com.wangrui.test;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
  
class StreamDrainerThread implements Runnable  
{  
    private InputStream ins;  
  
    public static String environmentValue;  
  
    public StreamDrainerThread(InputStream ins)  
    {  
        this.ins = ins;  
    }  
  
    public void run()  
    {  
        try  
        {  
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));  
            String line = null;  
            while ((line = reader.readLine()) != null)  
            {  
                if (!line.trim().equals(""))  
                {  
                    environmentValue = line;  
                }  
            }  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
}  
  
public class test3  
{  
    public static void main(String[] args) throws IOException  
    {  
        String[] cmd = new String[]  
        { "cmd.exe", "/C", "wmic ENVIRONMENT where \"name=\'framework_home\'\" get VariableValue" };  
        try  
        {  
            Process process = Runtime.getRuntime().exec(cmd);  
            StreamDrainerThread streamDrainer = new StreamDrainerThread(process.getInputStream());  
            new Thread(streamDrainer).start();  
            process.getOutputStream().close();  
            process.waitFor();  
            System.out.println("Environment Parameter OATS_HOME is " + StreamDrainerThread.environmentValue);  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
} 