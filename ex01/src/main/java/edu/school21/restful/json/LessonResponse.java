package edu.school21.restful.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.school21.restful.models.DayOfWeek;
import edu.school21.restful.models.User;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LessonResponse {
	private long id;
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime startTime;
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime endTime;
	private DayOfWeek dayOfWeek;
	private UserResponse teacher;
}
