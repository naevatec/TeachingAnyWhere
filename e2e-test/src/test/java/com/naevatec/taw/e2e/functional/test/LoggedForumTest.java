package com.naevatec.taw.e2e.functional.test;

import com.naevatec.taw.e2e.common.BaseLoggedTest;
import com.naevatec.taw.e2e.common.CourseNavigationUtilities;
import com.naevatec.taw.e2e.common.ForumNavigationUtilities;
import com.naevatec.taw.e2e.common.NavigationUtilities;
import com.naevatec.taw.e2e.common.exception.BadUserException;
import com.naevatec.taw.e2e.common.exception.ElementNotFoundException;
import com.naevatec.taw.e2e.common.exception.NotLoggedException;
import com.naevatec.taw.e2e.common.exception.TimeOutExeception;
import com.naevatec.taw.e2e.utils.Click;
import com.naevatec.taw.e2e.utils.DOMMannager;
import com.naevatec.taw.e2e.utils.ParameterLoader;
import com.naevatec.taw.e2e.utils.Wait;
import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import static com.naevatec.taw.e2e.common.Constants.*;
import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SeleniumExtension.class)
public class LoggedForumTest extends BaseLoggedTest {
	protected static WebDriver driver;
	
	protected String courseName="Pseudoscientific course for treating the evil eye";

	public static Stream<Arguments> data() throws IOException {
		return ParameterLoader.getTestUsers();
	}

    @ParameterizedTest
	  @MethodSource("data")
    public void forumLoadEntriesTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)  throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;
		
	 	String courseName = properties.getProperty("forum.test.course");

		driver = loginAndValidate(driver,  user, password);

    	try {
    		//navigate to courses.
    		if (!NavigationUtilities.amIHere(driver, COURSES_URL.replace("__HOST__", host))) {	
    			driver = NavigationUtilities.toCoursesHome(driver);	
    		}
    		List <String> courses = CourseNavigationUtilities.getCoursesList(driver, host);
    		
    		assertTrue(courses.size()>0, "No courses in the list");
    		
    		//find course with forum activated 
    		boolean activated_forum_on_some_test=false;
    		boolean has_comments=false;
    		for (String course_name : courses) {
    			//go to each of the courses 
    			WebElement course = CourseNavigationUtilities.getCourseElement(driver, course_name);
    			course.findElement(COURSELIST_COURSETITLE).click();
    	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
    	    	
    	    	//go to forum tab to check if enabled:
    	    	//load forum
    	    	driver = CourseNavigationUtilities.go2Tab(driver, FORUM_ICON);
    	    	if(ForumNavigationUtilities.isForumEnabled(CourseNavigationUtilities.getTabContent(driver, FORUM_ICON))) {
    	    		activated_forum_on_some_test = true;
    	        	//Load list of entries
    	    		List <String> entries_list = ForumNavigationUtilities.getFullEntryList(driver);
    	    		if (entries_list.size()>0) {
    	    			
	    	        	//Go into first entry
    	    			for (String entry_name : entries_list) {
    	    				WebElement entry = ForumNavigationUtilities.getEntry(driver, entry_name);
    	    				driver = Click.element(driver, entry.findElement(FORUMENTRYLIST_ENTRYTITLE));
    	    				//Load comments
    	        	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
    	        	    	List<WebElement>comments = ForumNavigationUtilities.getComments(driver);
    	    				if(comments.size()>0) {
    	    					has_comments = true;
    	    					List <WebElement> user_comments = ForumNavigationUtilities.getUserComments(driver, userName);  	    					
    	    				}//else go to next entry
    	    				driver = Click.element(driver, DOMMannager.getParent(driver, driver.findElement(BACK_TO_ENTRIESLIST_ICON)));
    	    			}
    	    		}//(else) if no entries go to next course
    	    		
    	    	}//(else) if forum no active go to next course
    	    	
    	    	driver = Click.element(driver, BACK_TO_DASHBOARD);
    		}
    		assertEquals((activated_forum_on_some_test&&has_comments), true, "There isn't any forum that can be used to test this [Or not activated or no entry lists or not comments]");
    		
    	}catch(ElementNotFoundException enfe) {
    		fail("Failed to navigate to courses forum:: "+ enfe.getClass()+ ": "+enfe.getLocalizedMessage());
    	}
    	
    	
    	
    }

	@ParameterizedTest
	@MethodSource("data")
    public void forumNewEntryTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)  throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;

		driver = loginAndValidate(driver,  user, password);

    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());

    	int mYear = calendar.get(Calendar.YEAR);
    	int mMonth = calendar.get(Calendar.MONTH);
    	int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    	int mHour = calendar.get(Calendar.HOUR_OF_DAY);
    	int mMinute = calendar.get(Calendar.MINUTE);
    	int mSecond = calendar.get(Calendar.SECOND);
    	
    	String newEntryTitle = "New Entry Test "+ mDay+mMonth+mYear+mHour+mMinute+mSecond;
    	String newEntryContent = "This is the content written on the "+mDay+" of "+months[mMonth-1]+", " +mHour+":"+mMinute+","+mSecond ;
    	
    	try {
    		//navigate to courses.
    		if (!NavigationUtilities.amIHere(driver, COURSES_URL.replace("__HOST__", host))) {	
    			driver = NavigationUtilities.toCoursesHome(driver);	
    		}
    		WebElement course = CourseNavigationUtilities.getCourseElement(driver, courseName);
    		course.findElement(COURSELIST_COURSETITLE).click();
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
	    	driver = CourseNavigationUtilities.go2Tab(driver, FORUM_ICON);
	    	
	    	assertEquals(ForumNavigationUtilities.isForumEnabled(CourseNavigationUtilities.getTabContent(driver,FORUM_ICON)), true, "Forum not activated");
	    	
	    	driver = ForumNavigationUtilities.newEntry(driver, newEntryTitle, newEntryContent);
    		
	    	//Check entry... 
	    	WebElement newEntry = ForumNavigationUtilities.getEntry(driver, newEntryTitle);

	    	assertEquals(newEntry.findElement(FORUMENTRYLIST_ENTRY_USER).getText(),userName,"Incorrect user");
	    	
	    	driver = Click.element(driver, newEntry.findElement(FORUMENTRYLIST_ENTRYTITLE));
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
	    	WebElement entryTitleRow = driver.findElement(FORUMCOMMENTLIST_ENTRY_TITLE);
	    	assertEquals( entryTitleRow.getText().split("\n")[0], newEntryTitle,"Incorrect Entry Title");
	    	assertEquals( entryTitleRow.findElement(FORUMCOMMENTLIST_ENTRY_USER).getText(), userName, "Incorrect User for Entry");
	    	
	    	//first comment should be the inserted while creating the entry
	    	List<WebElement>comments = ForumNavigationUtilities.getComments(driver);
	    	assertFalse(comments.size()< 1, "No comments on the entry");
	    	
	    	WebElement newComment = comments.get(0);
	    	assertEquals(newComment.findElement(FORUMCOMMENTLIST_COMMENT_CONTENT).getText(),newEntryContent,"Bad content of comment");
	    	assertEquals(newComment.findElement(FORUMCOMMENTLIST_COMMENT_USER).getText(),userName,"Bad user in comment");
	    	
    	}catch(ElementNotFoundException enfe) {
    		fail("Failed to navigate to course forum:: "+ enfe.getClass()+ ": "+enfe.getLocalizedMessage());
    	}
    	
    }

	@ParameterizedTest
	@MethodSource("data")
    public void forumNewCommentTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)  throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;

		driver = loginAndValidate(driver,  user, password);

    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());

    	int mYear = calendar.get(Calendar.YEAR);
    	int mMonth = calendar.get(Calendar.MONTH);
    	int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    	int mHour = calendar.get(Calendar.HOUR_OF_DAY);
    	int mMinute = calendar.get(Calendar.MINUTE);
    	int mSecond = calendar.get(Calendar.SECOND);
    	
    	String newEntryTitle = "";
    	try {
	    	//check if course have any entry for comment
	    	if (!NavigationUtilities.amIHere(driver, COURSES_URL.replace("__HOST__", host))) {	
				driver = NavigationUtilities.toCoursesHome(driver);	
			}
    	
			WebElement course = CourseNavigationUtilities.getCourseElement(driver, courseName);
			course.findElement(COURSELIST_COURSETITLE).click();
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
	    	driver = CourseNavigationUtilities.go2Tab(driver, FORUM_ICON);
	    	assertEquals(ForumNavigationUtilities.isForumEnabled(CourseNavigationUtilities.getTabContent(driver,FORUM_ICON)), true, "Forum not activated");
	    	
	    	List <String> entries_list = ForumNavigationUtilities.getFullEntryList(driver);
	    	WebElement entry; 
			if (entries_list.size()<=0) {//if not new entry
				newEntryTitle = "New Comment Test "+ mDay+mMonth+mYear+mHour+mMinute+mSecond;
		    	String newEntryContent = "This is the content written on the "+mDay+" of "+months[mMonth-1]+", " +mHour+":"+mMinute+","+mSecond ;
				driver = ForumNavigationUtilities.newEntry(driver, newEntryTitle, newEntryContent);
				entry = ForumNavigationUtilities.getEntry(driver, newEntryTitle);
			}
			else {
				entry = ForumNavigationUtilities.getEntry(driver, entries_list.get(0));
			}
			//go to entry 
			driver = Click.element(driver, entry.findElement(FORUMENTRYLIST_ENTRYTITLE));
			WebElement commentList = Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
			
			//new comment
			WebElement newCommentIcon = commentList.findElement(FORUMCOMMENTLIST_NEWCOMMENT_ICON);
	    	driver = Click.element(driver, newCommentIcon);
	    	Wait.aLittle(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUM_NEWCOMMENT_MODAL));
	    	String newCommentContent = "COMMENT TEST"+ mDay+mMonth+mYear+mHour+mMinute+mSecond+". This is the comment written on the "+mDay+" of "+months[mMonth-1]+", " +mHour+":"+mMinute+","+mSecond ;
	
	    	WebElement comment_field = driver.findElement(FORUM_NEWCOMMENT_MODAL_TEXTFIELD);
	    	comment_field.sendKeys(newCommentContent);
	    	
	    	driver = Click.element(driver, FORUM_NEWCOMMENT_MODAL_POSTBUTTON);
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
	    	List<WebElement>comments = ForumNavigationUtilities.getComments(driver);
	    	
	    	//asserts
	    	assertEquals(comments.size()>1, true, "Comment list empty or only original comment");
	    	boolean commentFound = false;
	    	for (WebElement comment : comments) {
	    		//check if it is new comment
	    		if (comment.findElement(FORUMCOMMENTLIST_COMMENT_CONTENT).getText().equals(newCommentContent)) {
	    			commentFound = true;
	    			assertEquals(comment.findElement(FORUMCOMMENTLIST_COMMENT_USER).getText(),userName,"Bad user in comment");
	    		}
	    	}
	    	assertEquals(commentFound, true, "Comment not found");
	    	
    	}catch(ElementNotFoundException enfe) {
    		fail("Failed to navigate to course forum:: "+ enfe.getClass()+ ": "+enfe.getLocalizedMessage());
    	}

    }

	@ParameterizedTest
	@MethodSource("data")
    public void forumNewReply2CommentTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)  throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {
		driver = rwd;

		driver = loginAndValidate(driver,  user, password);

    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());

    	int mYear = calendar.get(Calendar.YEAR);
    	int mMonth = calendar.get(Calendar.MONTH);
    	int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    	int mHour = calendar.get(Calendar.HOUR_OF_DAY);
    	int mMinute = calendar.get(Calendar.MINUTE);
    	int mSecond = calendar.get(Calendar.SECOND);
    	
    	String newEntryTitle = "";
    	try {
	    	//check if course have any entry for comment
	    	if (!NavigationUtilities.amIHere(driver, COURSES_URL.replace("__HOST__", host))) {	
				driver = NavigationUtilities.toCoursesHome(driver);	
			}
    	
			WebElement course = CourseNavigationUtilities.getCourseElement(driver, courseName);
			course.findElement(COURSELIST_COURSETITLE).click();
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
	    	driver = CourseNavigationUtilities.go2Tab(driver, FORUM_ICON);
	    	assertEquals(ForumNavigationUtilities.isForumEnabled(CourseNavigationUtilities.getTabContent(driver,FORUM_ICON)),true,"Forum not activated");
	    	
	    	List <String> entries_list = ForumNavigationUtilities.getFullEntryList(driver);
	    	WebElement entry; 
			if (entries_list.size()<=0) {//if not new entry
				newEntryTitle = "New Comment Test "+ mDay+mMonth+mYear+mHour+mMinute+mSecond;
		    	String newEntryContent = "This is the content written on the "+mDay+" of "+months[mMonth-1]+", " +mHour+":"+mMinute+","+mSecond ;
				driver = ForumNavigationUtilities.newEntry(driver, newEntryTitle, newEntryContent);
				entry = ForumNavigationUtilities.getEntry(driver, newEntryTitle);
			}
			else {
				entry = ForumNavigationUtilities.getEntry(driver, entries_list.get(0));
			}
			//go to entry 
			driver = Click.element(driver, entry.findElement(FORUMENTRYLIST_ENTRYTITLE));
			WebElement commentList = Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
			List<WebElement>comments = ForumNavigationUtilities.getComments(driver);
			
			//go to first comment
			WebElement comment = comments.get(0);
			driver = Click.element(driver, comment.findElement(FORUMCOMMENTLIST_COMMENT_REPLY_ICON));
	    	
			String newReplyContent = "This is the reply written on the "+mDay+" of "+months[mMonth-1]+", " +mHour+":"+mMinute+","+mSecond ;

			//reply
			Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST_MODAL_NEWREPLY));
			
			WebElement textField = driver.findElement(FORUMCOMMENTLIST_MODAL_NEWREPLY_TEXTFIELD);
			textField.sendKeys(newReplyContent);
			driver = Click.element(driver, FORUM_NEWCOMMENT_MODAL_POSTBUTTON);
			commentList = Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(FORUMCOMMENTLIST));
			comments = ForumNavigationUtilities.getComments(driver);

			//getComment replies 
			List <WebElement> replies = ForumNavigationUtilities.getReplies(driver,comments.get(0)); 
			WebElement newReply = null;
			for(WebElement reply: replies) {
				if(reply.findElement(FORUMCOMMENTLIST_COMMENT_CONTENT).getText().equals(newReplyContent))
					newReply= reply;				
			}
			//assert reply
			assertNotNull(newReply,"Reply not found");
	    	assertEquals(newReply.findElement(FORUMCOMMENTLIST_COMMENT_USER).getText(),userName,"Bad user in comment");
	    	
			//nested reply
	    	
			//assert nested reply
			
    	}catch(ElementNotFoundException enfe) {
    		fail("Failed to navigate to course forum:: "+ enfe.getClass()+ ": "+enfe.getLocalizedMessage());
    	}
    }
    
    protected  String months[] = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

}
