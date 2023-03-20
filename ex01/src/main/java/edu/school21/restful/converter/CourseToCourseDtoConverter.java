package edu.school21.restful.converter;

import edu.school21.restful.models.Course;
import edu.school21.restful.DTO.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseToCourseDtoConverter implements Converter<CourseDTO, Course> {

    @Override
    public CourseDTO convert(Course source) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(source.getId());
        courseDTO.setStartDate(source.getStartDate());
        courseDTO.setEndDate(source.getEndDate());
        courseDTO.setCourseName(source.getName());
        courseDTO.setDescription(source.getDescription());

        return courseDTO;
    }
}
