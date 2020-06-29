package com.naevatec.taw.backend.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.course.Course.SimpleCourseList;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Session implements Serializable{
	
	private static final long serialVersionUID = 6426900403882469733L;

	@JsonView(SimpleCourseList.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JsonView(SimpleCourseList.class)
	private String title;
	
	@JsonView(SimpleCourseList.class)
	private String description;
	
	@JsonView(SimpleCourseList.class)
	private long date;
	
	@JsonIgnore
	@ManyToOne
	private Course course;
	
	public Session() {}
	
	public Session(String title, String description, long date) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.course = null;
	}

	public Session(String title, String description, long date, Course course) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.course = course;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	//To make 'course.getSessions().remove(session)' possible
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Session))return false;
	    Session otherSession = (Session)other;
	    return (otherSession.id == this.id);
	}
	
}
