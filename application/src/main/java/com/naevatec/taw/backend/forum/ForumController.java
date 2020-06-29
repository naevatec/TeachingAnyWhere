package com.naevatec.taw.backend.forum;

import com.naevatec.taw.backend.coursedetails.CourseDetails;
import com.naevatec.taw.backend.coursedetails.CourseDetailsRepository;
import com.naevatec.taw.backend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api-forum")
public class ForumController {
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private CourseDetailsRepository courseDetailsRepository;
	
	@RequestMapping(value = "/edit/{courseDetailsId}", method = RequestMethod.PUT)
	public ResponseEntity<Object> modifyForum(@RequestBody boolean activated, @PathVariable(value="courseDetailsId") String courseDetailsId) {
		
		ResponseEntity<Object> authorized = authorizationService.checkBackendLogged();
		if (authorized != null){
			return authorized;
		};
		
		long id_i = -1;
		try{
			id_i = Long.parseLong(courseDetailsId);
		}catch(NumberFormatException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Optional<CourseDetails> o_cd = courseDetailsRepository.findById(id_i);
		CourseDetails cd = o_cd.get();
		
		ResponseEntity<Object> teacherAuthorized = authorizationService.checkAuthorization(cd, cd.getCourse().getTeacher());
		if (teacherAuthorized != null) { // If the user is not the teacher of the course
			return teacherAuthorized;
		} else {
		
			//Modifying the forum
			cd.getForum().setActivated(activated);
			//Saving the modified course
			courseDetailsRepository.save(cd);
			return new ResponseEntity<>(new Boolean(activated), HttpStatus.OK);
		}
	}

}
