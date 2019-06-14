package com.hsbc.interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Class to download JSON from given path
 *
 * @author Marcin Drewnowski
 * @version 1.0
 * @since 2019-06-12
 */

public class JsonReader {

	/**
	 * This method is used download JSON from given path
	 * 
	 * @param urlString URL to JSON resource
	 * @return String This returns JSON as String
	 */
	protected String readUrl(String urlString) throws IOException {
		InputStream is = new URL(urlString).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return jsonText;
		} finally {
			is.close();
		}
	}

	protected String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
