package com.naevatec.taw.backend.utils;

import com.google.gson.Gson;
import com.naevatec.taw.backend.course.Course;
import com.naevatec.taw.backend.session.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class SessionTestUtils {
	private static String newSession_uri="/api-sessions/course/";
	
	public static Course newSession(MockMvc mvc, Session s, Course c, HttpSession httpSession) {
		Gson gson = new Gson();
		String request = gson.toJson(s);
		Course course = c;
		//test ok ;
		try {
			MvcResult result =  mvc.perform(post(newSession_uri+c.getId())
					                .contentType(MediaType.APPLICATION_JSON_VALUE)
					                .session((MockHttpSession) httpSession)
					                .content(request)
					                ).andReturn();
			
			String content = result.getResponse().getContentAsString();
			course = CourseTestUtils.json2Course(content);
			
			int status = result.getResponse().getStatus();	
			int expected = HttpStatus.CREATED.value();

			assertEquals( expected, status, "failure - expected HTTP status "+expected);

		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION: //SessionUtils.newSession");		
		}
	
		return course; 
	}

}
