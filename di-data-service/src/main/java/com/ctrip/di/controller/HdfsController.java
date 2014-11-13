package com.ctrip.di.controller;

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

import com.ctrip.di.common.jmx.HdfsJmxService;
import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.hdfs.HdfsDirSummary;
import com.ctrip.di.hdfs.HdfsFileSummaryService;
/**
 * APIs to get information from hdfs cluster
 * @author xgliao
 *
 */
@Controller
@RequestMapping("/hdfs")
public class HdfsController {
	private static Log logger = LogFactory.getLog(HdfsController.class);

	@Autowired
	private HdfsFileSummaryService fileSummaryService;

	@Autowired
	private HdfsJmxService hadoopJmxService;

	@RequestMapping("/get/userfileinfo")
	public void getUserFileInfo(HttpServletRequest request,
			HttpServletResponse response) {
		List<HdfsDirSummary> hdfsSummaryList = fileSummaryService
				.getContentSummaryList();

		JSONArray jsonArray = JSONArray.fromObject(hdfsSummaryList);

		PrintWriterUtil.writeJson(request, response, jsonArray.toString());
	}

	@RequestMapping("/get/hdfsusageinfo")
	public void getHdfsClusterInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONObject jsonObject = hadoopJmxService.getJmxBean();
			PrintWriterUtil.writeJson(request, response, jsonObject.toString());
		} catch (Exception e) {
			logger.error("Jmx Service Exception", e);
			PrintWriterUtil.writeError(request, response,
					"Get hdfs usage information error " + e.getMessage());
		}
	}

}
