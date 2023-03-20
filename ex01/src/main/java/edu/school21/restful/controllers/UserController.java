package edu.school21.restful.controllers;

import edu.school21.restful.converter.UserDtoToResponseConverter;
import edu.school21.restful.json.UserRequest;
import edu.school21.restful.json.UserResponse;
import edu.school21.restful.DTO.UserDTO;
import edu.school21.restful.models.User;
import edu.school21.restful.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(
		name = "Пользователи (User)",
		description = "Все методы для работы с пользователями системы (UserController)"
)
public class UserController {
	private final UserService userService;
	private final UserDtoToResponseConverter converter;

	@GetMapping("users")
	@Operation(summary = "Endpoint to get all Users")
	public ResponseEntity<?> getAllUsers(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
										 @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
										 @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
		Iterable<UserDTO> us = userService.findAllUsers(pageNo, pageSize, sortBy);
		log.info("Get method - GetAllUser function");
		HashSet<UserResponse> users = new HashSet<>();
		for (UserDTO u : us) {
			users.add(converter.convert(u));
		}
		return ResponseEntity.ok()
				.body(users);
	}

	@GetMapping("users/{id}")
	@Operation(summary = "Endpoint to find User by ID")
	public ResponseEntity<?> findUserByid (@PathVariable(name = "id") Long id) {
		UserDTO findUser = userService.findUserById(id);
		log.info("Get method - findUserByid function");
		return ResponseEntity.status(HttpStatus.OK)
				.body(converter.convert(findUser));
	}

	@PostMapping("users")
	@Operation(summary = "Зарегистрировать нового пользователя (addNewUser)")
	public ResponseEntity<UserResponse> addNewUser(@RequestBody @Valid UserRequest request) {
		UserDTO user = userService.registration(
				request.getFirstName(),
				request.getLastName(),
				request.getLogin(),
				request.getPassword(),
				request.getRole()
		);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(converter.convert(user));
	}

	@PutMapping("users/{user_id}")
	@Operation(summary = "Endpoint to update User by ID")
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest user, @PathVariable("user_id") Long id) throws Exception {
		UserDTO userDTO = userService.updateUser(id, user);
		return ResponseEntity.ok()
				.body(converter.convert(userDTO));
	}

	@DeleteMapping("users/{userId}")
	@Operation(summary = "Удалить пользователя по id (deleteUser)")
    public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}
}
