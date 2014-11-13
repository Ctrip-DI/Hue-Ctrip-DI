package com.ctrip.di.dao.user;

/**
 * @author wu_jm
 *
 */
public class StatisticsUser {
	private int id ;
	private int user_type;
	private int counts;
	private long timestamps;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public long getTimestamps() {
		return timestamps;
	}
	public void setTimestamps(long timestamps) {
		this.timestamps = timestamps;
	}
}
