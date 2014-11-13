package com.ctrip.di.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.dao.hive.HiveJobsDo;
import com.ctrip.di.dao.hive.HiveJobsService;
import com.ctrip.di.hive.util.HiveHelper;

@Controller
@RequestMapping("/hive")
public class HiveController {

	private static final Log logger = LogFactory.getLog(HiveController.class);

	@Autowired
	private HiveHelper hiveHelper;

	@Autowired
	private HiveJobsService hiveJobsService;

	@RequestMapping("/get/addHiveJobInfo")
	public void addHiveJobInfo(HttpServletRequest request,
			HttpServletResponse response) {

		String dbName = request.getParameter("dbname");
		String tableName = request.getParameter("tablename");
		if (StringUtils.isBlank(dbName) || StringUtils.isBlank(tableName)) {
			logger(request, response, "Database or Table name can not be null");
			return;
		}
		if (!hiveHelper.isHiveTableExist(dbName + "." + tableName)) {
			logger(request, response,
					"Table can not be found, please check if the table is exist.");
			return;
		}

		String keepDays = request.getParameter("keepdays");
		String partitionPattern = request.getParameter("partitionPattern");

		String checkRate = request.getParameter("checkrate");

		if (StringUtils.isBlank(keepDays) && StringUtils.isBlank(checkRate)) {
			PrintWriterUtil.writeError(request, response,
					"KeepDays and checkRate can't be null at the same time.");
			return;
		}

		// keepDays check
		int days = 0;
		if (!StringUtils.isBlank(keepDays)) {

			try {
				days = Integer.parseInt(keepDays);
			} catch (Exception e) {
				logger(request, response,
						"KeepDays is not correct, which should be Integer.");
				return;
			}

			if (StringUtils.isBlank(partitionPattern)) {
				PrintWriterUtil
						.writeError(request, response,
								"partitionPattern can't be null when keepDays is not null.");
				return;
			}

			// partitionPattern check
			if (!partitionPattern.contains("=")) {
				logger(request, response,
						"PartitionPattern is not correct, which should not be like dt=yyyy-MM-dd.");
				return;
			}
			try {
				Calendar c = Calendar.getInstance();
				String format = partitionPattern.split("=")[1].trim();
				new SimpleDateFormat(format).format(c.getTime());
			} catch (Exception e) {
				PrintWriterUtil
						.writeError(request, response,
								"PartitionPattern is not correct, which should not be like dt=yyyy-MM-dd.");
				return;
			}
		} else if (!StringUtils.isBlank(partitionPattern)) {
			PrintWriterUtil
					.writeError(request, response,
							"KeepDays can't be null when PartitionPattern is not null.");
			return;
		}

		// checkRate value check
		double rate = 0;
		if (!StringUtils.isBlank(checkRate)) {

			try {
				rate = Double.parseDouble(checkRate);
				if (rate < 0) {
					PrintWriterUtil.writeError(request, response,
							"CheckRate should be a positive number.");
				}
			} catch (Exception e) {
				logger.error("CheckRate is not correct, which should be double");
				PrintWriterUtil.writeError(request, response,
						"CheckRate is not correct, which should be double");
				return;
			}
		}

		String username = request.getParameter("username");
		if (StringUtils.isBlank(username)) {
			logger(request, response, "username is not correct");
			return;
		}

		String email = request.getParameter("email");
		if (StringUtils.isBlank(email) || !email.contains("@")
				|| !email.contains(".")) {
			logger(request, response, "email is not correct");
			return;
		}

		if (!hiveHelper
				.hasPrivilegeToSetCleanAlert(dbName, tableName, username)) {
			PrintWriterUtil
					.writeError(
							request,
							response,
							"Please make sure you have enough privileges to do this config. Note: You should at least have one of these privileges:ALL, ALTER or CREATE.");
			return;
		}

		HiveJobsDo hiveJobsDo = new HiveJobsDo();
		hiveJobsDo.setDbname(dbName);
		hiveJobsDo.setTablename(tableName);
		hiveJobsDo.setKeepdays(days);
		hiveJobsDo.setPt_format(partitionPattern);
		hiveJobsDo.setCheckrate(rate);
		hiveJobsDo.setUsername(username);
		hiveJobsDo.setEmail(email);
		long time = System.currentTimeMillis();
		hiveJobsDo.setCreate_time(new Date(time));
		hiveJobsDo.setModified_time(new Date(time));

		hiveJobsService.addHiveJobs(hiveJobsDo);

		PrintWriterUtil.writeJson(request, response, "{\"status\":\"ok\"}");
	}

	private void logger(HttpServletRequest request,
			HttpServletResponse response, String message) {
		logger.error(message);
		PrintWriterUtil.writeError(request, response, message);
	}

	@RequestMapping("/get/getHiveJobInfo")
	public void getHiveJobInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String dbName = request.getParameter("dbname");
		String tableName = request.getParameter("tablename");

		logger.info("get hive job info:" + dbName + "." + tableName);

		HiveJobsDo hiveJobsDo = hiveJobsService.getHiveJob(dbName, tableName);

		JSONObject json = JSONObject.fromObject(hiveJobsDo);

		PrintWriterUtil.writeJson(request, response, json.toString());
	}

}
