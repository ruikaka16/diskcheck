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
* 本類實現了對JTable顏色的控制，提供了兩套方案：
* 1.實現了表格行兩種顏色交替的效果
* 2.實現了用一個控制顏色的字符串數組來設置所對應行的顏色
* 本文件與PlanetTable.java文件相配合使用
* @author Sidney
* @version 1.0 (2008-1-14)
*/
public class StyleTable extends JTable 
{
    private String[] color = null; //用於設定行顏色的數組

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
     * 根據color數組中相應字符串所表示的顏色來設置某行的顏色，注意，JTable中可以對列進行整體操作
     * 而無法對行進行整體操作，故設置行顏色實際上是逐列地設置該行所有單元格的顏色。
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
     * 將列設置为固定寬度。//fix table column width
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
     * 根據數據內容自動調整列寬。//resize column width automatically
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
//             header.setResizingColumn(column); // 此行很重要
//             column.setWidth(width+myTable.getIntercellSpacing().width);
//         }
//    }
    
    /**
     * 定義內部類，用於控制單元格顏色，每兩行顏色相間，本類中定義为藍色和綠色。
     *
     * @author Sidney
     *
     */


    /**
     * 定義內部類，可根據一個指定字符串數組來設置對應行的背景色
     *
     * @author Sidney
     *
     */
    private class RowColorRenderer extends DefaultTableCellRenderer 
    {
        public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) 
        {
            //分支判斷條件可根據需要進行修改
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

