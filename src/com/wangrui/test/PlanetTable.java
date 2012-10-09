package com.wangrui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

 

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
* ���ΪJTableһ�����Α��ã����F��JTable�����ɫ�O�ü����Ĵ�ӡ����
* ���ļ��cStyleTable.java�ļ������ʹ��
*/
public class PlanetTable 
{
    public static void main(String[] args) 
    {
        JFrame frame = new PlanetTableFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class PlanetTableFrame extends JFrame 
{
    private Object[][] cells = {
            { "Aercury", new Double(2440.0), new Integer(0),
              new Boolean(false) },
            { "Aenus", new Double(60520.0), new Integer(0), new Boolean(false),
              },
            { "Aarth", new Double(6378.0), new Integer(1), new Boolean(false),
              },
            { "Aars", new Double(3397.0), new Integer(0), new Boolean(false),
              },
            { "Aupiter", new Double(71492.0), new Integer(1),
              new Boolean(false)} };

    private String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous",
             };

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 200;
    
    public PlanetTableFrame() 
    {
        // ��춿���ÿһ���ɫ�Ĕ��M
        String[] color = { "H", "A", "F", "E", "W" };

        setTitle("PlanetTable");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // ���xJTable,�������Լ��Uչ��JTable��K��������O���ɫ�Ĕ��M
        final JTable table = new StyleTable(cells, columnNames, color);
         // �����@�д��a�Ɍ��F���������ɫ�����Ч��,ע���c��һ�еą^�e
         //final JTable table = new StyleTable(cells, columnNames);
        add(new JScrollPane(table), BorderLayout.CENTER);

        //���ô�ӡ       
        JButton printButton = new JButton("Print");
//        printButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent event) 
//            {
//                try {
//                        table.print();
//                } catch (java.awt.print.PrinterException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(printButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}