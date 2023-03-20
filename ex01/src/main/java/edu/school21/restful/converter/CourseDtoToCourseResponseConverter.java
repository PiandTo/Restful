package edu.school21.restful.converter;

import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.json.CourseResponse;
import edu.school21.restful.DTO.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseDtoToCourseResponseConverter implements Converter<CourseResponse, CourseDTO> {
    @Override
    public CourseResponse convert(CourseDTO source) {
        if (source == null) {
            throw new CourseNotFoundException("Курс не найден");
        }
        return new CourseResponse(
                source.getId(),
                source.getStartDate(),
                source.getEndDate(),
                source.getCourseName(),
                source.getDescription()
        );
    }
}
