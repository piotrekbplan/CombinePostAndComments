package com.hsbc.interview.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultsWrapper {
	@XmlElement(name = "post")
	private List<PostWithComments> postsWithComments;

	public List<PostWithComments> getPostsWithComments() {
		return postsWithComments;
	}

	public void setPostsWithComments(List<PostWithComments> postsWithComments) {
		this.postsWithComments = postsWithComments;
	}
}