package com.ctrip.di.common.jmx;

import javax.annotation.PostConstruct;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Abstract class for jmx service
 * @author xgliao
 *
 */
public abstract class AbstractJmxService {
	private static Log logger = LogFactory.getLog(AbstractJmxService.class);

	protected MBeanServerConnection mbsc;

	protected volatile JSONObject json = new JSONObject();

	@PostConstruct
	public void initConnection() throws Exception {
		JMXServiceURL url = new JMXServiceURL(getJmxServer());

		JMXConnector jmxc = JMXConnectorFactory.connect(url);

		mbsc = jmxc.getMBeanServerConnection();
	}

	/**
	 * provide jmx server url
	 * @return string
	 */
	protected abstract String getJmxServer();

	/**
	 * Get newer jmx bean
	 * @return jsonobject
	 * @throws Exception
	 */
	protected abstract JSONObject getNewJmxBean() throws Exception;

	/**
	 * Get a jmx bean
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJmxBean() throws Exception {
		if (json.isEmpty()) {
			synchronized (json) {
				if (json.isEmpty()) {
					JSONObject newJson = getNewJmxBean();
					json = newJson;
				}
			}
		}

		return json;
	}

	/**
	 * Run every half hour
	 * @throws Exception
	 */
	@Scheduled(fixedDelay = 1800 * 1000)
	public void run() throws Exception {
		logger.info("Start get new jmx bean while jmx server is "
				+ getJmxServer());
		JSONObject newJson = getNewJmxBean();
		logger.info("End get new jmx bean while jmx server is "
				+ getJmxServer());
		json = newJson;
	}

}
