package com.ctrip.di.hive;

import java.util.List;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.thrift.TException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctrip.di.hive.util.HiveHelper;

public class TestHiveHelper {

	@Test
	public void testHiveHelper() {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveHelper hiveHelper = context.getBean(HiveHelper.class);
		
    	HiveMetaStoreClient hiveClient = hiveHelper.getHiveMetaStoreClient();
   	 
    	List<Partition> partitionList = null;
    	
		try {
			partitionList = hiveClient.listPartitionsByFilter("test", "test6", "pt=\"2014-10-03\" and hour=\"01\"", Short.MAX_VALUE);
		} catch (NoSuchObjectException e) {
			System.out.println("no table named:" + e);
		} catch (MetaException e) {
			System.out.println("hive metastore exception: " + e);
		} catch (TException e) {
			System.out.println(e);
		}
    	 
    	 for(Partition p : partitionList) {
    		 System.out.println(p.getValues().get(0));
    		 System.out.println(p.getSd().getLocation());
    		 System.out.println(p.getValues());
    	 }
	}
}
