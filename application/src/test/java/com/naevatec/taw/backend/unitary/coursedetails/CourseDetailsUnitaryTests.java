package com.naevatec.taw.backend.unitary.coursedetails;

import com.naevatec.taw.backend.AbstractUnitTest;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.coursedetails.CourseDetails;
import com.naevatec.taw.backend.filegroup.FileGroup;
import com.naevatec.taw.backend.forum.Forum;
import com.naevatec.taw.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseDetailsUnitaryTests extends AbstractUnitTest {

	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	public void newCourseDetailsTest() {
		String[] roles = {"TEACHER"};
		User u =  new User("mock", "Pass1234", "mock", null, roles);

		CourseDetails cd = new CourseDetails();
		assertNotNull(cd);
		
		Course c = new Course("to modify", "/../assets/images/default_session_image.png", u);

		CourseDetails cd2 = new CourseDetails(c);
		
		assertNotNull(cd2);
		assertTrue(cd2.getCourse().equals(c));
		
	}

	@Test
	public void setAndGetCourseDetailsInfoTest() {
		CourseDetails cd = new CourseDetails();
		cd.setInfo("this is info");
		assertNotNull(cd);
		assertTrue("this is info".equals(cd.getInfo()));
	}

	@Test
	public void setAndGetCourseDetailsForumTest() {
		CourseDetails cd = new CourseDetails();
		Forum forum = new Forum();
		cd.setForum(forum);
		assertNotNull(cd);
		assertTrue(forum.equals(cd.getForum()));
	}

	@Test
	public void setAndGetCourseDetailsFilesTest() {
		CourseDetails cd = new CourseDetails();
		List<FileGroup> files= new ArrayList<FileGroup>();
		cd.setFiles(files);
		assertNotNull(cd);
		assertTrue(files.equals(cd.getFiles()));
	}

	@Test
	public void SetAndGetCourseDetailsCourseTest() {
		CourseDetails cd = new CourseDetails();
		String[] roles = {"TEACHER"};
		User u =  new User("mock", "Pass1234", "mock", null, roles);

		Course c = new Course("to modify", "/../assets/images/default_session_image.png", u);

		cd.setCourse(c);
		assertNotNull(cd);
		assertTrue(cd.getCourse().equals(c));

	}

}
