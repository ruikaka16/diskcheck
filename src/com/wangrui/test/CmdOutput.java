package com.wangrui.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdOutput {

	public static void main(String[] args) {
        try {
            Process pro = Runtime.getRuntime().exec("cmd /c start D:\\oracle\\batCommd.bat");
            pro.waitFor();
            
           // BufferedInputStream br = new BufferedInputStream(pro.getInputStream());
            BufferedInputStream br2 = new BufferedInputStream(pro.getErrorStream());
            int ch;
            while((ch = br2.read())!= -1){
                System.out.print((char)ch);
            } 
           
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
}