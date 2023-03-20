package edu.school21.restful.converter;

import edu.school21.restful.DTO.LessonDTO;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.lesson.LessonNotFoundException;
import edu.school21.restful.json.LessonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonDtoToLessonResponseConverter implements Converter<LessonResponse, LessonDTO> {

	@Autowired
	private UserDtoToResponseConverter userDtoToResponseConverter;

	@Override
	public LessonResponse convert(LessonDTO source) {
		if (source == null) {
			throw new LessonNotFoundException("Урок не найден");
		}
		LessonResponse l = new LessonResponse();
		l.setId(source.getId());
		l.setStartTime(source.getStartTime());
		l.setEndTime(source.getEndTime());
		l.setDayOfWeek(source.getDayOfWeek());
		l.setTeacher(userDtoToResponseConverter.convert(source.getTeacher()));
		return l;
	}
}
