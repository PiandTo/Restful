package edu.school21.restful.json;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    @Schema(example= "1", description = "id курса")
    private Long id;

    @Schema(example="01.01.2023",description="Дата начала курса")
    private LocalDate startDate;

    @Schema(example="31.05.2023",description="Дата окончания курса")
    private LocalDate endDate;

    @Schema(example="Программирование на Python",description="Название курса")
    private String courseName;

    @Schema(example="Основы программирования на Python для начинающих",description="Краткое описание курса")
    private String description;
}
