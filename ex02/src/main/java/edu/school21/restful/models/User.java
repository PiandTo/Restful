package edu.school21.restful.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="user", schema="restful")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "teachers")
    private Set<Course> listTeacherCourses = new HashSet<>();

    @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "students")
    private Set<Course> listStudentCourses = new HashSet<>();

//    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "teacher")
    private Set<Lesson> lessons = new HashSet<>();

    public void addCourse(Course course) {
        this.listTeacherCourses.add(course);
        course.getTeachers().add(this);
    }

    public void removeCourse(Course course) {
        this.listTeacherCourses.remove(course);
        course.getTeachers().remove(this);
    }

    public void addStudent(Course course) {
        this.listStudentCourses.add(course);
        course.getStudents().add(this);
    }

    public void removeStudent(Course course) {
        this.listStudentCourses.remove(course);
        course.getStudents().remove(this);
    }

    public void addTeacher(Course course) {
        this.listTeacherCourses.add(course);
        course.getTeachers().add(this);
    }

    public void removeTeacher(Course course) {
        this.listTeacherCourses.remove(course);
        course.getTeachers().remove(this);
    }

    public void addLessonToTeacher(Lesson lesson) {
        lessons.add(lesson);
        lesson.setTeacher(this);
    }
    public void removeLessonFromTeacher(Lesson l) {
        lessons.remove(l);
    }

}
