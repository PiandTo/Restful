package edu.school21.restful.exceptions.course;

public class CourseNotFoundException extends RuntimeException{
	public CourseNotFoundException(String e) {
		super(e);
	}
}
