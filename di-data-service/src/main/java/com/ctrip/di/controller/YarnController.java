package com.ctrip.di.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.dao.YarnJobCountDo;
import com.ctrip.di.dao.YarnJobService;
import com.ctrip.di.dao.YarnJobUserCountDo;
import com.ctrip.di.dao.YarnUserJobDo;

/**
 * APIs to get information by yarn
 * @author xgliao
 *
 */
@Controller
@RequestMapping("/yarn")
public class YarnController {
	private static Log logger = LogFactory.getLog(YarnController.class);

	@Autowired
	private YarnJobService yarnJobService;

	@RequestMapping("/get/jobsuccessrate")
	public void getJobSuccessRate(HttpServletRequest request,
			HttpServletResponse response) {
		String date = request.getParameter("date");
		if (date == null) {
			PrintWriterUtil.writeError(request, response,
					"Date can not be null");
			return;
		}
		List<YarnJobCountDo> jobCountList = yarnJobService
				.getJobCountByDate(Integer.valueOf(date));

		JSONArray jsonArray = new JSONArray();
		for (YarnJobCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();
			JSONObject json = new JSONObject();
			json.put("dateStr", yarnJobCountDo.getDateStr());
			json.put("dateTime", yarnJobCountDo.getDateTime());
			json.put("successRate", yarnJobCountDo.getSuccessRate());
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobnewestuserjobcount")
	public void getNewestUserJobCount(HttpServletRequest request,
			HttpServletResponse response) {
		List<YarnUserJobDo> jobList = yarnJobService.getNewestUserJobCount();
		JSONArray jsonArray = new JSONArray();
		for (YarnUserJobDo yarnJobCountDo : jobList) {
			yarnJobCountDo.init();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobuserjobcountbydate")
	public void getUserJobCountByDate(HttpServletRequest request,
			HttpServletResponse response) {
		String date = request.getParameter("date");
		if (date == null) {
			PrintWriterUtil.writeError(request, response,
					"Date can not be null");
			return;
		}

		List<YarnUserJobDo> jobList = yarnJobService
				.getUserJobCountByDate(Integer.valueOf(date));
		JSONArray jsonArray = new JSONArray();
		for (YarnUserJobDo yarnJobCountDo : jobList) {
			yarnJobCountDo.init();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobsuccesscountbydate")
	public void getJobSuccessCountByDate(HttpServletRequest request,
			HttpServletResponse response) {
		String date = request.getParameter("date");
		if (date == null) {
			PrintWriterUtil.writeError(request, response,
					"Date can not be null");
			return;
		}
		List<YarnJobCountDo> jobCountList = yarnJobService
				.getJobCountByDate(Integer.valueOf(date));

		JSONArray jsonArray = new JSONArray();
		for (YarnJobCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();
			JSONObject json = new JSONObject();
			json.put("dateStr", yarnJobCountDo.getDateStr());
			json.put("dateTime", yarnJobCountDo.getDateTime());
			json.put("successCount", yarnJobCountDo.getSuccessCount());
			json.put("totalCount", yarnJobCountDo.getTotalCount());
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobcountdetail")
	public void getJobCountDetail(HttpServletRequest request,
			HttpServletResponse response) {
		List<YarnJobCountDo> jobCountList = yarnJobService.getAllCount();

		JSONArray jsonArray = new JSONArray();
		for (YarnJobCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobcountdetailbyuser")
	public void getJobCountDetailByUser(HttpServletRequest request,
			HttpServletResponse response) {
		String date = request.getParameter("date");
		List<YarnJobUserCountDo> jobCountList = new ArrayList<YarnJobUserCountDo>();
		if (date == null) {
			jobCountList = yarnJobService.getCountByDateUser();
		} else {
			jobCountList = yarnJobService.getCountByDateUserD(Integer
					.valueOf(date));
		}

		JSONArray jsonArray = new JSONArray();
		for (YarnJobUserCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobcountdetailbyuserandpage")
	public void getJobCountDetailByUserAndPange(HttpServletRequest request,
			HttpServletResponse response) {
		String page = request.getParameter("page");
		String show = request.getParameter("show");
		if (page == null || show == null) {
			PrintWriterUtil.writeError(request, response,
					"Page and show can not be null");
			return;
		}
		int pageInt = Integer.valueOf(page);
		int showInt = Integer.valueOf(show);

		int start = (pageInt - 1) * showInt;
		List<YarnJobUserCountDo> jobCountList = yarnJobService
				.getCountByDateUserForPage(start, showInt);

		JSONArray jsonArray = new JSONArray();
		for (YarnJobUserCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/jobcountdetailbyuserordate")
	public void getJobCountDetailByUserOrDate(HttpServletRequest request,
			HttpServletResponse response) {
		String searchPara = request.getParameter("search");
		if (searchPara == null) {
			PrintWriterUtil.writeError(request, response,
					"Search parameter can not be null");
			return;
		}

		boolean isDate = isDate(searchPara);
		List<YarnJobUserCountDo> jobCountList = null;
		if (isDate) {
			jobCountList = yarnJobService.getYarnJobUserByDate(searchPara);
		} else {
			jobCountList = yarnJobService.getYarnJobUserByUserName(searchPara);
		}

		JSONArray jsonArray = new JSONArray();
		for (YarnJobUserCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());

	}

	private boolean isDate(String searchPara) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.parse(searchPara);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	@RequestMapping("/get/jobcountdetailbypage")
	public void getJobCountDetailByPage(HttpServletRequest request,
			HttpServletResponse response) {
		String page = request.getParameter("page");
		String show = request.getParameter("show");
		if (page == null || show == null) {
			PrintWriterUtil.writeError(request, response,
					"Page and show can not be null");
			return;
		}
		int pageInt = Integer.valueOf(page);
		int showInt = Integer.valueOf(show);

		int start = (pageInt - 1) * showInt;
		int end = pageInt * showInt;

		List<YarnJobCountDo> jobCountList = yarnJobService
				.getJobCountByPageRange(start, end);

		JSONArray jsonArray = new JSONArray();
		for (YarnJobCountDo yarnJobCountDo : jobCountList) {
			yarnJobCountDo.initRate();

			JSONObject json = JSONObject.fromObject(yarnJobCountDo);
			jsonArray.add(json);
		}

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	/*
	 * @Autowired private YarnJmxService yarnJmxService;
	 * 
	 * @RequestMapping("/get/yarnusageinfo") public void
	 * getHdfsClusterInfo(HttpServletRequest request, HttpServletResponse
	 * response) { try { JSONObject jsonObject = yarnJmxService.getJmxBean();
	 * PrintWriterUtil.writeJson(request, response, jsonObject.toString()); }
	 * catch (Exception e) { logger.error("Jmx Service Exception", e);
	 * PrintWriterUtil.writeError(request, response,
	 * "Get hdfs usage information error" + e.getMessage()); } }
	 */

}
