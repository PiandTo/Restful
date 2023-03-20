package edu.school21.restful.exceptions.user;

public class UserNotSavedException extends RuntimeException{

	public UserNotSavedException(String message) {
		super(message);
	}
}