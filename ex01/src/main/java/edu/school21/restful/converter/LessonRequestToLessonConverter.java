package edu.school21.restful.converter;

import edu.school21.restful.json.LessonRequest;
import edu.school21.restful.models.Lesson;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Component
public class LessonRequestToLessonConverter implements Converter<Lesson, LessonRequest>{
	@Override
	public Lesson convert(LessonRequest source) {
		Lesson lesson = new Lesson();
		lesson.setEndTime(source.getEndTime());
		lesson.setStartTime(source.getStartTime());
		lesson.setDayOfWeek(source.getDayOfWeek());
		return lesson;
	}
}
