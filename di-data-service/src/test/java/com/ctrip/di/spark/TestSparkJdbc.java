package com.ctrip.di.spark;

import java.util.List;
import java.util.Map;

public class TestSparkJdbc {
	
	public static void main(String[] args) throws Exception {
		SparkService test = new SparkService();
		List<Map<Object, Object>> result = test.executeSQL("show tables", "default");
		System.out.println(result.size());
	}

}
