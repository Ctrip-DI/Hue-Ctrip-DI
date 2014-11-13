package com.ctrip.di.ganglia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.xml.bind.JAXBException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ctrip.di.common.MetricConfigParser;
import com.ctrip.di.common.util.UrlUtils;
import com.ctrip.di.pojo.gen.Cluster;
import com.ctrip.di.pojo.gen.Clusters;
import com.ctrip.di.pojo.gen.Metric;

/**
 * 
 * Ganglia Metric Service: Provide all the metric information from ganglia
 * @author xgliao
 *
 */
@Service
@Singleton
public class GangliaMetricService {
	private static Log logger = LogFactory.getLog(GangliaMetricService.class);

	@Autowired
	private GangliaHttpParser httpParser;

	@Autowired
	private MetricConfigParser configParser;

	@Value("${GANGLIA_HOST_URL}")
	private String hostUrl;

	private Map<String, List<String>> metricMap = new ConcurrentHashMap<String, List<String>>();
	private Map<String, List<String>> vimetricMap = new ConcurrentHashMap<String, List<String>>();
	private Map<String, List<String>> clusterHostsMap = new ConcurrentHashMap<String, List<String>>();

	public List<String> getMetricsByCluster(String cluster) {
		List<String> metricList = metricMap.get(cluster);
		if (metricList == null || metricList.isEmpty()) {
			try {
				initClusterInfo(false);
				return metricMap.get(cluster);
			} catch (Exception e) {
				logger.error("Ganglia Http Parser Error", e);
				throw new RuntimeException("Ganglia Http Parser Error", e);
			}
		}

		return metricList;
	}

	public List<String> getVIMetricsByCluster(String cluster) {
		List<String> metricList = vimetricMap.get(cluster);
		if (metricList == null || metricList.isEmpty()) {
			try {
				initVIMap(false);
				return vimetricMap.get(cluster);
			} catch (Exception e) {
				logger.error("Ganglia Http Parser Error", e);
				throw new RuntimeException("Ganglia Http Parser Error", e);
			}
		}

		return metricList;
	}

	public List<String> getHostListByCluster(String cluster) {
		List<String> hostList = clusterHostsMap.get(cluster);
		if (hostList == null || hostList.isEmpty()) {
			try {
				initClusterInfo(false);
				return vimetricMap.get(cluster);
			} catch (Exception e) {
				logger.error("Ganglia Http Parser Error", e);
				throw new RuntimeException("Ganglia Http Parser Error", e);
			}
		}

		return hostList;
	}

	@Scheduled(fixedDelay = 3600 * 1000)
	public void start() throws Exception {
		logger.info("Start Initialize Cluster Information:"
				+ System.currentTimeMillis());
		initClusterInfo(true);
		logger.info("End Initialize Cluster Information:"
				+ System.currentTimeMillis());

		logger.info("Start Initialize Very Important Metric Information:"
				+ System.currentTimeMillis());
		initVIMap(true);
		logger.info("End Initialize Very Important Metric Information:"
				+ System.currentTimeMillis());
	}

	private synchronized void initVIMap(boolean force) throws JAXBException {
		if (vimetricMap.isEmpty() || force) {
			Clusters clusters = configParser.getClusters();
			List<Cluster> clusterList = clusters.getCluster();
			for (Cluster cluster : clusterList) {
				List<Metric> metricList = cluster.getMetrics().getMetric();
				List<String> metricStrList = new ArrayList<String>();
				for (Metric metric : metricList) {
					metricStrList.add(metric.getName());
				}
				if (metricStrList.size() != 0) {
					vimetricMap.put(cluster.getName(), metricStrList);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized void initClusterInfo(boolean force) {
		if (clusterHostsMap.isEmpty() || metricMap.isEmpty() || force) {
			String json = UrlUtils.getContent(hostUrl);
			JSONObject jsonObject = JSONObject.fromObject(json);
			JSONObject messagejson = (JSONObject) jsonObject.get("message");
			JSONObject clustersJson = (JSONObject) messagejson.get("clusters");

			for (String cluster : (Set<String>) clustersJson.keySet()) {
				try {

					JSONArray hostArray = (JSONArray) clustersJson.get(cluster);
					List<String> hostList = new ArrayList<String>();
					Iterator<String> iterator = hostArray.iterator();
					while (iterator.hasNext()) {
						String host = iterator.next();
						hostList.add(host);
					}
					if (hostList.size() != 0) {
						clusterHostsMap.put(cluster, hostList);
					}

					List<String> metricList = httpParser
							.getGangliaAttribute(cluster);
					if (metricList != null && metricList.size() != 0) {
						metricMap.put(cluster, metricList);
					}
				} catch (Exception e) {
					logger.error("Ganglia Http Parser Error", e);
					throw new RuntimeException("Ganglia Http Parser Error", e);
				}
			}
		}

	}

}
