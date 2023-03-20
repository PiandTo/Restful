package edu.school21.restful.exceptions.lesson;

public class LessonNotSavedException extends RuntimeException{

    public LessonNotSavedException(String message) {
        super(message);
    }
}
