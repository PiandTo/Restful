package edu.school21.restful.DTO;

import edu.school21.restful.enums.CourseStatus;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
@Data
public class CourseDTO extends RepresentationModel<CourseDTO>  {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String courseName;
    private String description;
    private CourseStatus status;
    private boolean published;
}
