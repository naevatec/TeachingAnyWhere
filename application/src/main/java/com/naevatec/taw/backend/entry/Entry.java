package com.naevatec.taw.backend.entry;

import com.naevatec.taw.backend.comment.Comment;
import com.naevatec.taw.backend.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Entry implements Serializable{

	private static final long serialVersionUID = -8235742718749641924L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String title;
	
	private long date;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Comment> comments;
	
	@ManyToOne
	private User user;
	
	public Entry() {}
	
	public Entry(String title, long date, User user) {
		this.title = title;
		this.date = date;
		this.user = user;
		this.comments = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
