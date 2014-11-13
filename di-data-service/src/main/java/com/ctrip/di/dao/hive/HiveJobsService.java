package com.ctrip.di.dao.hive;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HiveJobsService {
	private static final Log logger = LogFactory.getLog(HiveJobsService.class);

	@Autowired
	private HiveJobsMapper hiveJobsMapper;

	public List<HiveJobsDo> getAllHiveJobs() {
		List<HiveJobsDo> result = null;
		try {
			result = hiveJobsMapper.getAllHiveJobs();
		} catch (Exception e) {
			// Ignore sql exception
			logger.error("get all hive job info failed", e);
		}
		return result;
	}

	public HiveJobsDo getHiveJob(String dbName, String tableName) {
		HiveJobsDo result = null;
		try {
			result = hiveJobsMapper.getHiveJobs(dbName, tableName);
		} catch (Exception e) {
			// Ignore sql exception
			logger.error("get hive job info failed", e);
		}

		return result;
	}

	/**
	 * add the hive jobs, update if it exists in database
	 * 
	 * @param hiveJobsDao
	 */
	@Transactional
	public void addHiveJobs(HiveJobsDo hiveJobsDao) {
		logger.info("begin to add hive job : " + hiveJobsDao);

		try {
			HiveJobsDo hjd = hiveJobsMapper.getHiveJobs(
					hiveJobsDao.getDbname(), hiveJobsDao.getTablename());
			if (null == hjd) {
				hiveJobsMapper.insertHiveJobs(hiveJobsDao);
			} else {
				hiveJobsDao.setCreate_time(hjd.getCreate_time());
				hiveJobsMapper.updateHiveJobs(hiveJobsDao);
			}

		} catch (Exception e) {
			logger.error("add hive job info failed, rollback...", e);
		}
	}

}
