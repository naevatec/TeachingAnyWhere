package com.naevatec.taw.backend.user;

import com.naevatec.taw.backend.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByName(String name);
	
	public Collection<User> findByNameIn(Collection<String> names);
	
	public Collection<User> findByCourses(Collection<Course> courses);

}
