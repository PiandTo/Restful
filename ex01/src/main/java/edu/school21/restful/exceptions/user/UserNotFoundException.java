package edu.school21.restful.exceptions.user;

public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUUID = 5071646428281007896L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
