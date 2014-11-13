package com.ctrip.di.common.jmx;

import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HdfsJmxService extends AbstractJmxService {

	@Value("${HDFS_JMX_SERVER}")
	private String hdfsJmxServer;

	@Override
	protected String getJmxServer() {
		return hdfsJmxServer;
	}

	@Override
	public JSONObject getNewJmxBean() throws Exception {
		HdfsJmxBean hdfsJmxBean = new HdfsJmxBean();

		ObjectName memoryObjectName = new ObjectName("java.lang:type=Memory");
		CompositeDataSupport heapMemoryUsage = (CompositeDataSupport) mbsc
				.getAttribute(memoryObjectName, "HeapMemoryUsage");
		hdfsJmxBean.setCommitedHeapMemory((Long) heapMemoryUsage
				.get("committed"));
		hdfsJmxBean.setUsedHeapMemory((Long) heapMemoryUsage.get("used"));
		hdfsJmxBean.setTotalHeapMemory((Long) heapMemoryUsage.get("max"));

		CompositeDataSupport nonHeapMemoryUsage = (CompositeDataSupport) mbsc
				.getAttribute(memoryObjectName, "NonHeapMemoryUsage");
		hdfsJmxBean.setCommitedNonHeapMemory((Long) nonHeapMemoryUsage
				.get("committed"));
		hdfsJmxBean.setUsedNonHeapMemory((Long) nonHeapMemoryUsage.get("used"));
		hdfsJmxBean.setTotalNonHeapMemory((Long) nonHeapMemoryUsage.get("max"));

		ObjectName on = new ObjectName(
				"Hadoop:service=NameNode,name=NameNodeInfo");
		Object totalFiles = mbsc.getAttribute(on, "TotalFiles");
		if (totalFiles != null) {
			hdfsJmxBean.setUsedFileDirCount((Long) totalFiles);
		}
		Object totalBlocks = mbsc.getAttribute(on, "TotalBlocks");
		if (totalBlocks != null) {
			hdfsJmxBean.setUsedFileDirCount((Long) totalBlocks);
		}

		Object usedDfs = mbsc.getAttribute(on, "Used");
		if (usedDfs != null) {
			hdfsJmxBean.setUsedDfs((Long) usedDfs);
		}

		Object totalDfs = mbsc.getAttribute(on, "Total");
		if (totalDfs != null) {
			hdfsJmxBean.setTotalDfsCapacity((Long) totalDfs);
		}

		Object nonDfsUsedSpace = mbsc.getAttribute(on, "NonDfsUsedSpace");
		if (nonDfsUsedSpace != null) {
			hdfsJmxBean.setUsedNonDfs((Long) nonDfsUsedSpace);
		}

		Object blockPoolUsedSpace = mbsc.getAttribute(on, "BlockPoolUsedSpace");
		if (blockPoolUsedSpace != null) {
			hdfsJmxBean.setUsedBlockPool((Long) blockPoolUsedSpace);
		}

		ObjectName fson = new ObjectName(
				"Hadoop:service=NameNode,name=FSNamesystem");
		Object blockCapacity = mbsc.getAttribute(fson, "BlockCapacity");
		if (blockCapacity != null) {
			hdfsJmxBean.setTotalBlockCount((Integer) blockCapacity);
		}

		Object liveNodes = mbsc.getAttribute(on, "LiveNodes");
		if (liveNodes != null) {
			hdfsJmxBean.setLiveNodes((String) liveNodes);
		}
		Object deadNodes = mbsc.getAttribute(on, "DeadNodes");
		if (deadNodes != null) {
			hdfsJmxBean.setDeadNodes((String) deadNodes);
		}
		Object decomNodes = mbsc.getAttribute(on, "DecomNodes");
		if (decomNodes != null) {
			hdfsJmxBean.setDecomNodes((String) deadNodes);
		}

		JSONObject json = JSONObject.fromObject(hdfsJmxBean);

		return json;
	}

}
