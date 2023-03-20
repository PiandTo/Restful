package edu.school21.restful.service;

import edu.school21.restful.DTO.UserDTO;
import edu.school21.restful.converter.UserToUserDtoConverter;
import edu.school21.restful.exceptions.user.UserNotFoundException;
import edu.school21.restful.exceptions.user.UserNotSavedException;
import edu.school21.restful.json.UserRequest;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import edu.school21.restful.repository.CourseRepository;
import edu.school21.restful.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final UserToUserDtoConverter userToUserDtoConverter;
	private final CourseRepository courseRepository;

	public UserDTO findUserById (Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return userToUserDtoConverter.convert(user.get());
		} else {
			throw new UserNotFoundException("No such user with id " + id);
		}
	}

	public Iterable<UserDTO> findAllUsers (Integer pageNo, Integer pageSize, String sortBy){
		int size = 0;
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Iterable<User> users = userRepository.findAll(pageable);
		for (User user : users) {
			size++;
		}
		if (size == 0) { throw new UserNotFoundException("No saved users"); }
		ArrayList<UserDTO> usersDTO = new ArrayList<>();
		for (User user : users) {
			usersDTO.add(userToUserDtoConverter.convert(user));
		}
		return usersDTO;
	}

	public List<UserDTO> getAllUsers() {
		Iterable<User> users = userRepository.findAll();
		ArrayList<UserDTO> usersDTO = new ArrayList<>();
		for (User user : users) {
			usersDTO.add(userToUserDtoConverter.convert(user));
		}
		return usersDTO;
	}

    public UserDTO registration(String firstName, String lastName, String login, String password, Role role) {
		try {
			User newUser = new User();
	//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//        String hash = passwordEncoder.encode(password);


			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setLogin(login);
			newUser.setPassword(password);
			newUser.setRole(role);
			userRepository.save(newUser);
			return userToUserDtoConverter.convert(newUser);
		} catch (Exception ex) {
			throw new UserNotSavedException("User not saved");
		}
    }

	public UserDTO  updateUser(Long id, UserRequest user) throws Exception {
		if (user == null || id == null) {
			throw new UserNotSavedException("It is no provided user");
		}
		try {
			Optional<User> newUser = userRepository.findById(id);
			if (newUser.isPresent()) {
				User user1 = newUser.get();
				user1.setFirstName(user.getFirstName());
				user1.setLastName(user.getLastName());
				user1.setRole(user.getRole());
				user1.setLogin(user.getLogin());
				user1.setPassword(user.getPassword());
				return userToUserDtoConverter.convert(userRepository.save(user1));
			}
		} catch (Exception ex) {
			throw new UserNotSavedException("It is not good type");
		}
		return null;
	}

	public void deleteUser(Long id) {
		if (id == null) {
			throw new UserNotSavedException("It is no provided user");
		}
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			Iterable<Course> courses = courseRepository.findAll();
			for (Course course : courses){
				course.removeUserFromCourse(user.get());
				courseRepository.save(course);
			}
			userRepository.deleteById(user.get().getId());
		} else {
			throw new UserNotFoundException("User not found");
		}
	}

	public User findByLogin(String login) {
		User user = userRepository.findByLogin(login);
		return user;
	}

}
