package com.ctrip.di;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import net.sf.json.JSONObject;

import com.ctrip.di.common.jmx.YarnJmxBean;

public class TestJmx {

	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://192.168.81.176:8026/jmxrmi");

		JMXConnector jmxc = JMXConnectorFactory.connect(url);

		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		YarnJmxBean yarnJmxBean = new YarnJmxBean();

		ObjectName on = new ObjectName(
				"Hadoop:service=ResourceManager,name=QueueMetrics,q0=root");
		Object appsCompleted = mbsc.getAttribute(on, "AppsCompleted");
		if (appsCompleted != null) {
			yarnJmxBean.setAppsCompleted((Integer) appsCompleted);
		}

		Object appsFailed = mbsc.getAttribute(on, "AppsFailed");
		if (appsFailed != null) {
			yarnJmxBean.setAppsFailed((Integer) appsFailed);
		}

		Object appsKilled = mbsc.getAttribute(on, "AppsKilled");
		if (appsKilled != null) {
			yarnJmxBean.setAppsKilled((Integer) appsKilled);
		}

		Object appsPending = mbsc.getAttribute(on, "AppsPending");
		if (appsPending != null) {
			yarnJmxBean.setAppsPending((Integer) appsPending);
		}

		Object appsRunning = mbsc.getAttribute(on, "AppsRunning");
		if (appsRunning != null) {
			yarnJmxBean.setAppsRunning((Integer) appsRunning);
		}

		Object appsSubmitted = mbsc.getAttribute(on, "AppsSubmitted");
		if (appsSubmitted != null) {
			yarnJmxBean.setAppsSumitted((Integer) appsSubmitted);
		}

		ObjectName rmon = new ObjectName(
				"Hadoop:service=ResourceManager,name=RMNMInfo");
		Object liveNodeManagers = mbsc.getAttribute(rmon, "LiveNodeManagers");
		if (liveNodeManagers != null) {
			yarnJmxBean.setLiveNodeManagers((String) liveNodeManagers);
		}

		JSONObject json = JSONObject.fromObject(yarnJmxBean);
		System.out.println(json);

		/*
		 * Set mbeans = mbsc.getMBeanInfo(on); Iterator iter =
		 * mbeans.iterator(); while (iter.hasNext()) { ObjectInstance oi =
		 * (ObjectInstance) iter.next(); ObjectName objectName =
		 * oi.getObjectName(); System.out.println("ObjectName: " + objectName);
		 * MBeanInfo mi = mbsc.getMBeanInfo(objectName); MBeanAttributeInfo[]
		 * mais = mi.getAttributes(); for (int i = 0; i < mais.length; i++) {
		 * MBeanAttributeInfo mai = mais[i]; String attributeName =
		 * mai.getName(); Object value = mbsc.getAttribute(objectName,
		 * attributeName); System.out.println("Attribute:" + attributeName + "="
		 * + value); }
		 * 
		 * }
		 */
	}

}
