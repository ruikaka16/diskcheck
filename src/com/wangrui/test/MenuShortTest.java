package com.wangrui.test;

import java.awt.event.KeyEvent;  

import javax.swing.JFrame;  
import javax.swing.JMenu;  
import javax.swing.JMenuBar;  
import javax.swing.JMenuItem;  
import javax.swing.KeyStroke;  
  
public class MenuShortTest {  
  
  public static void main(final String args[]) {  
    JFrame frame = new JFrame("MenuSample Example");  
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    JMenuBar menuBar = new JMenuBar();  
  
    // File Menu, F - Mnemonic  
    JMenu fileMenu = new JMenu("File");  
    fileMenu.setMnemonic(KeyEvent.VK_F);  
    menuBar.add(fileMenu);  
  
    // File->New, N - Mnemonic  
    JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);  
    fileMenu.add(newMenuItem);  
  
    // Edit->Cut, T - Mnemonic, CTRL-X - Accelerator  
    JMenuItem cutMenuItem = new JMenuItem("Cut", KeyEvent.VK_T);  
    KeyStroke ctrlXKeyStroke = KeyStroke.getKeyStroke("control X");  
    cutMenuItem.setAccelerator(ctrlXKeyStroke);  
    fileMenu.add(cutMenuItem);  
  
    frame.setJMenuBar(menuBar);  
    frame.setSize(350, 250);  
    frame.setVisible(true);  
  }  
} 