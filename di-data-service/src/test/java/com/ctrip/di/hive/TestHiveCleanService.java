package com.ctrip.di.hive;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.AlreadyExistsException;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.InvalidObjectException;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Order;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.SerDeInfo;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.thrift.TException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ctrip.di.dao.hive.HiveJobsDo;
import com.ctrip.di.hive.HivePartitionCleanService;
import com.ctrip.di.hive.util.HiveHelper;

public class TestHiveCleanService {

	protected static HiveMetaStoreClient client;
	
	@Test
	public void testCheckHiveJob() throws 
		AlreadyExistsException, InvalidObjectException, MetaException, TException {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		
		HiveHelper hiveHelper = context.getBean(HiveHelper.class);
		
		client = hiveHelper.getHiveMetaStoreClient();
		
		String dbName = "test99";
		String tblName = "test99";
		
		client.dropTable(dbName, tblName);
	    client.dropDatabase(dbName);
		
		Database db = new Database();
	    db.setName(dbName);
	    client.createDatabase(db);
		
	    ArrayList<FieldSchema> cols = new ArrayList<FieldSchema>(2);
	    cols.add(new FieldSchema("c1", serdeConstants.STRING_TYPE_NAME, ""));
	    cols.add(new FieldSchema("c2", serdeConstants.INT_TYPE_NAME, ""));

	    ArrayList<FieldSchema> partCols = new ArrayList<FieldSchema>(3);
	    partCols.add(new FieldSchema("business", serdeConstants.STRING_TYPE_NAME, ""));
	    partCols.add(new FieldSchema("pt", serdeConstants.STRING_TYPE_NAME, ""));
	    partCols.add(new FieldSchema("hour", serdeConstants.STRING_TYPE_NAME, ""));
	    
	    Table tbl = new Table();
	    tbl.setDbName(dbName);
	    tbl.setTableName(tblName);
	    StorageDescriptor sd = new StorageDescriptor();
	    tbl.setSd(sd);
	    sd.setCols(cols);
	    sd.setCompressed(false);
	    sd.setNumBuckets(1);
	    sd.setParameters(new HashMap<String, String>());
	    sd.setBucketCols(new ArrayList<String>());
	    sd.setSerdeInfo(new SerDeInfo());
	    sd.getSerdeInfo().setName(tbl.getTableName());
	    sd.getSerdeInfo().setParameters(new HashMap<String, String>());
	    sd.getSerdeInfo().getParameters()
	        .put(serdeConstants.SERIALIZATION_FORMAT, "1");
	    sd.setSortCols(new ArrayList<Order>());

	    tbl.setPartitionKeys(partCols);
	    client.createTable(tbl);

	    tbl = client.getTable(dbName, tblName);
	    
	    List<String> vals = new ArrayList<String>(3);
	    vals.add("hotel");
	    vals.add("2014-10-25");
	    vals.add("01");
	    
	    List<String> vals2 = new ArrayList<String>(3);
	    vals2.add("hotel");
	    vals2.add("2014-10-25");
	    vals2.add("02");
	    
	    List<String> vals3 = new ArrayList<String>(3);
	    vals3.add("hotel");
	    vals3.add("2014-10-25");
	    vals3.add("03");
	    
	    List<String> vals4 = new ArrayList<String>(3);
	    vals4.add("airline");
	    vals4.add("2014-10-25");
	    vals4.add("01");
	    
	    List<String> vals5 = new ArrayList<String>(3);
	    vals5.add("airline");
	    vals5.add("2014-10-25");
	    vals5.add("02");
	    
	    List<String> vals6 = new ArrayList<String>(3);
	    vals6.add("airline");
	    vals6.add("2014-10-25");
	    vals6.add("03");
	    
	    add_partition(client, tbl, vals, "part1");
	    add_partition(client, tbl, vals2, "part2");
	    add_partition(client, tbl, vals3, "part3");
	    add_partition(client, tbl, vals4, "part4");
	    add_partition(client, tbl, vals5, "part5");
	    add_partition(client, tbl, vals6, "part6");
	    
	    List<String> part_values = new ArrayList<String>();
    	part_values.add("business");
    	part_values.add("pt");
    	part_values.add("hour");
	    
	    System.out.println("partition number before clean: " + client.listPartitionNames(dbName, tblName, Short.MAX_VALUE).size());
	    
		HivePartitionCleanService hpcj = context.getBean(HivePartitionCleanService.class);
		
		HiveJobsDo hiveJobsDo = new HiveJobsDo();
		hiveJobsDo.setDbname(dbName);
		hiveJobsDo.setTablename(tblName);
		hiveJobsDo.setKeepdays(4);
		hiveJobsDo.setPt_format("pt=yyyy-MM-dd");
		
		hpcj.doOneHiveTable(hiveJobsDo);
		
		System.out.println("partition number after clean: " + client.listPartitionNames(dbName, tblName, Short.MAX_VALUE).size());
		
		assertEquals(client.listPartitionNames(dbName, tblName, Short.MAX_VALUE).size(), 0);
	  
	}
	
	//@Test
	public void testCheckCleanJob() {
		ApplicationContext context = new ClassPathXmlApplicationContext("rest-servlet.xml");
		HivePartitionCleanService hpcj = context.getBean(HivePartitionCleanService.class);
		
		HiveJobsDo hiveJobsDo = new HiveJobsDo();
		hiveJobsDo.setDbname("test");
		hiveJobsDo.setTablename("test2");
		hiveJobsDo.setKeepdays(2);
		hiveJobsDo.setPt_format("pt=2014-11-0600");
		
		hpcj.doOneHiveTable(hiveJobsDo);
	}
	
	private void add_partition(HiveMetaStoreClient client, Table table,
		      List<String> vals, String location) throws InvalidObjectException,
		        AlreadyExistsException, MetaException, TException {

	    Partition part = new Partition();
	    part.setDbName(table.getDbName());
	    part.setTableName(table.getTableName());
	    part.setValues(vals);
	    part.setParameters(new HashMap<String, String>());
	    part.setSd(table.getSd());
	    part.getSd().setSerdeInfo(table.getSd().getSerdeInfo());
	    part.getSd().setLocation(table.getSd().getLocation() + location);

	    client.add_partition(part);
	}
}
