package com.ctrip.di.common.jmx;

public class YarnJmxBean {
	private int appsSumitted;
	private int appsPending;
	private int appsRunning;
	private int appsCompleted;
	private int appsFailed;
	private int appsKilled;

	private String liveNodeManagers;

	public int getAppsSumitted() {
		return appsSumitted;
	}

	public void setAppsSumitted(int appsSumitted) {
		this.appsSumitted = appsSumitted;
	}

	public int getAppsPending() {
		return appsPending;
	}

	public void setAppsPending(int appsPending) {
		this.appsPending = appsPending;
	}

	public int getAppsRunning() {
		return appsRunning;
	}

	public void setAppsRunning(int appsRunning) {
		this.appsRunning = appsRunning;
	}

	public int getAppsCompleted() {
		return appsCompleted;
	}

	public void setAppsCompleted(int appsCompleted) {
		this.appsCompleted = appsCompleted;
	}

	public int getAppsFailed() {
		return appsFailed;
	}

	public void setAppsFailed(int appsFailed) {
		this.appsFailed = appsFailed;
	}

	public int getAppsKilled() {
		return appsKilled;
	}

	public void setAppsKilled(int appsKilled) {
		this.appsKilled = appsKilled;
	}

	public String getLiveNodeManagers() {
		return liveNodeManagers;
	}

	public void setLiveNodeManagers(String liveNodeManagers) {
		this.liveNodeManagers = liveNodeManagers;
	}

}
