package edu.school21.restful.service;


import edu.school21.restful.DTO.LessonDTO;
import edu.school21.restful.converter.LessonRequestToLessonConverter;
import edu.school21.restful.converter.LessonToLessonDTOConverter;
import edu.school21.restful.exceptions.course.CourseNotFoundException;
import edu.school21.restful.exceptions.course.CourseNotSavedException;
import edu.school21.restful.exceptions.lesson.LessonNotFoundException;
import edu.school21.restful.exceptions.lesson.LessonNotSavedException;
import edu.school21.restful.exceptions.user.UserNotFoundException;
import edu.school21.restful.json.LessonRequest;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.repository.CourseRepository;
import edu.school21.restful.repository.LessonRepository;
import edu.school21.restful.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
	private final LessonRequestToLessonConverter lessonConverter;
	private final LessonToLessonDTOConverter lessonToLessonDTOConverter;
	private final LessonRepository lessonRepository;
	private final CourseRepository courseRepository;
	private final UserRepository userRepository;

	public Iterable<LessonDTO> getAllByCourse(Integer pageNo, Integer pageSize, String sortBy, long id) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Lesson> l = lessonRepository.findByCourses_Id(id, pageable);
		int size = 0;
		for (Lesson c: l) {
			size++;
		}
		if (size == 0) {throw new CourseNotFoundException("Not found course at all");}
		List<LessonDTO> lessonDTOS = new ArrayList<>();
		for (Lesson lesson : l){
			lessonDTOS.add(lessonToLessonDTOConverter.convert(lesson));
		}
		return lessonDTOS;
	}

	public LessonDTO addLessonToCourse(LessonRequest lessonDto, long id) {
		Optional<Course> c = courseRepository.findById(id);
		Optional<User> teacher = userRepository.findById(lessonDto.getTeacherId());
		if (!teacher.isPresent()) {
			throw new CourseNotSavedException("No teacher with id "+ id + ". We cant add Lesson");
		}
		Lesson lesson = lessonConverter.convert(lessonDto);
		if (c.isPresent()) {
			try {
				lesson.setTeacher(teacher.get());
				teacher.get().addLessonToTeacher(lesson);
				c.get().addLessonToCourse(lesson);
				Lesson lessonSaved = lessonRepository.save(lesson);
				return lessonToLessonDTOConverter.convert(lessonSaved);
			} catch (Exception e) {
				throw new CourseNotSavedException(e.getMessage());
			}
		}
		throw new CourseNotFoundException("No course with id " + id);
	}

	public Lesson findById(long id) {
		Optional<Lesson> lesson = lessonRepository.findById(id);
		if (lesson.isPresent()) {
			return lesson.get();
		}
		throw new LessonNotFoundException("No lesson with id " + id);
	}

	public LessonDTO updateLessonInCourse(Lesson oldLesson, Lesson newLesson, long teacher_id, long id) {
		Optional<User> teacher = userRepository.findById(teacher_id);
		if (!teacher.isPresent()) {
			throw new UserNotFoundException("No teacher with id " + teacher_id);
		}
		oldLesson.setDayOfWeek(newLesson.getDayOfWeek());
		oldLesson.setStartTime(newLesson.getStartTime());
		oldLesson.setEndTime(newLesson.getEndTime());
		oldLesson.setTeacher(teacher.get());
		try {
			Lesson l = lessonRepository.save(oldLesson);
			return lessonToLessonDTOConverter.convert(l);
		} catch (Exception e) {
			throw new LessonNotSavedException(e.getMessage());
		}
	}

	public void deleteLessonFromCourse(long course_id, long lesson_id) {
		Optional<Lesson> lesson = lessonRepository.findById(lesson_id);
		Optional<Course> course = courseRepository.findById(course_id);
		if (!course.isPresent()) {
			throw new CourseNotSavedException("No such course with id " + course_id);
		}
		if (lesson.isPresent()) {
			lesson.get().getTeacher().removeLessonFromTeacher(lesson.get());
			course.get().removeLessonFromCourse(lesson.get());
			lessonRepository.delete(lesson.get());
		} else {
			throw new LessonNotFoundException("No such lesson with id " + lesson_id);
		}
	}
}
