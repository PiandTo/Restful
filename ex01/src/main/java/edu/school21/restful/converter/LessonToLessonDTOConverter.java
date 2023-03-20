package edu.school21.restful.converter;

import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.DTO.LessonDTO;
import edu.school21.restful.models.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonToLessonDTOConverter implements Converter<LessonDTO, Lesson>{

	@Autowired
	private UserToUserDtoConverter userToUserDtoConverter;

	@Override
	public LessonDTO convert(Lesson source) {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setId(source.getId());
		lessonDTO.setEndTime(source.getEndTime());
		lessonDTO.setStartTime(source.getStartTime());
		lessonDTO.setDayOfWeek(source.getDayOfWeek());
		lessonDTO.setTeacher(userToUserDtoConverter.convert(source.getTeacher()));

		return lessonDTO;
	}
}
