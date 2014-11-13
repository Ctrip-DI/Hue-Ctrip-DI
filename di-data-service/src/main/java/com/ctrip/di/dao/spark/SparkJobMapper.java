package com.ctrip.di.dao.spark;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Spark job Mapper
 * @author xgliao
 *
 */
@Repository
public interface SparkJobMapper {

	public void insertSparkJob(SparkJob sparkJob);

	public List<SparkJob> getSparkJobsByUser(String userName);

}
