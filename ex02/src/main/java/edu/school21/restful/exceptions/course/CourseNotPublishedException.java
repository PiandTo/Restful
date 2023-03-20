package edu.school21.restful.exceptions.course;

public class CourseNotPublishedException extends RuntimeException {
    public CourseNotPublishedException(String e) {
        super(e);
    }
}
