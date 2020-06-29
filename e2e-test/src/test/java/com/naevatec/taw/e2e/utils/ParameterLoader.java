package com.naevatec.taw.e2e.utils;

import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.slf4j.LoggerFactory.getLogger;

public class ParameterLoader {

	protected static final Logger log = getLogger(lookup().lookupClass());

	public static Stream<Arguments> getTestUsers()throws IOException {
        log.debug("[getTestUsers] INI");
        Stream records = Stream.empty();

		Collection<User> users = UserLoader.getAllUsers();
		
		for(User user : users) {
			log.debug("getTestUsers--{}",user.getUserCsv());
			records = Stream.concat(records, Stream.of(arguments(user.getUserCsv().split(","))));
			//records.add(user.getUserCsv().split(","));
		}
        log.debug("[getTestUsers] END");
		return records;
		}
	
	public static Stream<Arguments> getTestStudents() throws IOException {

        log.debug("[getTestStudents] INI");
		Stream records = Stream.empty();
		
		Collection<User> users = UserLoader.getAllUsers();
		
		for(User user : users) {
			if (isStudent(user) && !isTeacher(user)) {
				log.debug("getTestStudents--{}", user.getUserCsv());
				records = Stream.concat(records, Stream.of(arguments(user.getUserCsv().split(","))));
			}
		}
        log.debug("[getTestStudents] END");
        return records;
	}
	
	public static  Stream<Arguments> getTestTeachers() throws IOException {

        log.debug("[getTestTeachers] INI");
		Stream records = Stream.empty();
		
		Collection<User> users = UserLoader.getAllUsers();
		
		for(User user : users) {
			if (!isStudent(user) && isTeacher(user)) {
				log.debug("getTestTeachers--{}", user.getUserCsv());
				records = Stream.concat(records, Stream.of(arguments(user.getUserCsv().split(","))));
			}
		}
        log.debug("[getTestTeachers] END");
        return records;
	}
	
	public static  Collection<String[]> sessionParameters() throws IOException{
		return UserLoader.getSessionParameters();
	}

	private static boolean isStudent(User user) {

		return user.getRole().trim().equalsIgnoreCase("STUDENT");
	}
	
	private static boolean isTeacher(User user) {

		return user.getRole().trim().equalsIgnoreCase("TEACHER");
	}

}
