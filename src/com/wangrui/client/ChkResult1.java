package com.wangrui.client;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.wangrui.test.Student;

public class ChkResult1 extends JTable {

	// JTable表分页信息相关变量
	public int currentPage = 1;
	public int pageCount = 20;
	public int totalPage = 0;
	public int totalRowCount = 0;
	public int column = 0;
	public int restCount;
	public Object[][] resultData;
	// JTable表信息相关变量
	public List<ChkResultBean> chkResultBean = ChkResultBean.chkResultBean;
	public String[] columnNames = { "数据日期", "IP地址", "盘符", "可用空间", "总空间", "利用率",
			"类型(1超过阀值)" };
	public DefaultTableModel model = null;

	public ChkResult1() {
		initTable();
	}

	/**
	 * 获取下一页
	 */
	public int getNextPage() {
		if (this.currentPage != this.totalPage) {
			return ++currentPage;
		}
		return -1;
	}

	/**
	 * 获取上一页
	 */
	public int getPreviousPage() {
		if (this.currentPage != 1) {
			return --currentPage;
		}
		return -1;
	}

	/**
	 * 获取最后一页
	 */
	public int getLastPage() {
		return this.totalPage;
	}

	/**
	 * 获取第一页
	 */
	public int getFirstPage() {
		return 1;
	}

	/**
	 * 获取总页数
	 */
	public int getTotolPage() {
		return this.totalPage;
	}

	/**
	 * 获取当前页
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * 获得原始数据集
	 * 
	 * @param students
	 * @return
	 */
	public Object[][] getData(List<ChkResultBean> chkResultBean) {
		if (chkResultBean.size() > 0) {
			Object[][] data = new Object[chkResultBean.size()][7];
			for (int i = 0; i < chkResultBean.size(); i++) {
				ChkResultBean s = chkResultBean.get(i);
				Object[] a = { s.getDate(), s.getIp(), s.getDeviceid(),
						s.getFreespaces(), s.getSize(), s.getUtil(),
						s.getType() };// 把List集合的数据赋给Object数组
				data[i] = a;// 把数组的值赋给二维数组的一行
			}
			return data;
		}
		return null;
	}

	/**
	 * 初始化结果集
	 * 
	 * @param data
	 */
	public void initResultData(Object[][] data) {
		if (data != null) {
			resultData = data;// 总的结果集
			column = data[0].length;// 表的列数
			totalRowCount = data.length;// 表的长度
			totalPage = totalRowCount % pageCount == 0 ? totalRowCount
					/ pageCount : totalRowCount / pageCount + 1;// 结果集的总页数
			restCount = totalRowCount % pageCount == 0 ? 20 : totalRowCount
					% pageCount;// 最后一页的数据数
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @return
	 */
	public Object[][] getPageData() {
		Object[][] currentPageData = new Object[pageCount][column];// 构造每页数据集
		if (this.getCurrentPage() < this.totalPage) {// 如果当前页数小于总页数，那么每页数目应该是规定的数pageCount
			for (int i = pageCount * (this.getCurrentPage() - 1); i < pageCount
					* (this.getCurrentPage() - 1) + pageCount; i++) {
				for (int j = 0; j < column; j++) {
					// 把结果集中对应每页的每一行数据全部赋值给当前页的每一行的每一列
					currentPageData[i % pageCount][j] = resultData[i][j];
				}
			}
		} else {
			// 在动态改变数据结果集的时候，如果当前页没有数据了，则回到前一页（一般针对最后一页而言）
			if (pageCount * (this.getCurrentPage() - 1) >= totalRowCount)
				this.currentPage--;
			for (int i = pageCount * (this.getCurrentPage() - 1); i < pageCount
					* (this.getCurrentPage() - 1) + restCount; i++) {
				for (int j = 0; j < column; j++) {
					currentPageData[i % pageCount][j] = resultData[i][j];
				}
			}
		}
		return currentPageData;
	}

	/**
	 * 初始化表格数据
	 */
	public void initTable() {
		Object[][] data = getData(chkResultBean);
		if (data != null) {
			initResultData(data);
			model = new DefaultTableModel(getPageData(), columnNames);
		} else {
			// 如果结果集中没有数据，那么就用空来代替数据集中的每一行
			Object[][] nothing = { {}, {}, {}, {}, {}, {}, {} };
			model = new DefaultTableModel(nothing, columnNames);
			totalRowCount = 0;
		}
		this.setModel(model);
		this.setRowHeight(20);
		
		  TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
	        //sorter.setSortable(1, false);
	        this.setRowSorter(sorter);
		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		setDefaultRenderer(Object.class, r);

	}

}

