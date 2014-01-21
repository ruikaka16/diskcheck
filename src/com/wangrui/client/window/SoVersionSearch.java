package com.wangrui.client.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.wangrui.client.CollectSysConfig;
import com.wangrui.client.JExpectSearchField;
import com.wangrui.server.DBConnection;

public class SoVersionSearch extends JDialog {
    
	private ResultSet rs;
    /** Creates new form Find */
    public SoVersionSearch(int system_type) {

    	ImageIcon icon=new ImageIcon(CollectSysConfig.filePathresult+"/image/magnifier.png");
        jExpectSearchField = new JExpectSearchField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new JLabel();
        jLabel1 = new JLabel();

        DBConnection conn_searchSuggest = new DBConnection();
		String sql = "select distinct file_name from update_so_version where system_type = "+system_type+" and cal_time in (select max(cal_time) from update_so_version)";
	    //String sql = "select distinct file_name from update_so_version where system_type = 0 and cal_time in (select max(cal_time) from update_so_version)";
		ResultSet rs_search = conn_searchSuggest.executeQuery(sql);

		try {
			List<Object> value = new ArrayList<Object>();
			while (rs_search.next()) {
				value.add(rs_search.getString("file_name"));
				jExpectSearchField = new JExpectSearchField(value, 50);

			}
			rs_search.close();
			conn_searchSuggest.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jLabel2.setFont(new Font("华文琥珀", Font.CENTER_BASELINE, 18));
	    jLabel2.setForeground(Color.RED);
        jLabel2.setText("版本信息");
        
		jExpectSearchField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					 DBConnection conn_search = new DBConnection();
						String sql = "select  file_version from update_so_version where cal_time in (select max(cal_time) from update_so_version) and file_name = '"+jExpectSearchField.getText()+"'";
					    //String sql = "select distinct file_name from update_so_version where system_type = 0 and cal_time in (select max(cal_time) from update_so_version)";
						ResultSet rs = conn_search.executeQuery(sql);
						try {
							while (rs.next()) {
								jLabel2.setText(rs.getString("file_version"));
							}
							
							rs.close();
							conn_search.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				
			}
		});
      jLabel1.setText("请输入so文件名：");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(jLabel1)
                .add(18, 18, 18)
               // .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jExpectSearchField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel2)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(jLabel3))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jExpectSearchField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel2))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            //.add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel1))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        setIconImage(icon.getImage());
    	setTitle("版本信息");
		setModal(true); //子窗口在父窗口上，将子窗口设置为JDialog，并设置setModal(true)
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setSize(480, 100);
	    setLocationRelativeTo(this);
		setResizable(false);
    }// </editor-fold>

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SoVersionSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SoVersionSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SoVersionSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SoVersionSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }
    
    // Variables declaration - do not modify
   // private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private JExpectSearchField jExpectSearchField;
    // End of variables declaration
    
}
