package edu.school21.restful.exceptions.course;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class CourseGlobalExceptionHandler {

	@ExceptionHandler(CourseNotFoundException.class)
	public ResponseEntity<CourseError> handleUserNotFound(CourseNotFoundException e) {
		CourseError courseError = new CourseError();
		courseError.setTimestamp(LocalDateTime.now());
		courseError.setStatus(HttpStatus.NOT_FOUND.value());
		courseError.setMessage("Course is not found");
		List<String> a = new ArrayList<>();
		a.add(e.getMessage());
		courseError.setErrors(a);
		return ResponseEntity
				.status(courseError.getStatus())
				.body(courseError);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<CourseError> handleUserNotFound(InvalidFormatException e) {
		CourseError courseError = new CourseError();
		courseError.setTimestamp(LocalDateTime.now());
		courseError.setStatus(HttpStatus.NOT_FOUND.value());
		courseError.setMessage("Course is not found");
		List<String> a = new ArrayList<>();
		a.add(e.getMessage());
		courseError.setErrors(a);
		return ResponseEntity
				.status(courseError.getStatus())
				.body(courseError);
	}

	@ExceptionHandler({CourseNotSavedException.class, DateTimeParseException.class})
	public ResponseEntity<CourseError> handleCourseNotFound(CourseNotSavedException e, DateTimeParseException ef) {
		CourseError courseError = new CourseError();
		courseError.setTimestamp(LocalDateTime.now());
		courseError.setStatus(HttpStatus.BAD_REQUEST.value());
		courseError.setMessage("Course is not saved correctly");
		List<String> a = new ArrayList<>();
		System.out.println(e.getMessage() + "Hello");
		if (e != null) {
			a.add(e.getMessage());
		} else if (ef != null) {
			a.add(ef.getMessage());
		}
		courseError.setErrors(a);
		return ResponseEntity
				.status(courseError.getStatus())
				.body(courseError);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CourseError> handleException(HttpMessageNotReadableException e) {
		System.out.println("-------------------------");
		CourseError courseError = new CourseError();
		courseError.setTimestamp(LocalDateTime.now());
		courseError.setStatus(HttpStatus.BAD_REQUEST.value());
		courseError.setMessage("Course is not saved correctly");
		List<String> a = new ArrayList<>();
		a.add(e.getMessage());
		courseError.setErrors(a);
		return ResponseEntity
				.status(courseError.getStatus())
				.body(courseError);
	}

}