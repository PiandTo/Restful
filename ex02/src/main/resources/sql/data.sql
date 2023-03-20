INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (10, 'Mikhail1', 'Malev1', 'STUDENT', 'snaomi1', '1');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (20, 'Mikhail2', 'Malev2', 'ADMINISTRATOR', 'snaomi2', '2');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (30, 'Mikhail3', 'Malev3', 'STUDENT', 'snaomi3', '3');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (40, 'Mikhail4', 'Malev4', 'TEACHER', 'snaomi4', '4');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (50, 'Mikhail5', 'Malev5', 'TEACHER', 'snaomi5', '5');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (60, 'Mikhail6', 'Malev6', 'TEACHER', 'snaomi6', '6');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (70, 'Mikhail7', 'Malev7', 'TEACHER', 'snaomi8', '7');
INSERT INTO restful.user (id, first_name, last_name, role, login, password)
values (80, 'Mikhail8', 'Malev8', 'TEACHER', 'snaomi8', '8');

INSERT INTO restful.course (id, start_date, end_date, name, description, status, is_published)
VALUES (10, '2022-10-10', '2022-10-22', 'math', 'algebra', 'DRAFT', false) ;
INSERT INTO restful.course (id, start_date, end_date, name, description, status, is_published)
VALUES (20, '2022-10-10', '2022-10-22', 'math1', 'algebra1', 'DRAFT', false) ;
INSERT INTO restful.course (id, start_date, end_date, name, description, status, is_published)
VALUES (30, '2022-10-10', '2022-10-22', 'math2', 'algebra2', 'DRAFT', false) ;

INSERT INTO restful.lesson (id, start_time, end_time, day_of_week, teacher_id)
VALUES (10, '09:52:20.751278', '19:52:20.751278', 'WEDNESDAY', 50) ;
INSERT INTO restful.lesson (id, start_time, end_time, day_of_week, teacher_id)
VALUES (11, '09:52:20.751278', '19:52:20.751278', 'WEDNESDAY', 60) ;
INSERT INTO restful.lesson (id, start_time, end_time, day_of_week, teacher_id)
VALUES (12, '09:52:20.751278', '19:52:20.751278', 'MONDAY', 80) ;
INSERT INTO restful.lesson (id, start_time, end_time, day_of_week, teacher_id)
VALUES (13, '09:52:20.751278', '19:52:20.751278', 'FRIDAY', 50) ;

INSERT INTO restful.lessons_courses (lesson_id, course_id) VALUES (10,20);
INSERT INTO restful.lessons_courses (lesson_id, course_id) VALUES (10,30);
INSERT INTO restful.lessons_courses (lesson_id, course_id) VALUES (11,20);
INSERT INTO restful.lessons_courses (lesson_id, course_id) VALUES (12,30);
INSERT INTO restful.lessons_courses (lesson_id, course_id) VALUES (13,20);

INSERT INTO restful.students_courses (student_id, course_id) VALUES (10,20);
INSERT INTO restful.students_courses (student_id, course_id) VALUES (10,30);
INSERT INTO restful.students_courses (student_id, course_id) VALUES (30,20);

INSERT INTO restful.teachers_courses(teacher_id, course_id) VALUES (40, 10);
INSERT INTO restful.teachers_courses(teacher_id, course_id) VALUES (20, 30);
