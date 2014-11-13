package com.ctrip.di.hdfs;

/**
 * Hdfs directory summary data object
 * @author xgliao
 *
 */
public class HdfsDirSummary {
	private String user;
	private long length;
	private long fileCount;
	private long directoryCount;
	private long quota;
	private long spaceConsumed;
	private long spaceQuota;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getFileCount() {
		return fileCount;
	}

	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
	}

	public long getDirectoryCount() {
		return directoryCount;
	}

	public void setDirectoryCount(long directoryCount) {
		this.directoryCount = directoryCount;
	}

	public long getQuota() {
		return quota;
	}

	public void setQuota(long quota) {
		this.quota = quota;
	}

	public long getSpaceConsumed() {
		return spaceConsumed;
	}

	public void setSpaceConsumed(long spaceConsumed) {
		this.spaceConsumed = spaceConsumed;
	}

	public long getSpaceQuota() {
		return spaceQuota;
	}

	public void setSpaceQuota(long spaceQuota) {
		this.spaceQuota = spaceQuota;
	}

}
