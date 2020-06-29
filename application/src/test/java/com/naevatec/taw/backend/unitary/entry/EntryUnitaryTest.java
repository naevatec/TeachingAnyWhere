package com.naevatec.taw.backend.unitary.entry;


import com.naevatec.taw.backend.AbstractUnitTest;
import com.naevatec.taw.backend.comment.Comment;
import com.naevatec.taw.backend.entry.Entry;
import com.naevatec.taw.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntryUnitaryTest extends AbstractUnitTest {

	@BeforeEach
	public void setUp() throws Exception {
	}


	@Test
	public void newForumEntryTest() {
		String[] roles = {"TEACHER"};
		User u =  new User("mock", "Pass1234", "mock", null, roles);
		long date = System.currentTimeMillis();
		
		Entry e2 = new Entry();
		assertNotNull(e2);
		
		Entry e = new Entry("Test Entry",date,u);
		assertNotNull(e);
		assertTrue("Test Entry".equals(e.getTitle()));
		assertTrue(date==e.getDate());
		assertTrue(u.equals(e.getUser()));
	}

	@Test
	public void setAndGetEntryTitleTest() {
		Entry e = new Entry();
		e.setTitle("This title");
		assertNotNull(e);
		assertTrue("This title".equals(e.getTitle()));
	}

	@Test
	public void setAndGetEntryDateTest() {
		Entry e = new Entry();
		long date = System.currentTimeMillis();
		e.setDate(date);
		assertNotNull(e);
		assertTrue(date==e.getDate());

	}

	@Test
	public void setAndGetEntryUserTest() {
		String[] roles = {"TEACHER"};

		User u =  new User("mock", "Pass1234", "mock", null, roles);

		Entry e = new Entry();
		assertNotNull(e);
		e.setUser(u);
		assertTrue(u.equals(e.getUser()));

	}

	@Test
	public void setAndGetEntryCommentsTest() {

		List<Comment> comments = new ArrayList<Comment>();
		
		Entry e = new Entry();
		e.setComments(comments);
		assertNotNull(e);
		assertTrue(comments.equals(e.getComments()));

	}

}
