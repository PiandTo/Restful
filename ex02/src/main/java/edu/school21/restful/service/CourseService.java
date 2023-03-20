package edu.school21.restful.service;

import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.converter.CourseToCourseDtoConverter;
import edu.school21.restful.enums.CourseStatus;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.course.CourseNotPublishedException;
import edu.school21.restful.exceptions.course.CourseNotSavedException;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.User;
import edu.school21.restful.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final CourseToCourseDtoConverter courseToCourseDtoConverter;


    public CourseDTO findById(long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return courseToCourseDtoConverter.convert(course.get());
        } else {
            throw new CourseNotFoundException("No course with id " + id);
        }
    }

    public CourseDTO publishCourseById(long id) {
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isPresent()) {
                Course publishedCourse = course.get();
                if (publishedCourse.getStatus().equals(CourseStatus.DRAFT)) {
                    publishedCourse.setStatus(CourseStatus.PUBLISHED);
                    publishedCourse.setPublished(true);
                    courseRepository.save(publishedCourse);

                }
                return courseToCourseDtoConverter.convert(publishedCourse);
            }
        } catch (DataAccessException e) {
            throw new CourseNotSavedException("We can't save Course with id: " + id);
        } catch (IllegalArgumentException e) {
            throw new CourseNotFoundException("We can't find Course with id: " + id);
        }
        return null;
    }

}
