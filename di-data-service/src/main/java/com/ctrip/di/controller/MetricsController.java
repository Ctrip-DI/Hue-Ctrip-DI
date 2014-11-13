package com.ctrip.di.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.MetricConfigParser;
import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.ganglia.GangliaMetricService;

/**
 * API to get metric config informations
 * @author xgliao
 *
 */

@Controller
@RequestMapping("/metric")
public class MetricsController {

	@Autowired
	private MetricConfigParser configParser;

	@Autowired
	private GangliaMetricService metricService;

	@RequestMapping("/getmetriclist")
	public void getMetricList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("application/json");
		String clusterName = request.getParameter("clustername");
		if (clusterName == null) {
			PrintWriterUtil.writeError(request, response,
					"Cluster Name Can Not be null");
			return;
		}

		List<String> metricList = metricService
				.getVIMetricsByCluster(clusterName);

		JSONObject json = new JSONObject();
		json.put("metrics", metricList);
		PrintWriterUtil.writeJson(request, response, json.toString());
	}

	@RequestMapping("/getallmetriclist")
	public void getAllMetricList(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("application/json");
		String clusterName = request.getParameter("clustername");
		if (clusterName == null) {
			PrintWriterUtil.writeError(request, response,
					"Cluster Name Can Not be null");
			return;
		}

		List<String> metricList = metricService
				.getMetricsByCluster(clusterName);
		JSONObject json = new JSONObject();
		json.put("metrics", metricList);
		PrintWriterUtil.writeJson(request, response, json.toString());
	}

}
