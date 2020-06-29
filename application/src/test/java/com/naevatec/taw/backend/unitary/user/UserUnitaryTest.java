/**
 * 
 */
package com.naevatec.taw.backend.unitary.user;

import com.naevatec.taw.backend.AbstractUnitTest;
import com.naevatec.taw.backend.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * @author gtunon
 *
 */
/*@Transactional After each test the BBDD is rolled back*/
// @Transactional not necessary here
public class UserUnitaryTest extends AbstractUnitTest {

	/*Test user data*/
	String name = "TestUser";
	String password = "blablaba";
	String nickName = "testi";
	String picture = "picture/test.jpg";
	String[] roles = {"STUDENT"};
	
	
	/**
	 * Test method for {@link User#User(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}
	 * and {@link User#User()}.
	 */
	@Test
	public void newUserTest() {
		
		//Empty user
		User emptyUser = new User();
		notNull(emptyUser, "User failed to be created");
		
		//User with picture
		User u = new User(name, password, nickName, picture,roles);
		notNull(u, "User failed to be created");
		isTrue(name.equals(u.getName()), "User failed to be created");
		isTrue((new BCryptPasswordEncoder()).matches(password, u.getPasswordHash()), "User failed to be created");
		isTrue(nickName.equals(u.getNickName()), "User failed to be created");
		isTrue(picture.equals(u.getPicture()), "User failed to be created");
		isTrue(roles.length == u.getRoles().size(), "User failed to be created");
		
		//user witout picture
		u = new User(name, password, nickName, null,roles);
		notNull(u, "User failed to be created");
		isTrue(name.equals(u.getName()), "User failed to be created");
		isTrue((new BCryptPasswordEncoder()).matches(password, u.getPasswordHash()), "User failed to be created");
		isTrue(nickName.equals(u.getNickName()), "User failed to be created");
		notNull(u.getPicture(), "User failed to be created");
		isTrue(roles.length == u.getRoles().size(), "User failed to be created");
	}

	/**
	 * Test method for {@link User#getName()}.
	 * and {@link User#setName(java.lang.String)}.
	 */
	@Test
	public void setAndGetUserNameTest() {
		User u = new User();
		u.setName(name);
		isTrue(name.equals(u.getName()), "testSetAndGetUserName FAIL");
	}


	/**
	 * Test method for {@link User#setPasswordHash(java.lang.String)}
	 * and {@link User#getPasswordHash()}.
	 */
	@Test
	public void setAndGetHashPasswordTest() {
		User u = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		u.setPasswordHash(encoder.encode(password));
		isTrue(encoder.matches(password, u.getPasswordHash()), "setAndGetHashPasswordTest FAIL");
	}
	
	/**
	 * Test method for {@link User#getRoles()}
	 * and  {@link User#setRoles(java.util.List)}.
	 */
	@Test
	public void setAndGetUserRolesTest() {
		User u = new User();	
		u.setRoles(Arrays.asList(roles));
		isTrue(roles.length == u.getRoles().size(), "SetAndGetUserRolesTest FAIL");
	}

	/**
	 * Test method for {@link User#getNickName()}
	 * and {@link User#setNickName(java.lang.String)}.
	 */
	@Test
	public void setAndGetUserNickNameTest() {
		User u = new User();
		u.setNickName(nickName);
		isTrue(nickName.equals(u.getNickName()), "SetAndGetUserNickNameTest FAIL");
	}

	/**
	 * Test method for {@link User#getPicture()}
	 * and {@link User#setPicture(java.lang.String)}.
	 */
	@Test
	public void setAndGetUserPictureTest() {
		User u = new User();
		u.setPicture(picture);
		isTrue(picture.equals(u.getPicture()), "SetAndGetUserPictureTest FAIL");
	}

	/**
	 * Test method for {@link User#getRegistrationDate()}
	 * and {@link User#setRegistrationDate(long)}.
	 */
	@Test
	public void setAndGetUserRegistrationDateTest() {
		User u = new User();
		Long date = System.currentTimeMillis();
		u.setRegistrationDate(date);
		isTrue(date==u.getRegistrationDate(), "Bad date");
	}


	/**
	 * Test method for {@link User#equals(java.lang.Object)}.
	 */
	@Test
	public void equalUserTest() {
		User u1 = new User(name, password, nickName, picture,roles);
		User u2 = new User(name, password, nickName, picture,roles);
		isTrue(u1.equals(u2), "EqualUserTest FAIL");
		isTrue(!u1.equals("not An User"), "EqualUserTest FAIL");
		isTrue(u1.equals(u1), "EqualUserTest FAIL");
	}



}
