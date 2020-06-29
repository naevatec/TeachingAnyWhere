package com.naevatec.taw.backend.user;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

/**
 * This class is designed to manage the information for the user while he is
 * logged in the service. This object can be used in any other @Component
 * auto-wiring it as usual.
 * 
 * Instances of this class are never sent to the user in any REST endpoint. It
 * can hold sensible information that can not be known in the client. 
 * 
 * NOTE: This class is intended to be extended by developer adding new
 * attributes. Current attributes can not be removed because they are used in
 * authentication procedures.
 */

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserComponent implements Serializable {

	private static final long serialVersionUID = -7070832473325447034L;

	private User user;

	public User getLoggedUser() {
		return user;
	}

	public void setLoggedUser(User user) {
		this.user = user;
	}

	public boolean isLoggedUser() {
		return this.user != null;
	}

}
