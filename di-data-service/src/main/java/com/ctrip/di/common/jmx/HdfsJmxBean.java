package com.ctrip.di.common.jmx;

/**
 * 
 * HDFS Jmx Bean, get the information from hadoop hdfs cluster
 * @author xgliao
 *
 */
public class HdfsJmxBean {
	private long usedFileDirCount;
	private long usedBlockCount;
	private int totalBlockCount;

	private long usedHeapMemory;
	private long commitedHeapMemory;
	private long totalHeapMemory;

	private long usedNonHeapMemory;
	private long commitedNonHeapMemory;
	private long totalNonHeapMemory;

	private long totalDfsCapacity;
	private long usedDfs;
	private long usedNonDfs;

	private long usedBlockPool;

	private String liveNodes;
	private String deadNodes;
	private String decomNodes;

	public long getUsedFileDirCount() {
		return usedFileDirCount;
	}

	public void setUsedFileDirCount(long usedFileDirCount) {
		this.usedFileDirCount = usedFileDirCount;
	}

	public long getUsedBlockCount() {
		return usedBlockCount;
	}

	public void setUsedBlockCount(long usedBlockCount) {
		this.usedBlockCount = usedBlockCount;
	}

	public int getTotalBlockCount() {
		return totalBlockCount;
	}

	public void setTotalBlockCount(int totalBlockCount) {
		this.totalBlockCount = totalBlockCount;
	}

	public long getUsedHeapMemory() {
		return usedHeapMemory;
	}

	public void setUsedHeapMemory(long usedHeapMemory) {
		this.usedHeapMemory = usedHeapMemory;
	}

	public long getCommitedHeapMemory() {
		return commitedHeapMemory;
	}

	public void setCommitedHeapMemory(long commitedHeapMemory) {
		this.commitedHeapMemory = commitedHeapMemory;
	}

	public long getTotalHeapMemory() {
		return totalHeapMemory;
	}

	public void setTotalHeapMemory(long totalHeapMemory) {
		this.totalHeapMemory = totalHeapMemory;
	}

	public long getUsedNonHeapMemory() {
		return usedNonHeapMemory;
	}

	public void setUsedNonHeapMemory(long usedNonHeapMemory) {
		this.usedNonHeapMemory = usedNonHeapMemory;
	}

	public long getCommitedNonHeapMemory() {
		return commitedNonHeapMemory;
	}

	public void setCommitedNonHeapMemory(long commitedNonHeapMemory) {
		this.commitedNonHeapMemory = commitedNonHeapMemory;
	}

	public long getTotalNonHeapMemory() {
		return totalNonHeapMemory;
	}

	public void setTotalNonHeapMemory(long totalNonHeapMemory) {
		this.totalNonHeapMemory = totalNonHeapMemory;
	}

	public long getTotalDfsCapacity() {
		return totalDfsCapacity;
	}

	public void setTotalDfsCapacity(long totalDfsCapacity) {
		this.totalDfsCapacity = totalDfsCapacity;
	}

	public long getUsedDfs() {
		return usedDfs;
	}

	public void setUsedDfs(long usedDfs) {
		this.usedDfs = usedDfs;
	}

	public long getUsedNonDfs() {
		return usedNonDfs;
	}

	public void setUsedNonDfs(long usedNonDfs) {
		this.usedNonDfs = usedNonDfs;
	}

	public long getUsedBlockPool() {
		return usedBlockPool;
	}

	public void setUsedBlockPool(long usedBlockPool) {
		this.usedBlockPool = usedBlockPool;
	}

	public String getLiveNodes() {
		return liveNodes;
	}

	public void setLiveNodes(String liveNodes) {
		this.liveNodes = liveNodes;
	}

	public String getDeadNodes() {
		return deadNodes;
	}

	public void setDeadNodes(String deadNodes) {
		this.deadNodes = deadNodes;
	}

	public String getDecomNodes() {
		return decomNodes;
	}

	public void setDecomNodes(String decomNodes) {
		this.decomNodes = decomNodes;
	}

}
