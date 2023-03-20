package edu.school21.restful.service;

import edu.school21.restful.DTO.UserDTO;
import edu.school21.restful.converter.UserToUserDtoConverter;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.user.UserNotFoundException;
import edu.school21.restful.exceptions.user.UserNotSavedException;
import edu.school21.restful.json.UserRequest;
import edu.school21.restful.json.UserResponse;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import edu.school21.restful.repository.CourseRepository;
import edu.school21.restful.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserToUserDtoConverter userToUserDtoConverter;

	public HashSet<UserDTO> findAllByRoleAndId(long id, Pageable p) {
		Page<User> students = studentRepository.findByRoleAndListStudentCourses_Id(Role.STUDENT, id, p);
		if (students.isEmpty()) {
			throw new UserNotFoundException("No students with course id " + id);
		}
		HashSet<UserDTO> u = new HashSet<>();
		for (User us : students) {
			u.add(userToUserDtoConverter.convert(us));
		}
		return u;
	}

	public UserDTO addStudentToCourse(UserRequest user, long id) {
		Optional<Course> course = courseRepository.findById(id);
		User newStudent = new User();
		if (course.isPresent()) {
			try {
				newStudent.setFirstName(user.getFirstName());
				newStudent.setLastName(user.getLastName());
				newStudent.setRole(Role.STUDENT);
				newStudent.setLogin(user.getLogin());
				newStudent.setPassword(passwordEncoder.encode(user.getPassword()));
				newStudent.addStudent(course.get());
				User userSaved = studentRepository.save(newStudent);
				return userToUserDtoConverter.convert(userSaved);
			} catch (Exception e) {
				throw new UserNotSavedException("No saved Student with name " + user.getFirstName());
			}
		}
		throw new CourseNotFoundException("No course with such id " + id);
	}

	public void deleteStudentFromCourse(long course_id, long student_id) {
		Optional<User> user = studentRepository.findById(student_id);
		Iterable<Course> courses = courseRepository.findAllByStudentsId(student_id);
		if (!user.isPresent()) throw new UserNotFoundException("Student not found with id " + student_id);
		int size = 0;
		for (Course c : courses) size++;
		if (size == 0) throw new CourseNotFoundException("Course not found with id " + course_id);
		for (Course c : courses) {
			user.get().removeStudent(c);
		}
		studentRepository.delete(user.get());
	}
}
