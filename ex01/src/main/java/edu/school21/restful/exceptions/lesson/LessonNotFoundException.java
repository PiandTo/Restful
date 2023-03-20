package edu.school21.restful.exceptions.lesson;

public class LessonNotFoundException extends RuntimeException{
    public LessonNotFoundException(String e) {
        super(e);
    }
}
