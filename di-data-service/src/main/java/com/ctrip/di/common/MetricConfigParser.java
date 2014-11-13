package com.ctrip.di.common;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctrip.di.pojo.gen.Clusters;

/**
 * Metric Config Parser
 * To get user defined Metric Configuration, which can be show 
 * in Critical metric report
 * @author xgliao
 *
 */
@Component
public class MetricConfigParser {
	@Value("${METRIC_CONFIG_FILE}")
	private String metricConfigFile;

	public Clusters getClusters() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Clusters.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Clusters clusters = (Clusters) unmarshaller
				.unmarshal(MetricConfigParser.class.getClassLoader()
						.getResourceAsStream(metricConfigFile));

		return clusters;
	}

}
