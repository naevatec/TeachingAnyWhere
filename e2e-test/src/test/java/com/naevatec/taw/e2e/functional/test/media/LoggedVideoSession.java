package com.naevatec.taw.e2e.functional.test.media;

import com.naevatec.taw.e2e.common.CourseNavigationUtilities;
import com.naevatec.taw.e2e.common.NavigationUtilities;
import com.naevatec.taw.e2e.common.SessionNavigationUtilities;
import com.naevatec.taw.e2e.common.UserUtilities;
import com.naevatec.taw.e2e.common.exception.BadUserException;
import com.naevatec.taw.e2e.common.exception.ElementNotFoundException;
import com.naevatec.taw.e2e.common.exception.NotLoggedException;
import com.naevatec.taw.e2e.common.exception.TimeOutExeception;
import com.naevatec.taw.e2e.utils.*;
import io.github.bonigarcia.seljup.DriverCapabilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

import static com.naevatec.taw.e2e.common.Constants.*;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.logging.Level.ALL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.remote.CapabilityType.LOGGING_PREFS;
import static org.openqa.selenium.remote.DesiredCapabilities.chrome;
import static org.slf4j.LoggerFactory.getLogger;

@Disabled
public class LoggedVideoSession{

	//1 teacher
	protected WebDriver teacherDriver;
	
	//at least 1 student;
	protected List<WebDriver> studentDriver;
	
	public String teacher_data;
		
	public String users_data;
	
	public String courseName;

	protected String teacherName;
	protected String teacher;
	protected String teacher_pass;
	
	protected List<String>students;
	protected List<String>studentPass;
	protected List<String>studentNames;
	

	protected String host=LOCALHOST;
	
	protected Properties properties; 
	
	final  Logger log = getLogger(lookup().lookupClass());

	private String sessionName = "Today's Session";
	private String sessionDescription= "Wow today session will be amazing";
	private String sessionDate;
	private String sessionHour;
	
	@DriverCapabilities
	 DesiredCapabilities capabilities = chrome();
	 {
	        LoggingPreferences logPrefs = new LoggingPreferences();
	        logPrefs.enable(BROWSER, ALL);
	        capabilities.setCapability(LOGGING_PREFS, logPrefs);
	    }

	@BeforeEach
	public void setUp() throws BadUserException, ElementNotFoundException, NotLoggedException, TimeOutExeception {

		 	log.info("[INI setUP]");

	    	host = SetUp.getHost();

	        log.info("Test over url: "+host);

	        //teacher setUp
	        
	        teacher = teacher_data.split(":")[0];
	        teacher_pass= teacher_data.split(":")[1];
	        teacherDriver = UserLoader.allocateNewBrowser(teacher_data.split(":")[2]);
	        
	    	//check if logged with correct user
	        teacherDriver = SetUp.loginUser(teacherDriver, host, teacher , teacher_pass);
	        teacherDriver = UserUtilities.checkLogin(teacherDriver, teacher);
	        teacherName = UserUtilities.getUserName(teacherDriver, true, host);
	    	
	        //students setUp
	        students = new ArrayList<String>();
	    	studentPass = new ArrayList<String>();
	    	studentNames = new ArrayList<String>();
	    	studentDriver = new ArrayList<WebDriver>();
	    	
	        String[] students_data = users_data.split(";");
	        
	        for(int i=0; i< students_data.length; i++) {
	        	String userid = students_data[i].split(":")[0];
	        	students.add(userid);
	        	String userpass = students_data[i].split(":")[1];
	        	studentPass.add(userpass);
	        	
	        	WebDriver studentD = UserLoader.allocateNewBrowser(students_data[i].split(":")[2]);
	        	
	        	studentD = SetUp.loginUser(studentD, host, userid , userpass);
	        	studentD = UserUtilities.checkLogin(studentD, userid);
	        	studentNames.add(UserUtilities.getUserName(studentD, true, host));	        	
	        	studentDriver.add(studentD);
	        }
	        
	    	/* Dedicated set up to Forum tests*/
	        /*log.info("INI dedicated setUP");
	    	
	    	
	    	//LOAD PROPERTIES:
	    	properties = new Properties();
			try {
				// load a properties file for reading
				properties.load(new FileInputStream("src/test/resources/inputs/test.properties"));
				courseName = properties.getProperty("forum.test.course");
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}  
			
	    	log.info("End dedicated setUP");*/
	    	/*END dedicated*/
	    	log.info("[End setUP]");
	    }
	
	 @AfterEach
	 public void teardown() throws IOException {
		//TODO delete tested test if it is last test.
        SetUp.tearDown(teacherDriver);
        teacherDriver.close();
        for (WebDriver driver: studentDriver) {
        	SetUp.tearDown(driver);
        	driver.close();
        }           
    }
	
    public static Collection<String[]> data() throws IOException {
        return ParameterLoader.sessionParameters();
    }

	@ParameterizedTest
	@MethodSource("data")
    public void sessionTest() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());

    	int mYear = calendar.get(Calendar.YEAR);
    	int mMonth = calendar.get(Calendar.MONTH);
    	int mDay = calendar.get(Calendar.DAY_OF_MONTH);	
    	int mHour = calendar.get(Calendar.HOUR);
    	if(mHour == 0) mHour = 12;
    	int mAMPM = calendar.get(Calendar.AM_PM);
    	int mMinute = calendar.get(Calendar.MINUTE);
    	int mSecond = calendar.get(Calendar.SECOND);
    	
    	sessionDate = ""+(mDay<10? "0"+mDay : mDay)+ (mMonth<10? "0"+mMonth : mMonth)+mYear;
    	sessionHour = ""+(mHour<10? "0"+mHour : mHour)+(mMinute<10? "0"+mMinute : mMinute)+(mAMPM == Calendar.AM ?"A" :"P" );
    	try {
    		if (!NavigationUtilities.amIHere(teacherDriver, COURSES_URL.replace("__HOST__", host))) {	
    			teacherDriver = NavigationUtilities.toCoursesHome(teacherDriver);	
    		}
    		List <String> courses = CourseNavigationUtilities.getCoursesList(teacherDriver, host);
    		
    		assertTrue(courses.size()>0, "No courses in the list");
    		//Teacher go to Course and create a new session to join
    	
			WebElement course = CourseNavigationUtilities.getCourseElement(teacherDriver, courseName);
			
			course.findElement(COURSELIST_COURSETITLE).click();
	    	Wait.notTooMuch(teacherDriver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
	    	teacherDriver = CourseNavigationUtilities.go2Tab(teacherDriver, SESSION_ICON);
	    	
	    	teacherDriver = Click.element(teacherDriver, SESSIONLIST_NEWSESSION_ICON);
	    	
			//wait for modal
	    	WebElement modal = Wait.notTooMuch(teacherDriver).until(ExpectedConditions.visibilityOfElementLocated(SESSIONLIST_NEWSESSION_MODAL));
	    	modal.findElement(SESSIONLIST_NEWSESSION_MODAL_TITLE).sendKeys(sessionName);
	    	modal.findElement(SESSIONLIST_NEWSESSION_MODAL_CONTENT).sendKeys(sessionDescription);
	    	modal.findElement(SESSIONLIST_NEWSESSION_MODAL_DATE).sendKeys(sessionDate);
	    	modal.findElement(SESSIONLIST_NEWSESSION_MODAL_TIME).sendKeys(sessionHour);
	    	teacherDriver = Click.element(teacherDriver, modal.findElement(SESSIONLIST_NEWSESSION_MODAL_POSTBUTTON));
	    	//teacherDriver = Click.element(teacherDriver, SESSIONLIST_NEWSESSION_MODAL_DATE);
	    	//check if session has been created
	    	List <String> session_titles = SessionNavigationUtilities.getFullSessionList(teacherDriver);
	    	assertTrue(session_titles.contains(sessionName), "Session has not been created");
	    	
		} catch (ElementNotFoundException e) {
			fail("Error while creating new SESSION");
		}
    
    	//Teacher Join Session
    	try {
    		
	    	List <String> session_titles = SessionNavigationUtilities.getFullSessionList(teacherDriver);
	    	assertTrue(session_titles.contains(sessionName), "Session has not been created");
			
	    	//Teacher to: JOIN SESSION.
			WebElement session = SessionNavigationUtilities.getSession(teacherDriver,sessionName );
			teacherDriver = Click.element(teacherDriver, session.findElement(SESSIONLIST_SESSION_ACCESS));
			
			//assertTrue(condition);
	    	//Check why this is failing... maybe urls are not correct? configuration on the project?
	    	
		} catch (ElementNotFoundException e) {
			fail("Error while creating new SESSION");
		}
    	
    	//Students Join Sessions
    	try {
    		for(WebDriver student_d: studentDriver) {
    			
    			if (!NavigationUtilities.amIHere(student_d, COURSES_URL.replace("__HOST__", host))) {	
    				student_d = NavigationUtilities.toCoursesHome(student_d);	
        		}
        		List <String> courses = CourseNavigationUtilities.getCoursesList(student_d, host);
        		
        		assertTrue(courses.size()>0, "No courses in the list");
        		//Teacher go to Course and create a new session to join
        	
    			WebElement course = CourseNavigationUtilities.getCourseElement(student_d, courseName);
    			
    			course.findElement(COURSELIST_COURSETITLE).click();
    	    	Wait.notTooMuch(student_d).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
    	    	student_d = CourseNavigationUtilities.go2Tab(student_d, SESSION_ICON);
    	    	
		    	List <String> session_titles = SessionNavigationUtilities.getFullSessionList(student_d);
		    	assertTrue(session_titles.contains(sessionName), "Session has not been created");
				
		    	//Student to: JOIN SESSION.
				WebElement session = SessionNavigationUtilities.getSession(student_d,sessionName );
				student_d = Click.element(student_d, session.findElement(SESSIONLIST_SESSION_ACCESS));
				
				//assertTrue(condition);
		    	//Check why this is failing... maybe urls are not correct? configuration on the project?
    		}
	    	
		} catch (ElementNotFoundException e) {
			fail("Error while creating new SESSION");
		}
    	
    	//Students Leave Sessions
    	try {
    		for(WebDriver student_d: studentDriver) {
		    			
		    	//student to: LEAVE SESSION.
    			student_d = Click.element(student_d, SESSION_LEFT_MENU_BUTTON);
				
    			student_d = Click.element(student_d, SESSION_EXIT_ICON);
				
				//Wait for something
				Wait.notTooMuch(student_d).until(ExpectedConditions.visibilityOfElementLocated(COURSE_TABS));
				//assertTrue(condition);
		    	//Check why this is failing... maybe urls are not correct? configuration on the project?
    		}
	    	
		} catch (ElementNotFoundException e) {
			fail("Error while leaving SESSION");
		}
    	//Teacher Leave Session
    	try {
			
		    //student to: LEAVE SESSION.
    		teacherDriver = Click.element(teacherDriver, SESSION_LEFT_MENU_BUTTON);
				
    		teacherDriver = Click.element(teacherDriver, SESSION_EXIT_ICON);
				
			//Wait for something
			Wait.notTooMuch(teacherDriver).until(ExpectedConditions.visibilityOfElementLocated(COURSE_TABS));
			//assertTrue(condition);
	    	//Check why this is failing... maybe urls are not correct? configuration on the project?
	    	
		} catch (ElementNotFoundException e) {
			fail("Error while leaving SESSION");
		}
    	try {
    		//delete session by teacher
			WebElement session = SessionNavigationUtilities.getSession(teacherDriver,sessionName);

			teacherDriver = Click.element(teacherDriver, session.findElement(SESSIONLIST_SESSIONEDIT_ICON));

	    	WebElement modal = Wait.notTooMuch(teacherDriver).until(ExpectedConditions.visibilityOfElementLocated(SESSIONLIST_EDIT_MODAL));
	    	teacherDriver = Click.element(teacherDriver, modal.findElement(SESSIONLIST_EDITMODAL_DELETE_DIV).findElement(By.tagName("label")));
	    	teacherDriver = Click.element(teacherDriver, modal.findElement(SESSIONLIST_EDITMODAL_DELETE_DIV).findElement(By.tagName("a")));
	    	
	    	List <String> session_titles = SessionNavigationUtilities.getFullSessionList(teacherDriver);
	    	assertTrue(!session_titles.contains(sessionName), "Session has not been deleted");
	    	
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
}
