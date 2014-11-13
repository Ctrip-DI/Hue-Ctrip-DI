package com.ctrip.di.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.dao.spark.SparkJob;
import com.ctrip.di.dao.spark.SparkJobMapper;
import com.ctrip.di.spark.SparkService;

/**
 * APIs to excute sql on spark
 * @author xgliao
 *
 */
@Controller
@RequestMapping("/spark")
public class SparkController {
	private static Log logger = LogFactory.getLog(SparkController.class);

	private static final int SQL_MAX_LENGTH = 2000;

	@Autowired
	private SparkService sparkService;

	@Autowired
	private SparkJobMapper sparkJobMapper;

	@RequestMapping("/execute")
	public void executeSQL(HttpServletRequest request,
			HttpServletResponse response) {
		String sql = request.getParameter("sql");
		if (StringUtils.isEmpty(sql)) {
			PrintWriterUtil
					.writeError(request, response, "SQL can not be null");
			return;
		}
		String user = request.getParameter("user");
		String database = request.getParameter("database");
		if (database == null) {
			database = "default";
		}
		sql = sql.replace(";", "");
		List<Map<Object, Object>> result = null;
		SparkJob sparkJob = new SparkJob();
		sparkJob.setUser(user);
		if (sql.length() > SQL_MAX_LENGTH) {
			sql = sql.substring(0, SQL_MAX_LENGTH);
		}
		sparkJob.setSql(sql);
		sparkJob.setStartTime(System.currentTimeMillis());
		try {
			result = sparkService.executeSQL(sql, database);
		} catch (Exception e) {
			logger.error("Exception:{}", e);
			PrintWriterUtil.writeError(request, response, e.getMessage());
			return;
		}
		sparkJob.setFinishTime(System.currentTimeMillis());
		boolean isSuccess = getStatus(result);
		if (isSuccess) {
			sparkJob.setStatus("Success");
		} else {
			sparkJob.setStatus("Fail");
		}
		if (user != null) {
			sparkJobMapper.insertSparkJob(sparkJob);
		}

		JSONArray jsonArray = JSONArray.fromObject(result);
		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/getjobs")
	public void getJobs(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("user");
		if (StringUtils.isEmpty(user)) {
			PrintWriterUtil.writeError(request, response,
					"User can not be null");
			return;
		}
		List<SparkJob> sparkJobs = sparkJobMapper.getSparkJobsByUser(user);
		JSONArray jsonArray = JSONArray.fromObject(sparkJobs);
		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	private boolean getStatus(List<Map<Object, Object>> result) {
		if (result != null && !result.isEmpty()) {
			Object status = result.get(0).get("status");
			if (status != null) {
				return false;
			}
		}
		return true;
	}

}
