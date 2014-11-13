package com.ctrip.di.dao;

/**
 * Yarn job count by user
 * @author xgliao
 *
 */
public class YarnJobUserCountDo extends YarnJobCountDo {

	private String user;
	private int mapCount;
	private int reduceCount;
	private long executionTime;

	public int getMapCount() {
		return mapCount;
	}

	public void setMapCount(int mapCount) {
		this.mapCount = mapCount;
	}

	public int getReduceCount() {
		return reduceCount;
	}

	public void setReduceCount(int reduceCount) {
		this.reduceCount = reduceCount;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
