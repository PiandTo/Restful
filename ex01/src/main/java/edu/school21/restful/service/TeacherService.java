package edu.school21.restful.service;

import edu.school21.restful.DTO.UserDTO;
import edu.school21.restful.converter.UserToUserDtoConverter;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.user.UserNotFoundException;
import edu.school21.restful.exceptions.user.UserNotSavedException;
import edu.school21.restful.json.UserRequest;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import edu.school21.restful.repository.CourseRepository;
import edu.school21.restful.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
	private final TeacherRepository teacherRepository;
	private final CourseRepository courseRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserToUserDtoConverter userToUserDtoConverter;

	public HashSet<UserDTO> findAllByRoleAndId(long id, Pageable p) {
		Page<User> teachers = teacherRepository.findByRoleAndListTeacherCourses_Id(Role.TEACHER, id, p);
		if (teachers.isEmpty()) {
			throw new UserNotFoundException("No teacher with course id " + id);
		}
		HashSet<UserDTO> u = new HashSet<>();
		for (User us : teachers) {
			u.add(userToUserDtoConverter.convert(us));
		}
		return u;
	}

	public UserDTO addTeacherToCourse(UserRequest user, long id) {
		Optional<Course> course = courseRepository.findById(id);
		User newTeacher = new User();
		if (course.isPresent()) {
			try {
				newTeacher.setFirstName(user.getFirstName());
				newTeacher.setLastName(user.getFirstName());
				newTeacher.setRole(Role.TEACHER);
				newTeacher.setLogin(user.getLogin());
				newTeacher.setPassword(passwordEncoder.encode(user.getPassword()));
				newTeacher.addTeacher(course.get());
				User u = teacherRepository.save(newTeacher);
				return userToUserDtoConverter.convert(u);
			} catch (Exception e) {
				throw new UserNotSavedException("No saved Teacher with name " + user.getFirstName());
			}
		}
		throw new CourseNotFoundException("No course with such id " + id);
	}

	public void deleteTeacherFromCourse(long course_id, long teacher_id) {
		Optional<User> user = teacherRepository.findById(teacher_id);
		Iterable<Course> courses = courseRepository.findAllByTeachersId(teacher_id);
		if (!user.isPresent()) throw new UserNotFoundException("Teacher not found with id " + teacher_id);
		int size = 0;
		for (Course c : courses) {
			System.out.println(c);
			size++;
		}
		if (size == 0) throw new CourseNotFoundException("Course not found with id " + course_id);
		for (Course c : courses) {
			user.get().removeTeacher(c);
		}
		teacherRepository.delete(user.get());
	}
}
