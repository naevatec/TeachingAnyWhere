package com.naevatec.taw.backend.integration.forum;

import com.naevatec.taw.backend.AbstractLoggedControllerUnitTest;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.utils.CourseTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class ForumControllerTest extends AbstractLoggedControllerUnitTest {
	
	private static String toggleForum_uri = "/api-forum/edit/";
	
	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@Test
	public void toggleForumTest() {
		
		Course c = CourseTestUtils.newCourseWithCd("Course Title", loggedUser, null, "this is the info", false);
		c = CourseTestUtils.createCourseIfNotExist(mvc, c, httpSession);

		//test ok 
		try {
			MvcResult result =  mvc.perform(put(toggleForum_uri+c.getCourseDetails().getId())//fakeID
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                .content("true")
					                ).andReturn();
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.OK.value();
			
			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test OK");
		}
		//test UNAUTHORIZED 
		try {
			
			MvcResult result =  mvc.perform(put(toggleForum_uri+c.getCourseDetails().getId())//fakeID
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .content("true")
					                ).andReturn();
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.UNAUTHORIZED.value();
			
			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test UNAUTHORIZED");
		}
		
		//test BAD_REQUEST 
		try {
			
			MvcResult result =  mvc.perform(put(toggleForum_uri+"not_a_id")//fakeID
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                ).andReturn();
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.BAD_REQUEST.value();
			
			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test BAD_REQUEST");
		}
		
	}

}
