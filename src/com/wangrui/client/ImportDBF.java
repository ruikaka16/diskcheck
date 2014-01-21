package com.wangrui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.wangrui.server.DBConnection;


public class ImportDBF extends JFrame {

	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JTextField jTextField5;

	public ImportDBF() {

		initCompoments();
	}

	private void initCompoments() {
		// TODO Auto-generated method stub
		jPanel2 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jTextField5 = new javax.swing.JTextField();
		jButton5 = new javax.swing.JButton();
		jButton6 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Excel文件导入");

		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Excel文件导入 "));

		jLabel6.setText("选择Excel文件:");

		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jLabel6)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jTextField5).addContainerGap()));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(jPanel2Layout
								.createSequentialGroup()
								.add(jPanel2Layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.BASELINE)
										.add(jLabel6)
										.add(jTextField5,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(12, Short.MAX_VALUE)));

		jButton5.setText("取消");

		jButton6.setText("导入");

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(org.jdesktop.layout.GroupLayout.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.add(layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.TRAILING)
										.add(jPanel2,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.add(layout
												.createSequentialGroup()
												.add(0, 248, Short.MAX_VALUE)
												.add(jButton6)
												.addPreferredGap(
														org.jdesktop.layout.LayoutStyle.RELATED)
												.add(jButton5).add(4, 4, 4)))
								.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { jButton5, jButton6 },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jPanel2,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED, 8,
								Short.MAX_VALUE)
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.BASELINE)
								.add(jButton5).add(jButton6)).addContainerGap()));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLocation(500, 300);
		pack();

		jTextField5.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jf = new JFileChooser();
				// jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//
				// 只选择文件夹
				int flag = jf.showOpenDialog(null);
				File f = null;
				if (flag == JFileChooser.APPROVE_OPTION) {
					f = jf.getSelectedFile();
					if (!f.getName().endsWith("xls")) {
						JOptionPane.showMessageDialog(null, "请选择Excel文件");
						jTextField5.setText("");
					}else{
					System.out.println(f.getPath());
					jTextField5.setText(f.getPath());
					}
					// btnNewButton_2.setEnabled(true);
				}
			}
		});

		jButton5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

		jButton6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					System.out.println("jTextField5.getText()="+jTextField5.getText());
					importDBF(jTextField5.getText());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

		});
	}


	private void importDBF(String filePath) throws Exception {
		// TODO Auto-generated method stub
		DBConnection c = new DBConnection();
		Connection conn = null;
		PreparedStatement pst = null;
		conn = c.getConnection();

		try {
			pst = (PreparedStatement) conn
					.prepareStatement("INSERT INTO shhq (S1,S2,S3,S4,S5,S6,S7,S8,S9,S10,S11,S13,S15,S16,S17,S18,S19,S21,S22,S23,S24,S25,S26,S27,S28,S29,S30,S31,S32,S33)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			//conn1.setAutoCommit(false);

			File file = new File(filePath);
			String[][] result = getData(file, 2);
			int rowLength = result.length;
			System.out.println(result.length);
			for (int i = 0; i < rowLength; i++) {
				// 根据数据库表字段的不同可以做修改
				pst.setString(1, result[i][0]);
				pst.setString(2, result[i][1]);
				pst.setString(3, result[i][2]);
				pst.setString(4, result[i][3]);
				pst.setString(5, result[i][4]);
				pst.setString(6, result[i][5]);
				pst.setString(7, result[i][6]);
				pst.setString(8, result[i][7]);
				pst.setString(9, result[i][8]);
				pst.setString(10, result[i][9]);
				pst.setString(11, result[i][10]);
				pst.setString(12, result[i][11]);
				pst.setString(13, result[i][12]);
				pst.setString(14, result[i][13]);
				pst.setString(15, result[i][14]);
				pst.setString(16, result[i][15]);
				pst.setString(17, result[i][16]);
				pst.setString(18, result[i][17]);
				pst.setString(19, result[i][18]);
				pst.setString(20, result[i][19]);
				pst.setString(21, result[i][20]);
				pst.setString(22, result[i][21]);
				pst.setString(23, result[i][22]);
				pst.setString(24, result[i][23]);
				pst.setString(25, result[i][24]);
				pst.setString(26, result[i][25]);
				pst.setString(27, result[i][26]);
				pst.setString(28, result[i][27]);
				pst.setString(29, result[i][28]);
				pst.setString(30, result[i][29]);

				pst.addBatch(); // 事务整体添加
			}
			// 事务整体提交
			pst.executeBatch();
			//conn.commit();
			JOptionPane.showMessageDialog(null,"导入数据库成功！");
			jTextField5.setText("");
			//System.out.println("数据库写入成功");
			// return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"导入数据库失败！");
			// return false;
		} finally {
			// 关闭PreparedStatement
			if (pst != null) {
				pst.close();
				pst = null;
			}
			// 关闭Connection
			if (conn != null) {
				conn.close();
				pst = null;
			}
		}
	}
	public static String[][] getData(File file, int ignoreRows)
			throws FileNotFoundException, IOException {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file));
		// 打开HSSFWorkbook
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 头两行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd")
											.format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("0.00").format(cell
										.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y"
									: "N");
							break;
						default:
							value = "";
						}
					}
					if (columnIndex == 0 && value.trim().equals("")) {
						break;
					}
					values[columnIndex] = rightTrim(value);
					hasValue = true;
				}

				if (hasValue) {
					result.add(values);
				}
			}
		}
		in.close();
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		return returnArray;
	}

	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	public static void main(String args[]) {

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ImportDBF().setVisible(true);
			}
		});
	}
}
