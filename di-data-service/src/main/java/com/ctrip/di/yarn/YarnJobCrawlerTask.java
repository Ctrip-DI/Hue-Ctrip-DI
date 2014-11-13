package com.ctrip.di.yarn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cloudera.org.apache.http.HttpStatus;
import com.ctrip.di.dao.YarnJobService;
import com.ctrip.di.dao.YarnJobsDo;

/**
 * A task to crawl the jobs from yarn cluster and insert into database
 * @author xgliao
 *
 */
@Component
public class YarnJobCrawlerTask {
	private static Log logger = LogFactory.getLog(YarnJobCrawlerTask.class);

	@Autowired
	private YarnJobService yarnJobService;

	@Value("${YARN_HISTORY_JOB_URL}")
	private String yarnHistoryJobUrl;

	private JSONObject getJson() throws HttpException, IOException {
		HttpClient httpClient = new HttpClient();
		GetMethod method = new GetMethod(yarnHistoryJobUrl);
		HttpMethodParams methodParams = new HttpMethodParams();
		methodParams.setParameter("Content-Type", "application/json");
		method.setParams(methodParams);
		httpClient.executeMethod(method);
		StringBuilder sb = new StringBuilder();
		if (method.getStatusCode() == HttpStatus.SC_OK) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						method.getResponseBodyAsStream(), "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (Exception e) {
				logger.error("Exception:", e);
				throw new RuntimeException("Get json failed,", e);
			} finally {
				if (reader != null)
					reader.close();
			}
		}

		JSONObject jsonObject = JSONObject.fromObject(sb.toString());

		return jsonObject;
	}

	@Scheduled(fixedDelay = 7200 * 1000)
	public void run() {
		try {
			long maxStartTime = yarnJobService.getMaxStartTime();
			List<YarnJobsDo> jobList = getJobList(maxStartTime);
			logger.info("Get yarn job size is " + jobList.size());
			yarnJobService.insertJobs(jobList);
		} catch (HttpException e) {
			logger.error("Http Exception", e);
		} catch (Throwable e) {
			logger.error("IOException", e);
		}
	}

	private List<YarnJobsDo> getJobList(long maxStartTime)
			throws HttpException, IOException {
		List<YarnJobsDo> jobsList = new ArrayList<YarnJobsDo>();

		JSONObject json = getJson();
		JSONObject jobs = (JSONObject) json.get("jobs");
		JSONArray jobArray = (JSONArray) jobs.get("job");
		@SuppressWarnings("unchecked")
		ListIterator<JSONObject> iterator = jobArray.listIterator();
		while (iterator.hasNext()) {
			JSONObject jobObject = iterator.next();
			long startTime = jobObject.getLong("startTime");
			if (startTime > maxStartTime) {
				YarnJobsDo yarnJobsDo = toYarnJobsDo(jobObject);
				jobsList.add(yarnJobsDo);
			}
		}
		return jobsList;
	}

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private YarnJobsDo toYarnJobsDo(JSONObject jobObject) {
		YarnJobsDo jobDo = new YarnJobsDo();
		jobDo.setStartTime(jobObject.getLong("startTime"));
		jobDo.setDateStr(dateFormat.format(new Date(jobObject
				.getLong("startTime"))));
		jobDo.setFinishTime(jobObject.getLong("finishTime"));
		jobDo.setJobId(jobObject.getString("id"));
		jobDo.setQueue(jobObject.getString("queue"));
		jobDo.setUser(jobObject.getString("user"));
		jobDo.setStatus(jobObject.getString("state"));
		jobDo.setMapsTotal(jobObject.getInt("mapsTotal"));
		jobDo.setReducesTotal(jobObject.getInt("reducesTotal"));

		return jobDo;
	}

}
