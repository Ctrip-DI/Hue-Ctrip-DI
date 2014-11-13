package com.ctrip.di.dao.hive;

import java.util.Date;

public class HiveJobsDo {

	private String dbname;
	private String tablename;
	private String pt_format;
	private int keepdays;
	private double checkrate;
	private String username;
	private String email;
	private Date create_time;
	private Date modified_time;

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getPt_format() {
		return pt_format;
	}

	public void setPt_format(String pt_format) {
		this.pt_format = pt_format;
	}

	public int getKeepdays() {
		return keepdays;
	}

	public void setKeepdays(int keepdays) {
		this.keepdays = keepdays;
	}

	public double getCheckrate() {
		return checkrate;
	}

	public void setCheckrate(double d) {
		this.checkrate = d;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getModified_time() {
		return modified_time;
	}

	public void setModified_time(Date modified_time) {
		this.modified_time = modified_time;
	}

	@Override
	public String toString() {
		return "dbname:" + dbname + " tablename:" + tablename + " pt:"
				+ pt_format + " days:" + keepdays + " checkrate:" + checkrate
				+ " username:" + username + " email:" + email + " createtime:"
				+ create_time + " modifiedtime:" + modified_time;
	}
}
