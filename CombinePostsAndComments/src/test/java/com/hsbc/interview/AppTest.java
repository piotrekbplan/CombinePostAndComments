package com.hsbc.interview;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hsbc.interview.App;
import com.hsbc.interview.model.Comment;
import com.hsbc.interview.model.Post;
import com.hsbc.interview.model.ResultsWrapper;

/**
 * JUnit test for App.
 * 
 * @author Marcin Drewnowski
 * @version 1.0
 * @since 2019-06-12
 */
public class AppTest {
	private static final String TEST_STRING = "Test";
	private static final String NEW_LINE_JSON = "\n";
	private static final String NEW_LINE_XML = "&#13;";
	private static final String RESULT_FILENAME = "results.xml";
	private static final String PREPARED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<root/>\n";
	private Comment[] comments;
	private Post[] posts;

	@Before
	public void initialize() {
		comments = new Comment[2];
		Comment comment = new Comment();
		comment.setBody(TEST_STRING.concat(NEW_LINE_JSON).concat(TEST_STRING));
		comment.setName(TEST_STRING.concat(NEW_LINE_JSON).concat(TEST_STRING));
		comment.setPostId(0);
		comments[0] = comment;
		Comment emptyComment = new Comment();
		emptyComment.setBody("");
		emptyComment.setName("");
		emptyComment.setPostId(1);
		comments[1] = emptyComment;

		posts = new Post[1];
		Post post = new Post();
		post.setTitle(TEST_STRING.concat(NEW_LINE_JSON).concat(TEST_STRING));
		post.setBody(TEST_STRING.concat(NEW_LINE_JSON).concat(TEST_STRING));
		post.setId(0);
		posts[0] = post;
	}

	@Test
	public void testReplaceNewLineInBodyComment() {
		App.replaceNewLineCharacters(comments, posts);
		Assert.assertEquals(comments[0].getBody(), TEST_STRING.concat(NEW_LINE_XML).concat(TEST_STRING));
	}

	@Test
	public void testReplaceNewLineInNameComment() {
		App.replaceNewLineCharacters(comments, posts);
		Assert.assertEquals(comments[0].getName(), TEST_STRING.concat(NEW_LINE_XML).concat(TEST_STRING));
	}

	@Test
	public void testReplaceNewLineInTitlePost() {
		App.replaceNewLineCharacters(comments, posts);
		Assert.assertEquals(posts[0].getTitle(), TEST_STRING.concat(NEW_LINE_XML).concat(TEST_STRING));
	}

	@Test
	public void testReplaceNewLineInTitleBody() {
		App.replaceNewLineCharacters(comments, posts);
		Assert.assertEquals(posts[0].getBody(), TEST_STRING.concat(NEW_LINE_XML).concat(TEST_STRING));
	}

	@Test
	public void testSaveFile() throws IOException {
		StringWriter stringWriter = new StringWriter();
		stringWriter.append(TEST_STRING);
		App.saveFile(stringWriter);
		File file = new File(RESULT_FILENAME);

		// assert that the file is not empty
		Assert.assertTrue(file.length() > 0);
	}

	@Test
	public void testToFindRelatedPoCommentsForPosts() throws IOException {
		ArrayList<Comment> commentsList = new ArrayList<Comment>();
		App.addRelatedComments(comments, posts[0], commentsList);
		Assert.assertTrue(commentsList.size() == 1);
	}

	@Test
	public void testNumberOfCombinedPosts() throws IOException {
		ResultsWrapper results = App.combinePostsAndComments(comments, posts);
		Assert.assertTrue(results.getPostsWithComments().get(0).getComments().size() == 1);
	}

	@Test
	public void testCommentBodyInResultWrapper() throws IOException {
		ResultsWrapper results = App.combinePostsAndComments(comments, posts);
		Assert.assertTrue(results.getPostsWithComments().get(0).getComments().get(0).getBody()
				.equals(TEST_STRING.concat(NEW_LINE_JSON).concat(TEST_STRING)));
	}

	@Test
	public void testPreparingXmlSchema() throws IOException, JAXBException {
		ResultsWrapper results = new ResultsWrapper();
		Assert.assertTrue(App.prepareXml(results).toString().equals(PREPARED_XML));
	}
}
