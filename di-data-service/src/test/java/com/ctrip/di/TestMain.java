package com.ctrip.di;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

public class TestMain {

	public void test() throws IOException {
		Configuration conf = new Configuration();
		conf.addResource("conf/hdfs/test-hdfs-client-conf.xml");

		System.setProperty("HADOOP_USER_NAME", "hdfs");
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(conf);

		DatanodeInfo[] dataNodeStatus = fs.getDataNodeStats();
		for (DatanodeInfo dninfo : dataNodeStatus) {
			System.out.println(dninfo.getHostName() + ", Is Decommission:"
					+ dninfo.isDecommissioned());
			System.out.println(dninfo.getHostName() + ", Dump Data node:"
					+ dninfo.dumpDatanode());
		}
		System.out.println("Default block size:" + fs.getDefaultBlockSize());
		ContentSummary contentSummary = fs.getContentSummary(new Path("/"));
		System.out.println("Content Summary:" + contentSummary);

		FsStatus fsstatus = fs.getStatus();

		System.out.println(fsstatus.getCapacity());
	}

	public static void main(String[] args) throws IOException {
		TestMain test = new TestMain();
		test.test();
	}

}
