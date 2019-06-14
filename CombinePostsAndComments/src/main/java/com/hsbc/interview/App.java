package com.hsbc.interview;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.NoEscapeHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hsbc.interview.model.Comment;
import com.hsbc.interview.model.Post;
import com.hsbc.interview.model.PostWithComments;
import com.hsbc.interview.model.ResultsWrapper;

/**
 * Application to download posts and comments from given URL, combine both and
 * generate XML file
 *
 * @author Marcin Drewnowski
 * @version 1.0
 * @since 2019-06-12
 */

public class App {
	private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
	private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
	private static final String RESULT_FILENAME = "results.xml";
	private static final String NEW_LINE_JSON = "\n";
	private static final String NEW_LINE_XML = "&#13;";

	/**
	 * This is the main method which gets JSON, parses and saves results as XML file
	 * 
	 * @param args Unused.
	 * @return Nothing.
	 * @exception JAXBException in case of problems with converting POJO to XML.
	 * @exception IOException   On input error.
	 */

	public static void main(String[] args) throws JAXBException, IOException {
		// get JSON from given URL
		JsonReader jsonReader = new JsonReader();
		String commentsString = jsonReader.readUrl(COMMENTS_URL);
		String postsString = jsonReader.readUrl(POSTS_URL);

		// parse JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Comment[] comments = gson.fromJson(commentsString, Comment[].class);
		Post[] posts = gson.fromJson(postsString, Post[].class);

		// replace new line characters from "\n" to "&#13;"
		replaceNewLineCharacters(comments, posts);

		// combine posts and comments
		ResultsWrapper results = combinePostsAndComments(comments, posts);

		// prepare XML
		StringWriter preparedXml = prepareXml(results);

		// save file
		saveFile(preparedXml);
	}

	protected static void replaceNewLineCharacters(Comment[] comments, Post[] posts) {
		for (Comment comment : comments) {
			comment.setBody(comment.getBody().replaceAll(NEW_LINE_JSON, NEW_LINE_XML));
			comment.setName(comment.getName().replaceAll(NEW_LINE_JSON, NEW_LINE_XML));
		}

		for (Post post : posts) {
			post.setTitle(post.getTitle().replaceAll(NEW_LINE_JSON, NEW_LINE_XML));
			post.setBody(post.getBody().replaceAll(NEW_LINE_JSON, NEW_LINE_XML));
		}
	}

	protected static void saveFile(StringWriter preparedXml) throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(RESULT_FILENAME));
		try {
			out.print(preparedXml.toString());
		} finally {
			out.close();
		}
	}

	protected static StringWriter prepareXml(ResultsWrapper results) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ResultsWrapper.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		// removed escaping characters due to problem with new line character "&#13;"
		CharacterEscapeHandler escapeHandler = NoEscapeHandler.theInstance;
		jaxbMarshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", escapeHandler);
		jaxbMarshaller.marshal(results, sw);
		return sw;
	}

	protected static ResultsWrapper combinePostsAndComments(Comment[] comments, Post[] posts) {
		List<PostWithComments> postsWithComments = new ArrayList<PostWithComments>();
		for (Post post : posts) {
			PostWithComments postWithComments = new PostWithComments();
			postWithComments.setId(post.getId());
			postWithComments.setUserId(post.getUserId());
			postWithComments.setTitle(post.getTitle());
			postWithComments.setBody(post.getBody());

			ArrayList<Comment> commentsList = new ArrayList<Comment>();
			addRelatedComments(comments, post, commentsList);
			postWithComments.setComments(commentsList);
			postsWithComments.add(postWithComments);
		}

		ResultsWrapper results = new ResultsWrapper();
		results.setPostsWithComments(postsWithComments);
		return results;
	}

	protected static void addRelatedComments(Comment[] comments, Post post, ArrayList<Comment> commentsList) {
		for (Comment comment : comments) {
			if (comment.getPostId() == post.getId()) {
				commentsList.add(comment);
			}
		}
	}
}