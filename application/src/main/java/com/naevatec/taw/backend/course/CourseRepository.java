package com.naevatec.taw.backend.course;

import com.naevatec.taw.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
    public Collection<Course> findByAttenders(Collection<User> users);

}
