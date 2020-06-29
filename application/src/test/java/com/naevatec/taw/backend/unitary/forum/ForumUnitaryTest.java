package com.naevatec.taw.backend.unitary.forum;

import com.naevatec.taw.backend.AbstractUnitTest;
import com.naevatec.taw.backend.entry.Entry;
import com.naevatec.taw.backend.forum.Forum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForumUnitaryTest extends AbstractUnitTest {

	
	@Test
	public void newForumTest() {
		Forum f = new Forum();
		assertNotNull(f);
		
		Forum f2 = new Forum(true);
		assertNotNull(f2);
		assertTrue(f2.isActivated());
		
		Forum f3 = new Forum(false);
		assertNotNull(f3);
		assertTrue(!f3.isActivated());
	}

	
	@Test
	public void activateAndDeactivateTest() {
		Forum f = new Forum();
		f.setActivated(true);
		assertTrue(f.isActivated());
		
		f.setActivated(false);
		assertTrue(!f.isActivated());
		
	}

	@Test
	public void testGetEntries() {
		Forum f = new Forum();
		List<Entry> entries = new ArrayList<Entry>();
		
		f.setEntries(entries);
		
		assertNotNull(f);
		assertTrue(f.getEntries().equals(entries));
	}

}
