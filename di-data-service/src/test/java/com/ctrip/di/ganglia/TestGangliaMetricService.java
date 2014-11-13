package com.ctrip.di.ganglia;

import java.util.Set;

import net.sf.json.JSONObject;

import com.ctrip.di.common.util.UrlUtils;

public class TestGangliaMetricService {

	public static void main(String[] args) {
		String json = UrlUtils
				.getContent("http://10.8.75.3/ganglia/?r=hour&cs=&ce=&s=by+name&c=Hbase_Dashboard_Cluster&tab=m&vn=&hide-hf=false");
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject messagejson = (JSONObject) jsonObject.get("message");
		JSONObject clustersJson = (JSONObject) messagejson.get("clusters");
		@SuppressWarnings("unchecked")
		Set<String> test = clustersJson.keySet();
		for (String cluster : test) {
			System.out.println(cluster);
		}
	}

}
