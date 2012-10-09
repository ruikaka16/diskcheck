package com.wangrui.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension; 

 

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
* ����F�ˌ�JTable�ɫ�Ŀ��ƣ��ṩ�˃��׷�����
* 1.���F�˱���ЃɷN�ɫ�����Ч��
* 2.���F����һ�������ɫ���ַ������M���O���������е��ɫ
* ���ļ��cPlanetTable.java�ļ������ʹ��
* @author Sidney
* @version 1.0 (2008-1-14)
*/
public class StyleTable extends JTable 
{
    private String[] color = null; //����O�����ɫ�Ĕ��M

    public StyleTable() 
    {
        super();
    }



    public StyleTable(Object[][] rowData, Object[] columnNames, String[] color) 
    {
        super(rowData, columnNames);
        this.color = color;
        paintColorRow();
        
       // setFixColumnWidth(this);
        
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
        this.setRowSorter(sorter);
        
        this.setIntercellSpacing(new Dimension(5,5));
        
        //fitTableColumns(this);
    }

    /**
     * ����color���M�������ַ�������ʾ���ɫ���O��ĳ�е��ɫ��ע�⣬JTable�п��Ԍ����M�����w����
     * ���o�������M�����w���������O�����ɫ���H�������е��O��ԓ�����І�Ԫ����ɫ��
     */


    public void paintColorRow() 
    {
        TableColumnModel tcm = this.getColumnModel();
        for (int i = 0, n = tcm.getColumnCount(); i < n; i++) 
        {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(new RowColorRenderer());
        }
    }

    /**
     * �����O��Ϊ�̶����ȡ�//fix table column width
     *
     */
//    public void setFixColumnWidth(JTable table)
//    {
//        //this.setRowHeight(30);
//        this.setAutoResizeMode(table.AUTO_RESIZE_OFF);
//        /**/
//        //The following code can be used to fix table column width
//        TableColumnModel tcm = table.getTableHeader().getColumnModel();
//        for (int i = 0; i < tcm.getColumnCount(); i++) 
//        {
//            TableColumn tc = tcm.getColumn(i);
//            tc.setPreferredWidth(50);
//            // tc.setMinWidth(100);
//            tc.setMaxWidth(50);
//        }
//    }
    
    /**
     * �������������Ԅ��{���Ќ���//resize column width automatically
     *
     */
//    public void fitTableColumns(JTable myTable)
//    {
//         myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//         JTableHeader header = myTable.getTableHeader();
//         int rowCount = myTable.getRowCount();
//         Enumeration columns = myTable.getColumnModel().getColumns();
//         while(columns.hasMoreElements())
//         {
//             TableColumn column = (TableColumn)columns.nextElement();
//             int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
//             int width = (int)header.getDefaultRenderer().getTableCellRendererComponent
//             (myTable, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
//             for(int row = 0; row < rowCount; row++)
//             {
//                 int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent
//                 (myTable, myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
//                 width = Math.max(width, preferedWidth);
//             }
//             header.setResizingColumn(column); // ���к���Ҫ
//             column.setWidth(width+myTable.getIntercellSpacing().width);
//         }
//    }
    
    /**
     * ���x�Ȳ����춿��Ɔ�Ԫ���ɫ��ÿ�����ɫ���g������ж��xΪ�{ɫ�;Gɫ��
     *
     * @author Sidney
     *
     */


    /**
     * ���x�Ȳ���ɸ���һ��ָ���ַ������M���O�Ì����еı���ɫ
     *
     * @author Sidney
     *
     */
    private class RowColorRenderer extends DefaultTableCellRenderer 
    {
        public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) 
        {
            //��֧�Д��l���ɸ�����Ҫ�M���޸�
        	System.out.println(""+color[row].trim());
            if (color[row].trim().equals("E")) 
            {
                setBackground(Color.RED);
            } 
            else if (color[row].trim().equals("H")) 
            {
                setBackground(Color.CYAN);
            } 
            else if (color[row].trim().equals("A")) 
            {
                setBackground(Color.BLUE);
            } 
            else if (color[row].trim().equals("F")) 
            {
                setBackground(Color.ORANGE);
            } 
            else 
            {
                setBackground(Color.WHITE);
            }
   
            return super.getTableCellRendererComponent(t, value, isSelected,
                    hasFocus, row, column);
        }
    }
}

