package com.wangrui.client.DTO;

import java.io.Serializable;

public class UpdateDevice implements Serializable {
	
	public int id;
	public String ip;
	public String username;
	public String password;
	public int backup_flag;
	public int updatedir_flage;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getBackup_flag() {
		return backup_flag;
	}
	public void setBackup_flag(int backup_flag) {
		this.backup_flag = backup_flag;
	}
	public int getUpdatedir_flage() {
		return updatedir_flage;
	}
	public void setUpdatedir_flage(int updatedir_flage) {
		this.updatedir_flage = updatedir_flage;
	}

	
}
