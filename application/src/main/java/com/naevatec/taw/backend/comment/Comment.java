package com.naevatec.taw.backend.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.naevatec.taw.backend.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment implements Serializable{
	
	private static final long serialVersionUID = 1727989132504027717L;

	public interface CommentNoParent {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String message;
	
	private long date;
	
	@OneToMany(mappedBy="commentParent", cascade=CascadeType.ALL)
	@JsonManagedReference
	private List<Comment> replies;
	
	@ManyToOne
	@JsonBackReference
	private Comment commentParent;
	
	@ManyToOne
	private User user;
	
	public Comment() {}
	
	public Comment(String message, long date, User user) {
		this.message = message;
		this.date = date;
		this.user = user;
		this.replies = new ArrayList<Comment>();
		this.commentParent = null;
	}
	
	public Comment(String message, long date, User user, Comment commentParent) {
		this.message = message;
		this.date = date;
		this.user = user;
		this.replies = new ArrayList<Comment>();
		this.commentParent = commentParent;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Comment getCommentParent() {
		return commentParent;
	}

	public void setCommentParent(Comment commentParent) {
		this.commentParent = commentParent;
	}

}
