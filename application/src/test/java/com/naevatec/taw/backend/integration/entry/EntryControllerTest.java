package com.naevatec.taw.backend.integration.entry;

import com.google.gson.Gson;
import com.naevatec.taw.backend.AbstractLoggedControllerUnitTest;
import com.naevatec.taw.backend.comment.Comment;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.entry.Entry;
import com.naevatec.taw.backend.utils.CourseTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class EntryControllerTest extends AbstractLoggedControllerUnitTest {
	
	
	private static String newEntry_uri ="/api-entries/forum/";
	
	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@Test
	public void newForumEntryControllerTest() {
		
		Course c = CourseTestUtils.newCourseWithCd("Test Forum", loggedUser, null, "this is the info", true);
		c = CourseTestUtils.createCourseIfNotExist(mvc, c, httpSession);

		long forumId = c.getCourseDetails().getForum().getId();
		long cdId = c.getCourseDetails().getId();
		
		
		Comment cm = new Comment("This is the message", System.currentTimeMillis(), loggedUser);
		Entry entry = new Entry("Test Entry",System.currentTimeMillis(),loggedUser);
		entry.getComments().add(cm);
		
		assertTrue((forumId>-1)&&(cdId>-1));
		
		Gson gson = new Gson();
		String entry_request = gson.toJson(entry);
		
		//test ok 
		try {
			
			MvcResult result =  mvc.perform(post(newEntry_uri+cdId)
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                .content(entry_request)
					                ).andReturn();
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.CREATED.value();
			
			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test OK");
		}
		//test UNAUTHORIZED 
		try {
			
			MvcResult result =  mvc.perform(post(newEntry_uri+forumId)//fakeID
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .content(entry_request+String.valueOf(cdId))
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
			
			MvcResult result =  mvc.perform(post(newEntry_uri+"not_a_id")//fakeID
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
