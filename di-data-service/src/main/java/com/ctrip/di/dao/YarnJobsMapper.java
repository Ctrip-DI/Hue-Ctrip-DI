package com.ctrip.di.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Yarn jobs Mapper
 * @author xgliao
 *
 */
@Repository
public interface YarnJobsMapper {

	public void insertYarnJob(YarnJobsDo yarnJobsDo);

	public List<YarnJobsDo> getYanJobs();

	public Long getMaxStartTime();
	
	public List<YarnJobCountDo> getAllCount();
	
	public List<YarnJobCountDo> getJobCountByDate(int date);
	
	public List<YarnJobCountDo> getJobCountByPageRange(int start, int end);
	
	public List<YarnJobUserCountDo> getCountByDateUser();
	
	public List<YarnJobUserCountDo> getCountByDateUserD(int date);
	
	public List<YarnJobUserCountDo> getCountByDateUserForPage(int start, int num);
	
	public List<YarnUserJobDo> getNewestUserJobCount();
	
	public List<YarnUserJobDo> getUserJobCountByDate(int date);

	public List<YarnJobUserCountDo> getYarnJobUserByDate(String date);

	public List<YarnJobUserCountDo> getYarnJobUserByUserName(String userName);

}
