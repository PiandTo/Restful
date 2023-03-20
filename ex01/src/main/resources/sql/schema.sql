DROP SCHEMA if exists Restful CASCADE ;

CREATE SCHEMA Restful
    CREATE TABLE Restful.User (
         id serial primary key,
         first_name VARCHAR(50),
         last_name VARCHAR(50),
         role VARCHAR(30),
         login VARCHAR(30),
         password VARCHAR(255)
    );

    CREATE TABLE Restful.Course (
        id serial primary key,
        start_date date,
        end_date date,
        name VARCHAR(30),
        description VARCHAR(255)
    );

    CREATE TABLE Restful.Lesson (
        id serial primary key,
        start_time time,
        end_time time,
        day_of_week VARCHAR(30),
        teacher_id int not null,
        foreign key (teacher_id) references Restful.User(id)
    );

CREATE TABLE Restful.teachers_courses (
        course_id int not null,
        teacher_id int not null,
        primary key (course_id, teacher_id),
        foreign key (teacher_id) references Restful.User(id) on update cascade,
        foreign key (course_id) references Restful.Course(id)  on update cascade
);

CREATE TABLE Restful.students_courses (
        course_id int not null,
        student_id int not null,
        primary key (course_id, student_id),
        foreign key (student_id) references Restful.User(id) on update cascade,
        foreign key (course_id) references Restful.Course(id)  on update cascade
);

CREATE TABLE Restful.lessons_courses (
      lesson_id int not null,
      course_id int not null,
      primary key (lesson_id, course_id),
      foreign key (lesson_id) references Restful.Lesson(id) on update cascade,
      foreign key (course_id) references Restful.Course(id)  on update cascade
);

CREATE TABLE Restful.access_token (
                                      id serial primary key,
                                      login VARCHAR(30),
                                      password text,
                                      is_expired bool,
                                      created timestamp,
                                      updated timestamp
);

CREATE TABLE Restful.refresh_token (
                                       id serial primary key,
                                       login VARCHAR(30),
                                       password text,
                                       is_expired bool,
                                       created timestamp,
                                       updated timestamp
);