package com.naevatec.taw.backend.integration.comment;

import com.google.gson.Gson;
import com.naevatec.taw.backend.AbstractLoggedControllerUnitTest;
import com.naevatec.taw.backend.comment.Comment;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.entry.Entry;
import com.naevatec.taw.backend.utils.CourseTestUtils;
import com.naevatec.taw.backend.utils.ForumTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CommentControllerTest extends AbstractLoggedControllerUnitTest {

	private static String newComment_uri ="/api-comments/entry/{entryId}/forum/";
	
	private static String courseTitle = "Course Title";
	private static String info ="Course information";
	private static boolean forum = true;
	
	
	@BeforeEach
	public void setUp() {
		super.setUp();
		
	}

	@Rollback
	@Test
	public void newCommentTest() {
		
	
		Course c = CourseTestUtils.newCourseWithCd(courseTitle, loggedUser, null, info, forum);
			
		c = CourseTestUtils.createCourseIfNotExist(mvc, c, httpSession);
				
		Comment cm = new Comment("This is the message", System.currentTimeMillis(), loggedUser);
		Entry entry = new Entry("Test Entry",System.currentTimeMillis(),loggedUser);
		entry.getComments().add(cm);	
		
		c = ForumTestUtils.newEntry(mvc, c, entry, httpSession);
		
		long entryId = c.getCourseDetails().getForum().getEntries().get(0).getId();
		long cdId = c.getCourseDetails().getId();
		
		Comment comment = new Comment();
		comment.setMessage("New Comment");
		
		Gson gson = new Gson();
		String request_OK = gson.toJson(comment);
		
		//test new message
		//test ok 
		try {
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", String.valueOf(entryId))+cdId)
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                .content(request_OK)
					                ).andReturn();
			
			String content = result.getResponse().getContentAsString();
			Entry e = ForumTestUtils.json2Entry(content);
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.CREATED.value();

			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			Assertions.assertEquals(loggedUser,e.getComments().get(0).getUser(),"failure - expected user x");

		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test OK");
		}
		//test UNAUTHORIZED 
		try {
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", String.valueOf(entryId))+cdId)
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .content(request_OK)
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
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", String.valueOf(entryId))+"not_a_id")
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                ).andReturn();
			
			
			
			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.BAD_REQUEST.value();

			assertEquals(expected, status, "failure - expected HTTP status "+expected);

		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test BAD_REQUEST");
		}
	}
	
	@Rollback
	@Test
	public void replyCommentTest() throws Exception {
		
		Course c = CourseTestUtils.newCourseWithCd(courseTitle, loggedUser, null, info, forum);	
		
		c = CourseTestUtils.createCourseIfNotExist(mvc, c, httpSession);
						
		Comment cm = new Comment("This is the message", System.currentTimeMillis(), loggedUser);
		Entry entry = new Entry("Test Entry",System.currentTimeMillis(),loggedUser);
		entry.getComments().add(cm);		
		c = ForumTestUtils.newEntry(mvc, c, entry, httpSession);
		
		long entryId = c.getCourseDetails().getForum().getEntries().get(0).getId();
		long cdId = c.getCourseDetails().getId();
		
		Comment parent = c.getCourseDetails().getForum().getEntries().get(0).getComments().get(0);
		Comment comment = new Comment();
		comment.setMessage("New Comment");
		comment.setCommentParent(parent);
		
		Gson gson = new Gson();
		String request_OK = gson.toJson(comment);
		
		//test new message
		//test ok 
		try {
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", String.valueOf(entryId))+cdId)
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                .content(request_OK)
					                ).andReturn();
			
			String content = result.getResponse().getContentAsString();
			Entry e = ForumTestUtils.json2Entry(content);

			int status = result.getResponse().getStatus();
			
			int expected = HttpStatus.CREATED.value();

			assertEquals(expected, status, "failure - expected HTTP status "+expected);
			Assertions.assertEquals( loggedUser, e.getComments().get(0).getReplies().get(0).getUser(), "failure - expected user x");
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //test OK");
		}
		//test UNAUTHORIZED 
		try {
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", String.valueOf(entryId))+cdId)
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .content(request_OK)
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
			
			MvcResult result =  mvc.perform(post(newComment_uri.replace("{entryId}", "not_anID")+"not_a_id")
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
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
