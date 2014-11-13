package com.ctrip.di.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Statistics user Mapper
 * @author wu_jm
 *
 */
@Repository
public interface StatisticsUserMapper {

	public void insertStatisticsUser(StatisticsUser user);
	
	public List<StatisticsUser> queryAllStatistic();

}
