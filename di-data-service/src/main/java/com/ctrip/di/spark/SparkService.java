package com.ctrip.di.spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Spark service to excute spark sql
 * 
 * @author xgliao
 * 
 */
@Service
public class SparkService {
	private static Log logger = LogFactory.getLog(SparkService.class);

	@Value("${SPARK_JDBC_URL}")
	private String sparkUrl;

	private ThreadLocal<Connection> sparkConnection = new ThreadLocal<Connection>() {

		@Override
		protected Connection initialValue() {
			return getConnection();
		}
	};

	protected Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			conn = DriverManager.getConnection(sparkUrl, "hive", "hive");
		} catch (ClassNotFoundException e) {
			logger.error("Class Not Found Exception:{}", e);
		} catch (SQLException e) {
			logger.error("SQL Exception:{}", e);
		}

		return conn;
	}

	/**
	 * excute sql, if failed, retry again
	 * 
	 * @param sql
	 * @param database
	 * @return
	 */
	public List<Map<Object, Object>> executeSQL(String sql, String database) {
		List<Map<Object, Object>> result = execute(sql, database);
		if (result == null) {
			return execute(sql, database);
		}

		return result;
	}

	private List<Map<Object, Object>> execute(String sql, String database) {
		List<Map<Object, Object>> result = new ArrayList<Map<Object, Object>>();

		Connection conn = sparkConnection.get();

		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			boolean isSuccess = statement.execute(getUseDatabase(database));
			if (isSuccess) {
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					ResultSetMetaData metaData = rs.getMetaData();
					Map<Object, Object> valueMap = new HashMap<Object, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnName(i);
						Object value = rs.getObject(columnName);
						valueMap.put(columnName, value);
					}
					result.add(valueMap);
				}
			} else {
				Map<Object, Object> errorMap = new HashMap<Object, Object>();
				errorMap.put("status", "error");
				errorMap.put("message",
						"User Database Failed, please check if database is right");

				result.add(errorMap);
			}
		} catch (Exception e) {
			logger.error("Exception:", e);
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					// Ignore Exception
				}
			}
			//reset connection
			sparkConnection.set(getConnection());
			return null;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// Ignore Exception
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// Ignore Exception
				}
			}
		}

		return result;
	}

	private String getUseDatabase(String database) {
		return "use " + database;
	}

}
