package com.naevatec.taw.e2e.functional.test.student;

import com.naevatec.taw.e2e.common.BaseLoggedTest;
import com.naevatec.taw.e2e.common.CourseNavigationUtilities;
import com.naevatec.taw.e2e.common.NavigationUtilities;
import com.naevatec.taw.e2e.common.exception.BadUserException;
import com.naevatec.taw.e2e.common.exception.ElementNotFoundException;
import com.naevatec.taw.e2e.common.exception.NotLoggedException;
import com.naevatec.taw.e2e.common.exception.TimeOutExeception;
import com.naevatec.taw.e2e.utils.Click;
import com.naevatec.taw.e2e.utils.ParameterLoader;
import com.naevatec.taw.e2e.utils.Wait;
import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.naevatec.taw.e2e.common.Constants.*;
import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SeleniumExtension.class)
public class CourseStudentTest extends BaseLoggedTest {
	
	public String roles;
	

    public static Stream<Arguments> data() throws IOException {
        return ParameterLoader.getTestStudents();
    }
    
    
    @ParameterizedTest
	@MethodSource("data")
    public void studentCourseMainTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;
		driver = loginAndValidate(driver,  user, password);

    	try {
    		if(!NavigationUtilities.amIHere(driver,COURSES_URL.replace("__HOST__", host)))
        		driver = NavigationUtilities.toCoursesHome(driver);
	    	
    		//go to first course
    		//get course list
    		List<String>course_list = CourseNavigationUtilities.getCoursesList(driver, host);
    		if (course_list.size()<0)  fail("No courses available for test user");
    		
    		WebElement course_button = CourseNavigationUtilities.getCourseElement(driver, course_list.get(0)).findElement(By.className("title"));
    			    	
	    	driver = Click.element(driver, course_button);
	    	
	    	Wait.notTooMuch(driver).until(ExpectedConditions.visibilityOfElementLocated(By.id(TABS_DIV_ID)));
	    	
    	}catch(Exception e) {
			fail("Failed to load Courses Tabs"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	//Check tabs
    	//Home tab 
    	try {
    		driver = CourseNavigationUtilities.go2Tab(driver, HOME_ICON);
    		
    	} catch(Exception e) {
    		fail("Failed to load home tab"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	
    	try {
    		driver = CourseNavigationUtilities.go2Tab(driver, SESSION_ICON);
    	} catch(Exception e) {
    		fail("Failed to load session tab"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	
    	try {
    		driver = CourseNavigationUtilities.go2Tab(driver, FORUM_ICON);
    	} catch(Exception e) {
    		fail("Failed to load forum tab"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	
    	try {
    		driver = CourseNavigationUtilities.go2Tab(driver, FILES_ICON);
    	} catch(Exception e) {
    		fail("Failed to load files tab"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	
    	try {
    		driver = CourseNavigationUtilities.go2Tab(driver, ATTENDERS_ICON);	
    	} catch(Exception e) {
    		fail("Failed to load attenders tab"+ e.getClass()+ ": "+e.getLocalizedMessage());
    	}
    	
    }
    


}
