package edu.school21.restful.repository;

import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.HashSet;

public interface TeacherRepository extends PagingAndSortingRepository<User, Long> {
	Page<User> findByRoleAndListTeacherCourses_Id(Role role, long id, Pageable p);
}
