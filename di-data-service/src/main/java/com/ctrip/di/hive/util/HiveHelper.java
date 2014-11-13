package com.ctrip.di.hive.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.HiveObjectPrivilege;
import org.apache.hadoop.hive.metastore.api.HiveObjectRef;
import org.apache.hadoop.hive.metastore.api.HiveObjectType;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HiveHelper {

	@Value("${HIVE_CONFIG}")
	private String hive_conf;

	private static final Logger logger = Logger.getLogger(HiveHelper.class);

	private static HiveMetaStoreClient hiveClient = null;

	public HiveMetaStoreClient getHiveMetaStoreClient() {
		if (hiveClient == null) {
			HiveConf conf = new HiveConf();
			conf.addResource(HiveHelper.class.getClassLoader().getResource(
					"conf/hive/hive-default.xml"));
			conf.addResource(HiveHelper.class.getClassLoader().getResource(
					hive_conf));

			try {
				hiveClient = new HiveMetaStoreClient(conf, null);

			} catch (MetaException e) {
				logger.error("hive metastore exception: " + e);
			}
		}
		return hiveClient;
	}

	public List<Partition> getHiveTablePartitionList(String dbName,
			String tableName) {

		HiveMetaStoreClient hiveClient = getHiveMetaStoreClient();

		List<Partition> partitionList = null;

		try {
			partitionList = hiveClient.listPartitions(dbName, tableName,
					Short.MAX_VALUE);
		} catch (NoSuchObjectException e) {
			logger.warn("no table named:" + dbName + "." + tableName, e);
		} catch (MetaException e) {
			logger.error("hive metastore exception: " + e);
		} catch (TException e) {
			logger.error(e);
		}

		return partitionList;
	}

	public boolean isHiveTableExist(String tablename) {

		if (!tablename.contains(".")) {
			return false;
		}
		int index = tablename.indexOf(".");
		String dbName = tablename.substring(0, index);
		String tableName = tablename.substring(index + 1);

		try {
			HiveMetaStoreClient hiveClient = getHiveMetaStoreClient();
			return hiveClient.tableExists(dbName, tableName);
		} catch (NoSuchObjectException e) {
			logger.warn("no table named:" + tablename, e);
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public boolean hasPrivilegeToSetCleanAlert(String database, String table,
			String user) {
		HiveMetaStoreClient hiveClient = getHiveMetaStoreClient();
		HiveObjectRef hiveObject = new HiveObjectRef();
		hiveObject.setDbName(database);
		hiveObject.setObjectName(table);
		hiveObject.setObjectType(HiveObjectType.TABLE);
		List<HiveObjectPrivilege> privileges = new ArrayList<HiveObjectPrivilege>();
		try {
			privileges = hiveClient.list_privileges(user, PrincipalType.USER,
					hiveObject);
		} catch (Exception e) {
			logger.error("Error to get privileges:", e);
			return false;
		}
		for (HiveObjectPrivilege privilege : privileges) {
			String privilegeName = privilege.getGrantInfo().getPrivilege();
			if (privilegeName != null
					&& ("all".equalsIgnoreCase(privilegeName)
							|| "create".equalsIgnoreCase(privilegeName) || "ALTER"
								.equalsIgnoreCase(privilegeName))) {
				return true;
			}
		}

		return false;
	}

	public List<Partition> getPartitionsByFilter(String dbName,
			String tableName, String filter) {

		logger.info("Table:" + dbName + "." + tableName + " filter=" + filter);
		HiveMetaStoreClient hiveClient = getHiveMetaStoreClient();

		List<Partition> partitionList = null;

		try {
			partitionList = hiveClient.listPartitionsByFilter(dbName,
					tableName, filter, Short.MAX_VALUE);
		} catch (NoSuchObjectException e) {
			logger.warn("no table named:" + dbName + "." + tableName, e);
		} catch (MetaException e) {
			logger.error("hive metastore exception: " + e);
		} catch (TException e) {
			logger.error(e);
		}

		return partitionList;
	}

}