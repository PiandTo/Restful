package edu.school21.restful.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="course", schema="restful")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH })
    @JoinTable(
            name = "teachers_courses", schema = "Restful",
            joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<User> teachers = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(
            name = "students_courses", schema = "restful",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> students = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(
            name = "lessons_courses", schema = "restful",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lessons = new HashSet<>();

    public void addLessonToCourse(Lesson l) {
        this.getLessons().add(l);
        l.getCourses().add(this);
    }

    public void removeLessonFromCourse(Lesson l) {
        this.getLessons().remove(l);
        l.getCourses().remove(this);
    }

    public void removeUserFromCourse(User u) {
        this.getStudents().remove(u);
        this.getTeachers().remove(u);
        u.getListStudentCourses().remove(this);
        u.getListTeacherCourses().remove(this);
    }
}
