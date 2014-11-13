package com.ctrip.di.hive;

import java.util.Date;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctrip.di.dao.hive.HiveJobsDo;
import com.ctrip.di.dao.hive.HiveJobsService;
import com.ctrip.di.hive.HiveCheckService;
import com.ctrip.di.hive.HivePartitionCleanService;

public class TestHiveJob {

//	@Test
	public void testHiveCleanJob() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveJobsService hpcj = context.getBean(HiveJobsService.class);
		
		HiveJobsDo hiveJobsDo = hpcj.getHiveJob("test", "test3");
		
		JSONObject json = JSONObject.fromObject(hiveJobsDo);
		
		System.out.println(json.toString());
		
		System.out.println(hiveJobsDo.toString());
	}
	
	@Test
	public void testAddHiveJob() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveJobsService hpcj = context.getBean(HiveJobsService.class);
		
		HiveJobsDo hiveJobsDo = new HiveJobsDo();
		hiveJobsDo.setDbname("test");
		hiveJobsDo.setTablename("test5");
		hiveJobsDo.setPt_format("pt=${yyyy-MM-dd}");
		hiveJobsDo.setKeepdays(20);
		hiveJobsDo.setCheckrate(0.3);
		hiveJobsDo.setUsername("jianguo");
		hiveJobsDo.setEmail("jianguo@ctrip.com");
		long time = System.currentTimeMillis();
		hiveJobsDo.setCreate_time(new Date(time));
		hiveJobsDo.setModified_time(new Date(time));
		
		hpcj.addHiveJobs(hiveJobsDo);
	}
	
//	@Test
	public void testCheckHiveJob() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveCheckService hcs = context.getBean(HiveCheckService.class);
		
		hcs.checkHiveJob("test", "test2");
	}
}
