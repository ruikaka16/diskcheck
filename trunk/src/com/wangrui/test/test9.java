package com.wangrui.test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class test9 extends JApplet implements SwingConstants {

 JPanel jpane1 = new JPanel();
 JButton jbuttonOpen = new JButton("Open");
 JButton jbuttonSave = new JButton("Save");
 JButton jbuttonExit = new JButton("Exit");
 JTextArea jTextArea = new JTextArea("");
 File file = null;

 public void init() {
  Container contentPane = getContentPane();
  contentPane.setLayout(new BorderLayout());
  JToolBar jToolBar = new JToolBar("Still draggable");
  jbuttonOpen.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent e)  {
    // TODO Auto-generated method stub
    JFileChooser jc = new JFileChooser(); 
    jc.setSelectedFile(file); 
    int iResult = jc.showOpenDialog(null);
       file = jc.getSelectedFile();
    BufferedReader in=null;
    if (file != null) {
     int readbyte = 0;
     
     try {
      in = new BufferedReader(new FileReader(file.getAbsolutePath().toString()));
     } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
     }
     try {
      while((readbyte=in.read())!=-1)
      {
       jTextArea.append(String.valueOf((char) readbyte));
       
      }
      in.close();
     } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
     }
     
     showStatus("文件读取成功!");
     jc.show(false);
    }

   }

  });
  jToolBar.add(jbuttonOpen);
  
  jbuttonSave.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
     JFileChooser jc=new JFileChooser();
     jc.setSelectedFile(file); 
     int iResult=jc.showSaveDialog(null);
        file=jc.getSelectedFile();
      
     if (file!=null)
     {
      if (!file.exists())
       file=new File(jc.getSelectedFile().getAbsolutePath());
     
      FileOutputStream fileOutStream = null;
     try {
      fileOutStream = new FileOutputStream(  file );
      fileOutStream.write(jTextArea.getText().getBytes());
      
      fileOutStream.close();
     } catch ( Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      
     }
    
     }
     
     
     
   }

  }); 
  jToolBar.add(jbuttonSave); 
  
  jbuttonExit.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent e)  {
    // TODO Auto-generated method stub
     
    System.exit(0);
   }

  });
  jToolBar.add(jbuttonExit);
  jToolBar.setPreferredSize(new Dimension(200, 40));
  contentPane.add(jToolBar, "North");

  jTextArea.setRows(10);
  jTextArea.setColumns(20);
  jTextArea.setAutoscrolls(true);
  contentPane.add(new JScrollPane(jTextArea), "Center");
  show();
  
  Container contentpane = getContentPane();
 }
}


