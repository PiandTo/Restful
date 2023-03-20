package edu.school21.restful.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="user", schema="restful")
public class User  {
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

    @JsonIgnore
    @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "teachers")
    private Set<Course> listTeacherCourses = new HashSet<>();

    @JsonIgnore
    @ManyToMany (cascade = { CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "students")
    private Set<Course> listStudentCourses = new HashSet<>();

//    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "teacher")
    private Set<Lesson> lessons = new HashSet<>();

    public User(String f, String l, Role r, String lo, String p) {
        this.firstName = f;
        this.lastName = l;
        this.role = r;
        this.login = lo;
        this.password = p;
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
