package com.naevatec.taw.backend.course;

import com.fasterxml.jackson.annotation.JsonView;
import com.naevatec.taw.backend.coursedetails.CourseDetails;
import com.naevatec.taw.backend.session.Session;
import com.naevatec.taw.backend.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course implements Serializable {
	
	private static final long serialVersionUID = -1234594587165771952L;

	public interface SimpleCourseList {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(SimpleCourseList.class)
	private long id;
	
	@JsonView(SimpleCourseList.class)
	private String title;
	
	@JsonView(SimpleCourseList.class)
	private String image;
	
	@ManyToOne
	private User teacher;
	
	@OneToOne(cascade=CascadeType.ALL)
	private CourseDetails courseDetails;
	
	@JsonView(SimpleCourseList.class)
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="course")
	private Set<Session> sessions;
	
	@ManyToMany
	private Set<User> attenders;
	
	public Course() {}
	
	public Course(String title, String image, User teacher) {
		this.title = title;
		this.image = image;
		this.teacher = teacher;
		this.courseDetails = null;
		this.sessions = new HashSet<>();
		this.attenders = new HashSet<>();
	}

	public Course(String title, String image, User teacher, CourseDetails courseDetails) {
		this.title = title;
		this.image = image;
		this.teacher = teacher;
		this.courseDetails = courseDetails;
		this.sessions = new HashSet<>();
		this.attenders = new HashSet<User>();
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
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public CourseDetails getCourseDetails() {
		return courseDetails;
	}

	public void setCourseDetails(CourseDetails courseDetails) {
		this.courseDetails = courseDetails;
	}

	public Set<User> getAttenders() {
		return attenders;
	}

	public void setAttenders(Set<User> attenders) {
		this.attenders = attenders;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}
	
	//To make 'user.getCourse().remove(course)' possible
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Course))return false;
	    Course otherCourse = (Course)other;
	    return (otherCourse.id == this.id);
	}
}
