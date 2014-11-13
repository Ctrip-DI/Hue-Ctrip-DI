package com.ctrip.di.dao;

/**
 * Yarn job count by user
 * @author xgliao
 *
 */
public class YarnUserJobDo {
	private String user;
	private int totalCount;
	private int successCount;
	private int failCount;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public void init() {
		failCount = totalCount - successCount;
	}

}
