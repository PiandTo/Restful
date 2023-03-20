package edu.school21.restful.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.school21.restful.models.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime startTime;
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime endTime;
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;
	private long teacherId;
}
