package com.ctrip.di.dao;

/**
 * Yarn jobs data object
 * @author xgliao
 *
 */
public class YarnJobsDo {
	private long id;
	private long startTime;
	private long finishTime;
	private String jobId;
	private String queue;
	private String user;
	private String status;
	private int mapsTotal;
	private int reducesTotal;
	private String dateStr;
	
	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getMapsTotal() {
		return mapsTotal;
	}

	public void setMapsTotal(int mapsTotal) {
		this.mapsTotal = mapsTotal;
	}

	public int getReducesTotal() {
		return reducesTotal;
	}

	public void setReducesTotal(int reducesTotal) {
		this.reducesTotal = reducesTotal;
	}

}
