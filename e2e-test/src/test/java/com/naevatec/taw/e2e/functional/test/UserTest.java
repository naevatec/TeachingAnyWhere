package com.naevatec.taw.e2e.functional.test;

import com.naevatec.taw.e2e.common.BaseLoggedTest;
import com.naevatec.taw.e2e.common.UserUtilities;
import com.naevatec.taw.e2e.common.exception.BadUserException;
import com.naevatec.taw.e2e.common.exception.ElementNotFoundException;
import com.naevatec.taw.e2e.common.exception.NotLoggedException;
import com.naevatec.taw.e2e.common.exception.TimeOutExeception;
import com.naevatec.taw.e2e.utils.ParameterLoader;
import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.stream.Stream;

import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SeleniumExtension.class)
public class UserTest extends BaseLoggedTest {



	public static Stream<Arguments> data() throws IOException {
        return ParameterLoader.getTestUsers();
    }
	

	@ParameterizedTest
	@MethodSource("data")
	public void loginTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd) throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;

		try {
			driver = UserUtilities.login(driver, user, password, host);
		
			driver = UserUtilities.checkLogin(driver, user);

			assertTrue(true, "not logged");

		} catch (NotLoggedException | BadUserException e) {
				
			e.printStackTrace();
			fail("Not logged");
			
		} catch (ElementNotFoundException e) {
			
			e.printStackTrace();
			fail(e.getLocalizedMessage());
			
		}  catch (TimeOutExeception e) {
			fail(e.getLocalizedMessage());
		} 
		
		try {
			driver = UserUtilities.logOut(driver,host);
			
			driver = UserUtilities.checkLogOut(driver);
			
		} catch (ElementNotFoundException enfe) {
			fail("Still logged");
			
		} catch (NotLoggedException e) {
			assertTrue(true, "Not logged");
		}
			
		assertTrue(true);
	}
	
	
}
