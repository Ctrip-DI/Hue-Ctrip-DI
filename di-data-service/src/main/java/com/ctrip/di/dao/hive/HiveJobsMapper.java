package com.ctrip.di.dao.hive;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface HiveJobsMapper {

	public void insertHiveJobs(HiveJobsDo hiveJobsDo);

	public HiveJobsDo getHiveJobs(String dbname, String tablename);

	public List<HiveJobsDo> getAllHiveJobs();

	void updateHiveJobs(HiveJobsDo hiveJobsDo);
}
