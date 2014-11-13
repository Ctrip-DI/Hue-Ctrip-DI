package com.ctrip.di.ganglia;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ganglia http Parser: get ganglia attribute by cluster name.
 * And return a list of attribute
 * @author xgliao
 *
 */
@Component
public class GangliaHttpParser {
	@Value("${GANGLIA_METRIC_URL}")
	private String gangliaMetricUrl;
	@Value("${GANGLIA_METRIC_URL_ClUSTER_PATTERN}")
	private String clusterPattern;

	public List<String> getGangliaAttribute(String clusterName)
			throws ParserException, MalformedURLException, IOException {
		String url = gangliaMetricUrl.replaceAll(clusterPattern, clusterName);
		Parser parser = new Parser(new URL(url).openConnection());
		NodeFilter nodeFilter = new AndFilter(new TagNameFilter("select"),
				new HasAttributeFilter("id", "metrics-picker"));
		NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
		SimpleNodeIterator iterator = nodeList.elements();
		List<String> metricList = new ArrayList<String>();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();

			SimpleNodeIterator childIterator = node.getChildren().elements();
			while (childIterator.hasMoreNodes()) {
				OptionTag children = (OptionTag) childIterator.nextNode();
				metricList.add(children.getOptionText());
			}
		}

		return metricList;

	}

}
