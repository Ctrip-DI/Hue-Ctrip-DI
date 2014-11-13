package com.ctrip.di.common.jmx;

public class TestHdfsJmxService {
	
	public static void main(String[] args) throws Exception {
		HdfsJmxService test = new HdfsJmxService();
		test.getJmxBean();
	}

}
