/*package com.ctrip.di.common.jmx;

import javax.management.ObjectName;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class YarnJmxService extends AbstactJmxService {

	@Value("${YARN_JMX_SERVER}")
	private String yarnJmxServer;

	@Override
	protected String getJmxServer() {
		return yarnJmxServer;
	}

	@Override
	protected JSONObject getNewJmxBean() throws Exception {
		YarnJmxBean yarnJmxBean = new YarnJmxBean();
		
		ObjectName on = new ObjectName(
				"Hadoop:service=ResourceManager,name=QueueMetrics,q0=root");
		Object appsCompleted = mbsc.getAttribute(on, "AppsCompleted");
		if (appsCompleted != null) {
			yarnJmxBean.setAppsCompleted((Integer)appsCompleted);
		}
		
		Object appsFailed = mbsc.getAttribute(on, "AppsFailed");
		if (appsFailed != null) {
			yarnJmxBean.setAppsFailed((Integer)appsFailed);
		}
		
		Object appsKilled = mbsc.getAttribute(on, "AppsKilled");
		if (appsKilled != null) {
			yarnJmxBean.setAppsKilled((Integer)appsKilled);
		}
		
		Object appsPending = mbsc.getAttribute(on, "AppsPending");
		if (appsPending != null) {
			yarnJmxBean.setAppsPending((Integer)appsPending);
		}
		
		Object appsRunning = mbsc.getAttribute(on, "AppsRunning");
		if (appsRunning != null) {
			yarnJmxBean.setAppsRunning((Integer)appsRunning);
		}
		
		Object appsSubmitted = mbsc.getAttribute(on, "AppsSubmitted");
		if (appsSubmitted != null) {
			yarnJmxBean.setAppsSumitted((Integer)appsSubmitted);
		}
		
		ObjectName rmon = new ObjectName(
				"Hadoop:service=ResourceManager,name=RMNMInfo");
		Object liveNodeManagers = mbsc.getAttribute(rmon, "LiveNodeManagers");
		if (liveNodeManagers != null) {
			yarnJmxBean.setLiveNodeManagers((String)liveNodeManagers);
		}

		JSONObject json = JSONObject.fromObject(yarnJmxBean);
		return json;
	}

}
*/