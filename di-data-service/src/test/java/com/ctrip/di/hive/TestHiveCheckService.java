package com.ctrip.di.hive;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctrip.di.hive.HiveCheckService;

public class TestHiveCheckService {

	@Test
	public void testHiveCheck() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveCheckService hcs = context.getBean(HiveCheckService.class);
		
		hcs.checkHiveJob("test", "test2");
	}
}
