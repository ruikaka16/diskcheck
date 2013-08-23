package com.wangrui.test;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.wangrui.client.CollectSysConfig;

public class TestWriteCommd {

	static RandomAccessFile dos_vbs = null;

	
	public static void main(String args[]){
		WriteVbsCommd("168.100.8.47","wangrui"," ");
	}
	
	public static void WriteVbsCommd(String ip, String username, String password) {
		// 写入新的vbs文件
		try {
			dos_vbs = new RandomAccessFile("D:\\oracle"
					+ "\\" + "vbsCommd" + ".vbs", "rw");
			dos_vbs.seek(dos_vbs.length());

			String impSQL = "Set objWsh = CreateObject(\"WScript.Shell\")"
					+ "\r\n"
					+ "objWsh.Run \"WMIC /node:"
					+ ip
					+ " /user:"
					+ username
					+ " /password:"
					+ password
					+ " /output:"
					+ CollectSysConfig.filePathresult
					+ "\\"
					+ ip
					+ ".bak logicaldisk where drivetype=3 get DeviceID,Size,FreeSpace /format:csv\",vbhide\r\nWScript.Sleep 3000\r\n";
			System.out.println("打印vbs命令:\r\n" + impSQL);
			dos_vbs.writeBytes(new String(impSQL.getBytes(), "iso8859-1")
					+ "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // finally中将reader对象关闭　　　　　

			if (dos_vbs != null) {
				try {
					dos_vbs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
