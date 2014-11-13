package com.ctrip.di.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Yarn job service: crawler the jobs from yarn cluster and insert the job list into mysql.
 * And static the value by different dimensions.
 * @author xgliao
 *
 */
@Service
public class YarnJobService {

	@Autowired
	private YarnJobsMapper yarnJobsMapper;

	@Transactional
	public void insertJobs(List<YarnJobsDo> jobList) {
		for (YarnJobsDo job : jobList) {
			yarnJobsMapper.insertYarnJob(job);
		}
	}

	public long getMaxStartTime() {
		Long maxTime = yarnJobsMapper.getMaxStartTime();
		if(maxTime == null) {
			return 0;
		}
		return maxTime;
	}

	public List<YarnJobCountDo> getAllCount() {
		return yarnJobsMapper.getAllCount();
	}

	public List<YarnJobUserCountDo> getCountByDateUser() {
		return yarnJobsMapper.getCountByDateUser();
	}
	
	public List<YarnJobUserCountDo> getCountByDateUserD(int date) {
		return yarnJobsMapper.getCountByDateUserD(date);
	}
	
	public List<YarnJobUserCountDo> getCountByDateUserForPage(int start, int num) {
		return yarnJobsMapper.getCountByDateUserForPage(start, num);
	}
	
	public List<YarnJobUserCountDo> getYarnJobUserByDate(String date) {
		return yarnJobsMapper.getYarnJobUserByDate(date);
	}
	
	public List<YarnJobUserCountDo> getYarnJobUserByUserName(String userName) {
		return yarnJobsMapper.getYarnJobUserByUserName(userName);
	}

	public List<YarnUserJobDo> getNewestUserJobCount() {
		return yarnJobsMapper.getNewestUserJobCount();
	}

	public List<YarnUserJobDo> getUserJobCountByDate(int date) {
		return yarnJobsMapper.getUserJobCountByDate(date);
	}
	
	public List<YarnJobCountDo> getJobCountByDate(int date) {
		return yarnJobsMapper.getJobCountByDate(date);
	}
	
	public List<YarnJobCountDo> getJobCountByPageRange(int start, int end) {
		return yarnJobsMapper.getJobCountByPageRange(start, end);
	}
}
