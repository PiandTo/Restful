package edu.school21.restful.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    @Schema(example="01:01:2023",description="Дата начала курса")
    @JsonFormat(pattern = "dd:MM:yyyy")
    private LocalDate startDate;

    @Schema(example="31:05:2023",description="Дата окончания курса")
    @JsonFormat(pattern = "dd:MM:yyyy")
    private LocalDate endDate;

    @Schema(example="Программирование на Python",description="Название курса")
    private String courseName;

    @Schema(example="Основы программирования на Python для начинающих",description="Краткое описание курса")
    private String description;
}
