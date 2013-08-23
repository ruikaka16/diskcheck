package com.wangrui.test;

import com.jcraft.jsch.SftpProgressMonitor;

public class MyProgressMonitor implements SftpProgressMonitor {

	private long transfered;
	@Override
	public boolean count(long count) {
		// TODO Auto-generated method stub
		 transfered = transfered + count;
	        System.out.println("Currently transferred total size: " + transfered + " bytes");
	        return true;
	}
	@Override
	public void end() {
		// TODO Auto-generated method stub
		System.out.println("Transferring done.");
	}
	@Override
	public void init(int arg0, String arg1, String arg2, long arg3) {
		// TODO Auto-generated method stub
		System.out.println("Transferring begin.");
	}

}
