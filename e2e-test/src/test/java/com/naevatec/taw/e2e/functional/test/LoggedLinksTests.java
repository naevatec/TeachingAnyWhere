package com.naevatec.taw.e2e.functional.test;

import com.naevatec.taw.e2e.common.BaseLoggedTest;
import com.naevatec.taw.e2e.common.NavigationUtilities;
import com.naevatec.taw.e2e.common.SpiderNavigation;
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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SeleniumExtension.class)
public class LoggedLinksTests extends BaseLoggedTest {
	

	protected static int DEPTH = 3;

	public static Stream<Arguments> data() throws IOException {
		return ParameterLoader.getTestUsers();
	}

	@ParameterizedTest
	@MethodSource("data")
	public void spiderLoggedTest(String user, String password, String role, @DockerBrowser(type = CHROME) RemoteWebDriver rwd)  throws ElementNotFoundException, BadUserException, NotLoggedException, TimeOutExeception {

		driver = rwd;
		driver = loginAndValidate(driver,user,password);

		/*navigate from home*/
		NavigationUtilities.getUrlAndWaitFooter(driver, host);
				
		List <WebElement> pageLinks = SpiderNavigation.getPageLinks(driver);
		
		Map <String,String> explored = new HashMap<String,String>();
		
		//Navigate the links... 
		//Problem: once one is pressed the rest will be unusable as the page reloads... 

		explored = SpiderNavigation.exploreLinks(driver, pageLinks, explored, DEPTH);
		
		List<String> failed_links = new ArrayList<String>();
		System.out.println(user+" tested "+explored.size()+" urls");
		explored.forEach((link,result) -> {
				log.debug("\t"+link+" => "+result);
				if (result.equals("KO")) {
					failed_links.add(link);				
				}			
		});

		String msg = "";
		for (String failed: failed_links) {
			msg = failed +"\n";	
		}
		assertTrue(failed_links.isEmpty(), msg);
	}
	
}
