package com.naevatec.taw.backend.coursedetails;

import com.naevatec.taw.backend.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailsRepository extends JpaRepository<CourseDetails, Long> {
	
	CourseDetails findByCourse(Course course);

}
