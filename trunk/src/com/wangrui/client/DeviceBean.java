package com.wangrui.client;

public class DeviceBean {
	
	public String ip,username,password;

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	   public Object[] toObjectArray() {
	        return new Object[]{
	        		ip,username,password};
	} 
	

}
