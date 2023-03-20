package edu.school21.restful.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.school21.restful.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserResponse {

    @Schema(example= "1", description = "id пользователя")
    private Long id;

    @Schema(example= "Дарья", description = "Имя пользователя")
    private String firstName;

    @Schema(example= "Словецких", description = "Фамилия пользователя")
    private String lastName;
}
