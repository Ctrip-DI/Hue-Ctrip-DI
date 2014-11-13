package com.ctrip.di.ganglia;

import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class TestGangliaHttpParser {
	
	public static void main(String[] args) throws Exception {
		Parser parser = new Parser(new URL("http://10.8.75.3/ganglia/?r=hour&cs=&ce=&s=by+name&c=Zookeeper_Cluster&tab=m&vn=&hide-hf=false").openConnection());
		NodeFilter nodeFilter = new AndFilter(new TagNameFilter("select"),
				new HasAttributeFilter("id", "metrics-picker"));
		NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
		SimpleNodeIterator iterator = nodeList.elements();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();

			SimpleNodeIterator childIterator = node.getChildren().elements();
			while (childIterator.hasMoreNodes()) {
				OptionTag children = (OptionTag) childIterator.nextNode();
				System.out.println(children.getOptionText());
			}
		}

	}

}
