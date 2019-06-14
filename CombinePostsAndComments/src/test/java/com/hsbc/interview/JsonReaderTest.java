package com.hsbc.interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for JsonReader.
 * 
 * @author Marcin Drewnowski
 * @version 1.0
 * @since 2019-06-12
 */
public class JsonReaderTest {
	private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
	private static final String TEST_STRING = "Test";
	private JsonReader jsonReader;

	@Before
	public void initialize() {
		jsonReader = new JsonReader();
	}

	@Test
	public void testReadingUrl() throws IOException {
		String jsonText = jsonReader.readUrl(COMMENTS_URL);
		Assert.assertNotNull(jsonText);
	}

	@Test
	public void testReadingFromGivenReader() throws IOException {
		BufferedReader rd = new BufferedReader(new StringReader(TEST_STRING));
		String jsonText = jsonReader.readAll(rd);
		Assert.assertEquals(jsonText, TEST_STRING);
	}

}
