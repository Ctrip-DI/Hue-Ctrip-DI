package com.ctrip.di.hive;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.thrift.TException;
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
public class HivePartitionCleanService {
	private static final Log logger = LogFactory
			.getLog(HivePartitionCleanService.class);

	private static String BACKUP = "/tmp/backup/";

	@Value("${REMOVE_PARTITION_NUM_ALERT_THRESHOLD}")
	private int remove_partition_num_alert_threshold;

	@Value("${ADMIN_EMAIL}")
	private String admin_email;

	@Autowired
	private HDFSHelper hdfsHelper;

	@Autowired
	private HiveHelper hiveHelper;

	@Autowired
	private HiveJobsService hiveJobsService;
	
	@Scheduled(fixedDelay = 1 * 3600 * 1000)
	public void start() throws Exception {
		List<HiveJobsDo> list = hiveJobsService.getAllHiveJobs();

		for (HiveJobsDo hiveJobsDao : list) {
			doOneHiveTable(hiveJobsDao);
		}
	}

	public void doOneHiveTable(HiveJobsDo hiveJobsDo) {
		// if keep days is zero or negative, skip
		if (hiveJobsDo.getKeepdays() <= 0) {
			logger.info("keepdays <=0, skip." + hiveJobsDo);
			return;
		}

		List<Partition> partitionList = null;

		Calendar c = Calendar.getInstance();
		String today = (new SimpleDateFormat("yyyy-MM-dd")).format(c.getTime());

		try {
			logger.debug("Table = " + hiveJobsDo.getTablename());
			logger.debug("Pt_format = " + hiveJobsDo.getPt_format());
			String pt = hiveJobsDo.getPt_format().split("=")[0].trim();

			logger.debug("pt = " + pt);
			String format = hiveJobsDo.getPt_format().split("=")[1].trim();
			logger.debug("format = " + format);

			c.add(Calendar.DATE, 0 - hiveJobsDo.getKeepdays());
			String day = (new SimpleDateFormat(format)).format(c.getTime());
			logger.debug("day = " + day);

			partitionList = hiveHelper.getPartitionsByFilter(
					hiveJobsDo.getDbname(), hiveJobsDo.getTablename(), pt
							+ "<=" + "\"" + day + "\"");
		} catch (Exception e) {
			logger.error("Exception:", e);
		}

		if (null == partitionList || partitionList.isEmpty()) {
			System.out.println("partition is null, return.  " + hiveJobsDo);
			return;
		}

		boolean flag = true;
		for (Partition partition : partitionList) {
			String fromPath = partition.getSd().getLocation();
			String path = fromPath.replaceFirst("hdfs://", "");
			path = path.substring(path.indexOf("/"));
			String toPath = BACKUP + today + path;

			logger.debug("fromPath=" + fromPath);
			logger.debug("toPath=" + toPath);
			logger.debug("pt=" + partition.getValues());

			// move the hdfs file to backup folder
			flag = mvHDFS(fromPath, toPath);
			if (flag) {
				// delete the hive meta info
				dropHiveTablePartition(hiveJobsDo, partition.getValues());
			}
		}
		
		if(flag) {
			// send email if there are too many partitions to be dropped
			if (partitionList.size() > remove_partition_num_alert_threshold) {
				try {
					String subject = "Auto clean hive partition warning";
					StringBuilder content = new StringBuilder();
					content.append("Table[" + hiveJobsDo.getDbname() + "."
							+ hiveJobsDo.getTablename() + "]"
							+ " has been removed " + partitionList.size()
							+ " partitions : <br>");
					int index = 0;
					for (Partition p : partitionList) {
						index++;
						content.append(index + ":" + p.getSd().getLocation()
								+ "<br>");
					}
					EMail.sendMail(subject, content.toString(),
							hiveJobsDo.getEmail(), admin_email);
				} catch (MessagingException e) {
					logger.error("send email error:" + hiveJobsDo.getEmail()
							+ "ignore,\n" + e);
				}
			}
		}
		
	}

	private boolean mvHDFS(String frompath, String toPath) {
		try {
			FileSystem fs = hdfsHelper.getFileSystem();
			logger.info("frompath=" + frompath + "\ntopath = " + toPath);
			if (fs.mkdirs(new Path(toPath.substring(0, toPath.lastIndexOf("/"))))) {
				logger.info("mkdirs success");
			} else {
				logger.warn("mkdirs failed, ignore");
			}
			
			//tricky?
			System.setProperty("user.name","hdfs");
			
			Path to = new Path(toPath);
			if(fs.exists(to)) {
				System.setProperty("user.name","hdfs");
				fs.delete(to, true);
			}
			
			System.setProperty("user.name","hdfs");
			
			if (fs.rename(new Path(frompath), to)) {
				logger.info("rename success");
				return true;
			} else {
				logger.warn("rename failed, hive metastore does't drop");
				return false;
			}

		} catch (IOException e) {
			logger.error("IOException:", e);
		}
		return false;
	}

	private void dropHiveTablePartition(HiveJobsDo hiveJobsDao, List<String> pt) {

		String dbName = hiveJobsDao.getDbname();
		String tableName = hiveJobsDao.getTablename();

		try {
			HiveMetaStoreClient hiveClient = hiveHelper
					.getHiveMetaStoreClient();

			hiveClient.dropPartition(dbName, tableName, pt, false);
			logger.info("drop " + dbName + "." + tableName + ":" + pt);
		} catch (NoSuchObjectException e) {
			logger.error("no partition found:" + dbName + "." + tableName + pt,
					e);
		} catch (MetaException e) {
			logger.error(e);
		} catch (TException e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {

		HivePartitionCleanService job = new HivePartitionCleanService();

		System.out.println(job.remove_partition_num_alert_threshold);
	}

}
