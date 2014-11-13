package com.ctrip.di.hive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ctrip.di.dao.hive.HiveJobsDo;
import com.ctrip.di.dao.hive.HiveJobsService;
import com.ctrip.di.hive.alert.EMail;
import com.ctrip.di.hive.util.HDFSHelper;
import com.ctrip.di.hive.util.HiveHelper;

@Service
public class HiveCheckService {

	private static final Log logger = LogFactory.getLog(HiveCheckService.class);

	@Value("${HIVE_CHECK_AVG_PARTITION_NUM}")
	private int hive_check_avg_partition_num;

	@Value("${ADMIN_EMAIL}")
	private String admin_email;

	@Autowired
	private HDFSHelper hdfsHelper;

	@Autowired
	private HiveHelper hiveHelper;

	@Autowired
	private HiveJobsService hiveJobsService;

	private Partition lastPartition;

	@Scheduled(fixedDelay = 1 * 3600 * 1000)
	public void run() throws Exception {
		List<HiveJobsDo> list = getAllHiveJobs();

		for (HiveJobsDo hiveJobsdao : list) {
			checkHiveJob(hiveJobsdao.getDbname(), hiveJobsdao.getTablename());
		}
	}

	List<HiveJobsDo> getAllHiveJobs() {
		List<HiveJobsDo> result = null;
		try {
			result = hiveJobsService.getAllHiveJobs();
		} catch (Exception e) {
			logger.error("get all hive job info failed", e);
		}

		return result;
	}

	/**
	 * 
	 * @param dbName
	 * @param tableName
	 * @return true if ok, otherwise false
	 */
	public boolean checkHiveJob(String dbName, String tableName) {
		logger.debug("begin to checkhivejob, table: " + dbName + "."
				+ tableName);

		try {
			HiveJobsDo hiveJobsDao = hiveJobsService.getHiveJob(dbName,
					tableName);
			if (null == hiveJobsDao) {
				logger.error("not found table: " + dbName + "." + tableName);
				return false;
			}
			logger.info(hiveJobsDao);

			if (hiveJobsDao.getCheckrate() <= 0) { // don't need to check
				return true;
			}

			long avgSize = getAvgSize(dbName, tableName,
					hive_check_avg_partition_num);
			long lastSize = 0;
			if (lastPartition != null) {
				logger.info("last partition: "
						+ lastPartition.getSd().getLocation());
				lastSize = getPathSize(lastPartition.getSd().getLocation());
			}

			logger.info("avg size of the table: " + avgSize);
			logger.info("size of the last partition of the table: " + lastSize);
			if (avgSize <= 0) {
				return false;
			}

			float rate = lastSize / (float) avgSize;
			logger.debug("rate = " + rate);

			StringBuilder content = new StringBuilder();

			if (rate < 1 - hiveJobsDao.getCheckrate()) {
				content.append("Alert: the size of result partition decrease "
						+ (1 - rate) * 100 + "%" + "<br>");
			} else if (rate > 1 + hiveJobsDao.getCheckrate()) {
				content.append("Alert: the size of result parttion increase "
						+ (rate - 1) * 100 + "%" + "<br>");
			} else {
				logger.info("the size of result parttion is OK.");
				return true;
			}

			content.append("Partition: " + lastPartition.getDbName() + "."
					+ lastPartition.getTableName() + lastPartition.getValues()
					+ "<br>");
			content.append("avg size of the table: " + avgSize + "<br>");
			content.append("size of the last partition of the table: "
					+ lastSize + "<br>");
			logger.error(content.toString());

			SendAlert(content.toString(), hiveJobsDao);

		} catch (Exception e) {
			logger.error("checkHiveJob error....", e);
		}

		return false;
	}

	private long getAvgSize(String dbName, String tableName, int period) {

		Map<String, Long> map = getPartitionPathMap(dbName, tableName, period);
		long avg = 0;
		if (null != map) {
			logger.debug("mapSize = " + map.size());

			for (Long size : map.values()) {
				if (size != null) {
					avg += size / map.size();
				}
			}
		}

		return avg;
	}

	private Map<String, Long> getPartitionPathMap(String dbName,
			String tableName, int period) {

		if (period <= 0)
			return null;

		Map<String, Long> partitionPathMap = new HashMap<String, Long>();

		List<Partition> partitionList = hiveHelper.getHiveTablePartitionList(
				dbName, tableName);
		List<Partition> lastestList = null;

		if (partitionList == null | partitionList.size() == 0) {
			return null;
		} else if (partitionList.size() == 1) {
			lastestList = partitionList;
		} else if (partitionList.size() <= period) {
			lastestList = partitionList.subList(0, partitionList.size() - 1);
		} else {
			lastestList = partitionList.subList(partitionList.size() - period
					- 1, partitionList.size() - 1);
		}

		for (Partition p : lastestList) {
			String location = p.getSd().getLocation();
			long size = getPathSize(location);
			partitionPathMap.put(location, size);
			logger.debug(location + " : " + size);
		}

		lastPartition = partitionList.get(partitionList.size() - 1);

		return partitionPathMap;
	}

	private Long getPathSize(String path) {

		try {
			FileSystem fs = hdfsHelper.getFileSystem();
			return fs.getContentSummary(new Path(path)).getLength();
		} catch (IOException e) {
			logger.error(e);
		}

		return 0L;
	}

	private void SendAlert(String content, HiveJobsDo hiveJobsDo) {
		String subject = "DI-Portal: hive table alert";
		String mails = hiveJobsDo.getEmail();

		try {
			EMail.sendMail(subject, content, mails, admin_email);
		} catch (MessagingException e) {
			logger.error("send email error:" + mails + "ignore,\n" + e);
		}
	}

	public static void main(String[] args) {

		HiveCheckService job = new HiveCheckService();
		job.checkHiveJob("test", "test2");

	}

}
