package edu.school21.restful.repository;

import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.HashSet;

public interface TeacherRepository extends PagingAndSortingRepository<User, Long> {
	public HashSet<User> findByRoleAndListTeacherCourses_Id(Role role, long id);
}
