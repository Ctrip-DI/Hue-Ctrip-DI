package com.ctrip.di.hive.util;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HDFSHelper {

	@Value("${HDFS_CONFIG}")
	private String hdfs_conf;

	private static final Log LOG = LogFactory.getLog(HDFSHelper.class);

	private FileSystem fs = null;

	public FileSystem getFileSystem() {
		if (fs == null) {
			URL configUrl = HDFSHelper.class.getClassLoader().getResource(
					hdfs_conf);
			Configuration conf = new Configuration();
			conf.addResource(configUrl);

			System.setProperty("HADOOP_USER_NAME", "hdfs");
			try {
				fs = FileSystem.get(conf);
			} catch (IOException e) {
				LOG.error(e);
			}
		}

		return fs;
	}

}
