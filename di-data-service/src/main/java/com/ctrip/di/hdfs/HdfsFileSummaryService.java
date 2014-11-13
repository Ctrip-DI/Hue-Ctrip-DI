package com.ctrip.di.hdfs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Hdfs file summary data Service, which provide information for hdfs directory summary 
 * and refresh the summary automatically
 * @author xgliao
 *
 */
@Service
@Singleton
public class HdfsFileSummaryService {
	private static Log logger = LogFactory.getLog(HdfsFileSummaryService.class);

	private volatile List<HdfsDirSummary> summaryList = new ArrayList<HdfsDirSummary>();

	@Value("${HDFS_CONFIG}")
	private String hdfsConfig;
	@Value("${HADOOP_USER_NAME}")
	private String hadoopUserName;

	public List<HdfsDirSummary> getContentSummaryList() {
		if (summaryList.isEmpty()) {
			try {
				start();
			} catch (IOException e) {
				logger.error("IO Exception", e);
				throw new RuntimeException("IO Exception", e);
			}

		}

		return summaryList;
	}

	@Scheduled(fixedDelay = 4 * 3600 * 1000)
	public void start() throws IOException {
		logger.info("Start Initialize Hdfs File Summary:"
				+ System.currentTimeMillis());
		synchronized (summaryList) {
			URL configUrl = HdfsFileSummaryService.class.getClassLoader()
					.getResource(hdfsConfig);
			Configuration conf = new Configuration();
			conf.addResource(configUrl);

			System.setProperty("HADOOP_USER_NAME", hadoopUserName);
			FileSystem fs = FileSystem.get(conf);

			System.setProperty("user.name","hdfs");
			List<HdfsDirSummary> summaryListTemp = new ArrayList<HdfsDirSummary>();
			for (FileStatus fileStatus : fs.listStatus(new Path("/user"))) {
				if (fileStatus.isDirectory()) {
					Path filePath = fileStatus.getPath();
					ContentSummary summary = fs.getContentSummary(filePath);
					HdfsDirSummary hdfsDirSummay = new HdfsDirSummary();
					hdfsDirSummay.setUser(filePath.getName());
					hdfsDirSummay
							.setDirectoryCount(summary.getDirectoryCount());
					hdfsDirSummay.setFileCount(summary.getFileCount());
					hdfsDirSummay.setLength(summary.getLength());
					hdfsDirSummay.setQuota(summary.getQuota());
					hdfsDirSummay.setSpaceConsumed(summary.getSpaceConsumed());
					hdfsDirSummay.setSpaceQuota(summary.getSpaceQuota());

					summaryListTemp.add(hdfsDirSummay);

				}
			}

			if (summaryListTemp.size() > 0) {
				summaryList = summaryListTemp;
				summaryListTemp = null;
			}
		}

		logger.info("End Initialize Hdfs File Summary:"
				+ System.currentTimeMillis());
	}

	public static void main(String[] args) throws IOException {
		HdfsFileSummaryService test = new HdfsFileSummaryService();
		test.start();
	}

}
