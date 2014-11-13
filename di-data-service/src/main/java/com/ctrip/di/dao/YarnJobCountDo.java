package com.ctrip.di.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Yarn job count data object
 * @author xgliao
 *
 */
public class YarnJobCountDo {
	private static Log logger = LogFactory.getLog(YarnJobCountDo.class);

	private String dateStr;
	private long dateTime;
	private int totalCount;
	private int successCount;
	private int failCount;
	private int errorCount;
	private int killCount;

	private int unsuccessCount;
	private double successRate;
	private double unsuccessRate;
	private double failRate;
	private double killRate;
	private double errorRate;

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public double getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(double successRate) {
		this.successRate = successRate;
	}

	public double getUnsuccessRate() {
		return unsuccessRate;
	}

	public void setUnsuccessRate(double unsuccessRate) {
		this.unsuccessRate = unsuccessRate;
	}

	public double getFailRate() {
		return failRate;
	}

	public void setFailRate(double failRate) {
		this.failRate = failRate;
	}

	public double getKillRate() {
		return killRate;
	}

	public void setKillRate(double killRate) {
		this.killRate = killRate;
	}

	public int getUnsuccessCount() {
		return unsuccessCount;
	}

	public void setUnsuccessCount(int unsuccessCount) {
		this.unsuccessCount = unsuccessCount;
	}

	public double getErrorRate() {
		return errorRate;
	}

	public void setErrorRate(double errorRate) {
		this.errorRate = errorRate;
	}

	public void initRate() {
		try {
			dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
					.getTime();
		} catch (ParseException e) {
			// Ignore
			logger.error("Parse Exception", e);
		}
		if (totalCount == 0) {
			return;
		}
		successRate = div(successCount, totalCount);
		errorRate = div(errorCount, totalCount);
		killRate = div(killCount, totalCount);
		failRate = (10000 - (int) (100 * successRate) - (int) (100 * errorRate) - (int) (100 * killRate))
				/ (double) 100;
		unsuccessRate = (10000 - (int) (100 * successRate)) / (double) 100;
	}

	private double div(int num, int denom) {
		double result = (int) ((num / (double) denom) * 10000) / (double) 100;
		return result;
	}

	public static void main(String[] args) {
		YarnJobCountDo countDo = new YarnJobCountDo();
		countDo.setDateStr("2014-08-25");
		countDo.setTotalCount(13823);
		countDo.setSuccessCount(13780);
		countDo.setFailCount(33);

		JSONObject json = JSONObject.fromObject(countDo);
		System.out.println(json.toString());
	}

}
