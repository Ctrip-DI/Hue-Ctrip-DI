package com.ctrip.di.hive;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctrip.di.hive.util.HDFSHelper;

public class TestHDFSHelper {

	@Test
	public void testGetHDFS() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HDFSHelper hdfsHelper = context.getBean(HDFSHelper.class);
		
		FileSystem fs = hdfsHelper.getFileSystem();
		
		try {
			FileStatus[] status = fs.listStatus(new Path("/user/hdfs"));
			for(FileStatus fileStatus : status) {
				System.out.println("File:" + fileStatus.getPath());
			}
			System.out.println(status[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void testRMHDFS() throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HDFSHelper hdfsHelper = context.getBean(HDFSHelper.class);
		FileSystem fs = hdfsHelper.getFileSystem();
		
		System.out.println(fs.mkdirs(new Path("/user/you/testxgliao")));
	}
}
