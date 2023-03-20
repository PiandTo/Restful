package edu.school21.restful.DTO;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CourseDTO {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String courseName;
    private String description;
}
