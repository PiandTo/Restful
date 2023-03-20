package edu.school21.restful.service;

import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.converter.CourseToCourseDtoConverter;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.course.CourseNotSavedException;
import edu.school21.restful.json.CourseRequest;
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

    public CourseDTO addCourse(
            LocalDate startDate,
            LocalDate endDate,
            String name,
            String description
            ) {
        Course newCourse = new Course();

        newCourse.setStartDate(startDate);
        newCourse.setEndDate(endDate);
        newCourse.setName(name);
        newCourse.setDescription(description);
        courseRepository.save(newCourse);
        return courseToCourseDtoConverter.convert(newCourse);
    }

    public Iterable<CourseDTO> findAllCourses(Integer pageNo, Integer pageSize, String sortBy) {
        int size = 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Iterable<Course> courses = courseRepository.findAll(pageable);
        for (Course c: courses) {
            size++;
        }
        if (size == 0) {throw new CourseNotFoundException("Not found course at all");}
        List<CourseDTO> courseDTO = new ArrayList<>();
        for (Course c : courses) {
            courseDTO.add(courseToCourseDtoConverter.convert(c));
        }
        return courseDTO;
    }

    public CourseDTO findById (long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            return courseToCourseDtoConverter.convert(course.get());
        } else {
            throw new CourseNotFoundException("No course with id " + id);
        }
    }

    public CourseDTO update(long id, CourseRequest courseDTO) {
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isPresent()) {
                Course newCourse = course.get();
                newCourse.setName(courseDTO.getCourseName());
                newCourse.setDescription(courseDTO.getDescription());
                newCourse.setStartDate(courseDTO.getStartDate());
                newCourse.setEndDate(courseDTO.getEndDate());
                return courseToCourseDtoConverter.convert(courseRepository.save(newCourse));
            }
        } catch (DataAccessException e) {
            throw new CourseNotSavedException("We can't save Course " + courseDTO.toString());
        } catch (IllegalArgumentException e) {
            throw new CourseNotFoundException("We can't found Course with id " + id);
        }
        return null;
    }

    public void delete(long id) {
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isPresent()) {
                for (User c : course.get().getTeachers()) {
                    course.get().getTeachers().remove(c);
                }
                courseRepository.delete(course.get());
            }
        } catch (DataAccessException e) {
            throw new CourseNotSavedException("We can't delete Course");
        } catch (IllegalArgumentException e) {
            throw new CourseNotFoundException("We can't found Course with id " + id);
        }
    }
}
