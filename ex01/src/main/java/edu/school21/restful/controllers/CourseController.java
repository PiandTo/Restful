package edu.school21.restful.controllers;

import edu.school21.restful.DTO.LessonDTO;
import edu.school21.restful.DTO.UserDTO;
import edu.school21.restful.converter.CourseDtoToCourseResponseConverter;
import edu.school21.restful.converter.LessonDtoToLessonResponseConverter;
import edu.school21.restful.converter.LessonRequestToLessonConverter;
import edu.school21.restful.converter.UserDtoToResponseConverter;
import edu.school21.restful.json.*;
import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.service.CourseService;
import edu.school21.restful.service.LessonService;
import edu.school21.restful.service.StudentService;
import edu.school21.restful.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Tag(
        name ="Курсы (Course)",
        description = "Все методы для работы с курсами (CourseController)"
)
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    private final CourseDtoToCourseResponseConverter courseConverter;
    private final LessonDtoToLessonResponseConverter lessonConverter;
    private final LessonRequestToLessonConverter lessonRequestToLessonConverter;
    private final UserDtoToResponseConverter converter;

    @PostMapping()
    @Operation(summary="Добавить новый курс (addCourse)")
    public @ResponseBody
    ResponseEntity<CourseResponse> addCourse(@RequestBody @Valid CourseRequest request) {
        CourseDTO course = courseService.addCourse(
                request.getStartDate(),
                request.getEndDate(),
                request.getCourseName(),
                request.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(courseConverter.convert(course));
    }

    @GetMapping()
    @Operation(summary = "Извлечь все курсы (getAllCourses)")
    public ResponseEntity<?> getAllCourses(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
    ) {
        Iterable<CourseDTO> courseDTOS = courseService.findAllCourses(pageNo, pageSize, sortBy);
        HashSet<CourseResponse> courseResponses = new HashSet<>();
        for (CourseDTO c : courseDTOS) {
            courseResponses.add(courseConverter.convert(c));
        }
        return ResponseEntity.ok().body(courseResponses);
    }

    @GetMapping("/{course_id}")
    @Operation(summary = "Извлечение курса по id")
    public ResponseEntity<?> getCourse(@PathVariable("course_id") long id) {
        CourseDTO courseDTO = courseService.findById(id);
        return ResponseEntity.ok().body(courseDTO);
    }

    @PutMapping("/{course_id}")
    @Operation(summary = "Обновление информациии о Курсе по ID")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable("course_id") long id, @RequestBody CourseRequest course) {
        CourseDTO courseDTO = courseService.update(id, course);
        return ResponseEntity.ok().body(courseConverter.convert(courseDTO));
    }

    @DeleteMapping("/{course_id}")
    @Operation(summary ="Удаление информациии о Курсе по ID")
    public ResponseEntity<?> delete (@PathVariable("course_id") long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{course_id}/lessons")
    @Operation(summary = "Извлечь все Уроки по Курсу")
    public ResponseEntity<Iterable<LessonResponse>> getLessonsByCourse (@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                                    @RequestParam(name = "pageSize", defaultValue = "1") int pageSize,
                                                                    @RequestParam(name= "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                    @PathVariable(name = "course_id") long id) {
        Iterable<LessonDTO> lessons = lessonService.getAllByCourse(pageNo, pageSize, sortBy, id);
        List<LessonResponse> l = new ArrayList<>();
        for (LessonDTO k : lessons) {
            l.add(lessonConverter.convert(k));
        }
        return ResponseEntity.ok().body(l);
    }

    @PostMapping("/{course_id}/lessons")
    @Operation(summary = "Добавление нового урока в курс")
    public ResponseEntity<LessonResponse> addLessonToCourse (
            @RequestBody LessonRequest l,
            @PathVariable(name = "course_id") long id) {
        LessonDTO lesson = lessonService.addLessonToCourse(l, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonConverter.convert(lesson));
    }

    @PutMapping("/{course_id}/lessons/{lesson_id}")
    @Operation(summary = "Обновление информации по Уроку")
    public ResponseEntity<LessonResponse> updateLessonInCourse(
            @PathVariable(name = "course_id") long course_id,
            @PathVariable(name = "lesson_id") long lesson_id,
            @RequestBody LessonRequest lesson
    ) {
        Lesson oldLesson = lessonService.findById(lesson_id);
        LessonDTO newLesson = lessonService.updateLessonInCourse(oldLesson, lessonRequestToLessonConverter.convert(lesson), lesson.getTeacherId(), course_id);
        return ResponseEntity.ok(lessonConverter.convert(newLesson));
    }

    @DeleteMapping("/{course_id}/lessons/{lesson_id}")
    @Operation(summary = "Удаление урока по id из Курса")
    public ResponseEntity<?> deleteLessonFromCourse(
            @PathVariable("course_id") long course_id,
            @PathVariable("lesson_id") long lesson_id
    ) {
        lessonService.deleteLessonFromCourse(course_id, lesson_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{course_id}/students")
    @Operation(summary = "Формирование списка всех Студентов курса")
    public ResponseEntity<?> getStudentsFromCourse(
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "1") int pageSize,
            @RequestParam(name= "sortBy", defaultValue = "id", required = false) String sortBy,
            @PathVariable("course_id") long id) {
        Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        HashSet<UserDTO> studentsDto = studentService.findAllByRoleAndId(id, p);
        HashSet<UserResponse> students = new HashSet();
        for (UserDTO k : studentsDto) {
            students.add(converter.convert(k));
        }
        return ResponseEntity.ok().body(students);
    }

    @PostMapping("/{course_id}/students")
    @Operation(summary = "Добавление нового студента на Курс")
    public ResponseEntity<UserResponse> addStudentToCourse(
            @PathVariable("course_id") long id,
            @RequestBody UserRequest user
    ) {
        UserResponse addedStudent = converter.convert(studentService.addStudentToCourse(user, id));
        return ResponseEntity.ok().body(addedStudent);
    }

    @DeleteMapping("/{course_id}/students/{student_id}")
    @Operation(summary = "Удаление Студента из Курса")
    public ResponseEntity<?> deleteStudentFromCourse(
            @PathVariable("course_id") long course_id,
            @PathVariable("student_id") long student_id
    ) {
        studentService.deleteStudentFromCourse(course_id, student_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{course_id}/teachers")
    @Operation(summary = "Формирование списка всех Учителей курса")
    public ResponseEntity<?> getTeachersFromCourse(
            @RequestParam(value = "pageSize", defaultValue = "1") int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @PathVariable("course_id") long id) {
        Pageable p = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        HashSet<UserDTO> teachersDto = teacherService.findAllByRoleAndId(id, p);
        HashSet<UserResponse> students = new HashSet();
        for (UserDTO k : teachersDto) {
            students.add(converter.convert(k));
        }
        return ResponseEntity.ok().body(students);
    }

    @PostMapping("/{course_id}/teachers")
    @Operation(summary = "Добавление нового учителя на Курс")
    public ResponseEntity<UserResponse> addTeacherToCourse(
            @PathVariable("course_id") long id,
            @RequestBody UserRequest user
    ) {
        UserDTO addedTeacher = teacherService.addTeacherToCourse(user, id);
        return ResponseEntity.ok().body(converter.convert(addedTeacher));
    }

    @DeleteMapping("/{course_id}/teachers/{teacher_id}")
    @Operation(summary = "Удаление Учителя из Курса")
    public ResponseEntity<?> deleteTeacherFromCourse(
            @PathVariable("course_id") long course_id,
            @PathVariable("teacher_id") long teacher_id
    ) {
        teacherService.deleteTeacherFromCourse(course_id, teacher_id);
        return ResponseEntity.ok().build();
    }
}
