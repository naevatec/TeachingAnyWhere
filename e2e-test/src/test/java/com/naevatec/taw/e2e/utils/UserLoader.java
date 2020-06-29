package com.naevatec.taw.e2e.utils;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static io.github.bonigarcia.wdm.DriverManagerType.FIREFOX;

public class UserLoader {

	public static int USERNAME = 0;
	public static int PASSWORD = 1; 
	public static int ROLES = 2; 
	
	private static String cvsMainFieldsSplitBy = ",";
	private static String cvsRolesSplitBy = "\\|";
	
	private static Map<String,User> users;
	
	private static String usersDefaultFile = "src/test/resources/inputs/default_user_file.csv";
	private static String oneTeacherMultipleStudentsFile = "src/test/resources/inputs/session_test_file.csv";
	
	public static void loadUsers (List<User> userlst, boolean override) {
		if (override == true || users == null ) 
			users = new HashMap<String, User>();
		
		for (User i : userlst) users.put(i.getName(),i);
				
	}
	
	public static void loadUsers(String usersFile, boolean override) throws IOException {
		//read file and create users
		String line = "";
     
		List<User> userlst = new ArrayList<User>();
		
        BufferedReader br = new BufferedReader(new FileReader(usersDefaultFile));

        while ((line = br.readLine()) != null) {
        	userlst.add(parseUser(line));
        }  
        loadUsers(userlst, override);
	}
	
	
	
	public static void loadUsers(String usersFile) throws IOException {
		loadUsers(usersFile, false);
	}
	
	public static void loadUsers() throws IOException {
		loadUsers(usersDefaultFile);
	}
	
	
	
	public static User parseUser(String cvsline) {
		String field[] = cvsline.split(cvsMainFieldsSplitBy);
		return new User(field[USERNAME], 
						field[PASSWORD], 
						field[ROLES]);
	}

	
	public static Collection<String[]> getSessionParameters() throws IOException{
		String line = "";
	     
		List<String[]> paramList = new ArrayList<String[]>();
		
        BufferedReader br = new BufferedReader(new FileReader(oneTeacherMultipleStudentsFile));
        while ((line = br.readLine()) != null) {
        	paramList.add(line.split(cvsMainFieldsSplitBy));
        }  
        return paramList;
	}
	
	public static User retrieveUser(String name) {
		return users.get(name);
	}
	
	public static Collection<User> getAllUsers() throws IOException{
		if (users==null) {
			loadUsers();
		}
		return users.values();
	}
	
	public static WebDriver allocateNewBrowser(String browser) {
		WebDriver driver = null;
		switch (browser){
			case "chrome":
				ChromeDriverManager.getInstance(CHROME).setup();
				//TODO: driver = ChromeFactory.newWebDriver();
				break;
			case "firefox":
				FirefoxDriverManager.getInstance(FIREFOX).setup();
				//TODO: driver = FirefoxFactory.newWebDriver();
		}
		return driver;
	}
}
