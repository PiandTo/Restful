package edu.school21.restful.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
public class UserGlobalExceptionHandler {

	@ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<UserError> handleTypeMismatch(Exception ex) {
		List<String> error = new ArrayList<>();
		error.add(ex.getLocalizedMessage());
		UserError userError = new UserError(HttpStatus.BAD_REQUEST, "You can't save!", error);
		return ResponseEntity
				.status(userError.getStatus())
				.body(userError);
	}

//    @ExceptionHandler(UserNotSavedException.class)
//    public ResponseEntity<UserError> handleTypeMismatch(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        String error = ex.getLocalizedMessage();
//        UserError userError = new UserError(HttpStatus.BAD_REQUEST, ex.getMessage(), error);
//        return new ResponseEntity<>(userError, new HttpHeaders(), userError.getStatus());
//    }

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserError> handleAll(UserNotFoundException ex) {
		List<String> error = new ArrayList<>();
		error.add(ex.getLocalizedMessage());
		UserError userError = new UserError(HttpStatus.NOT_FOUND, ex.getMessage(), error);
		return ResponseEntity
				.status(userError.getStatus())
				.body(userError);
	}
}
