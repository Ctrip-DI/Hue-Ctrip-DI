package com.ctrip.di.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Get the content from the url
 * @author xgliao
 *
 */
public class UrlUtils {

	public static String getContent(String urlStr) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();

			inputStreamReader = new InputStreamReader(conn.getInputStream());
			br = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException("Url connection failed:" + urlStr, e);
		} finally {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					// Ignore
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
		return sb.toString();
	}

}
