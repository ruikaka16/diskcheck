package com.wangrui.client;

import java.util.ArrayList;
import java.util.List;

import com.wangrui.test.Student;

public class ChkResultBean {
	 public static List<ChkResultBean> chkResultBean=new ArrayList<ChkResultBean>();
	 public ChkResultBean(){}
	 public ChkResultBean(String date,String ip,String deviceid,String freespaces,String size,String util,String type){
	  super();
	  this.date=date;
	  this.ip=ip;
	  this.deviceid=deviceid;
	  this.freespaces=freespaces;
	  this.size=size;
	  this.util=util;
	  this.type=type;
	 }
	
	public String date,ip,deviceid,freespaces,size,util,type;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getFreespaces() {
		return freespaces;
	}

	public void setFreespaces(String freespaces) {
		this.freespaces = freespaces;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUtil() {
		return util;
	}

	public void setUtil(String util) {
		this.util = util;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
