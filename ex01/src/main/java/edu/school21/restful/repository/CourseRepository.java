package edu.school21.restful.repository;

import edu.school21.restful.models.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.HashSet;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
	HashSet<Course> findAllByStudentsId(long id);
	HashSet<Course> findAllByTeachersId(long id);
}
