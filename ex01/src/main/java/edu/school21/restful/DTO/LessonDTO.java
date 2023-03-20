package edu.school21.restful.DTO;

import edu.school21.restful.models.DayOfWeek;
import edu.school21.restful.models.User;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LessonDTO {
	private long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private DayOfWeek dayOfWeek;
	private UserDTO teacher;
}
