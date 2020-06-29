package com.naevatec.taw.backend.entry;

import com.naevatec.taw.backend.coursedetails.CourseDetails;
import com.naevatec.taw.backend.coursedetails.CourseDetailsRepository;
import com.naevatec.taw.backend.forum.Forum;
import com.naevatec.taw.backend.forum.ForumRepository;
import com.naevatec.taw.backend.security.AuthorizationService;
import com.naevatec.taw.backend.user.User;
import com.naevatec.taw.backend.user.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api-entries")
public class EntryController {
	
	@Autowired
	private ForumRepository forumRepository;
	
	@Autowired
	private CourseDetailsRepository courseDetailsRepository;
	
	@Autowired
	private UserComponent user;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@RequestMapping(value = "/forum/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> newEntry(@RequestBody Entry entry, @PathVariable(value="id") String courseDetailsId) {
		
		ResponseEntity<Object> authorized = authorizationService.checkBackendLogged();
		if (authorized != null){
			return authorized;
		};
		
		long id_i = -1;
		try {
			id_i = Long.parseLong(courseDetailsId);
		} catch(NumberFormatException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		
		Optional<CourseDetails> ocd = courseDetailsRepository.findById(id_i);
		CourseDetails cd = ocd.get();

		ResponseEntity<Object> userAuthorized = authorizationService.checkAuthorizationUsers(cd, cd.getCourse().getAttenders());
		if (userAuthorized != null) { // If the user is not an attender of the course
			return userAuthorized;
		} else {
		
			Forum forum = cd.getForum();
			
			//Setting the author of the entry
			User userLogged = user.getLoggedUser();
			entry.setUser(userLogged);
			
			//Setting the author and date of its first comment
			entry.getComments().get(0).setUser(userLogged);
			entry.getComments().get(0).setDate(System.currentTimeMillis());
			
			//Setting the date of the entry
			entry.setDate(System.currentTimeMillis());
			
			forum.getEntries().add(entry);
			/*Saving the modified forum: Cascade relationship between forum and entries
			  will add the new entry to EntryRepository*/
			forumRepository.save(forum);
			/*Entire forum is returned in order to have the new entry ID available just
			in case the author wants to add to it a new comment without refreshing the page*/
			return new ResponseEntity<>(forum, HttpStatus.CREATED);
		}
	}

}
