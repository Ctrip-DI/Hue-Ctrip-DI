package com.ctrip.di.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.util.UrlUtils;
import com.ctrip.di.ganglia.GangliaMetricService;

/**
 * APIs to get information from ganglia
 * 
 * @author xgliao
 * 
 */
@Controller
@RequestMapping("/ganglia")
public class GangliaController {
	private static Log logger = LogFactory.getLog(GangliaController.class);

	@Value("${GANGLIA_GRAPH_URL}")
	private String graphUrl;
	@Value("${GANGLIA_HOST_URL}")
	private String hostUrl;

	@Autowired
	private GangliaMetricService metricService;

	@RequestMapping("/graphjson")
	public void getData(HttpServletRequest request, HttpServletResponse response) {
		String queryString = request.getQueryString();

		response.setContentType("application/json");
		String jsonpCallback = request.getParameter("jsonpCallback");
		String url = graphUrl + queryString;
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			if (StringUtils.isNotBlank(queryString)
					&& queryString.indexOf("json=") != -1) {
				String jsonData = UrlUtils.getContent(url);
				pw.println(jsonpCallback + "(" + jsonData + ")");
			} else {
				pw.println(jsonpCallback
						+ "("
						+ "{\"status\":\"failed\",\"messange\":\"json parameter can not be null\"}"
						+ ")");
			}
		} catch (Exception e) {
			logger.error("Ganglia Json Error:" + url, e);
			if (pw != null) {
				pw.println(jsonpCallback + "(" + "{\"status\":\"failed\"}"
						+ ")");
			}
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}

	/*
	 * @RequestMapping("/graphjsonbycluster") public void
	 * getDataByCluster(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * response.setContentType("application/json");
	 * 
	 * String clusterName = request.getParameter("clustername"); if (clusterName
	 * == null) { PrintWriterUtil.writeError(request, response,
	 * "Cluster Name Can Not be null"); return; } String queryString =
	 * request.getQueryString(); List<String> hostList =
	 * metricService.getHostListByCluster(clusterName); for (String host :
	 * hostList) { String url = graphUrl + queryString + "&&h=" + host; String
	 * jsonData = UrlUtils.getContent(url); }
	 * 
	 * String jsonpCallback = request.getParameter("jsonpCallback"); // TODO
	 * UNFINISHED }
	 */

	@RequestMapping("/clusterinfo")
	public void getHostInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String json = UrlUtils.getContent(hostUrl);
		response.setContentType("application/json");
		String jsonpCallback = request.getParameter("jsonpCallback");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.println(jsonpCallback + "(" + json + ")");
		} catch (IOException e) {
			logger.error("Ganglia Json Error:" + hostUrl, e);
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}

	}

}
