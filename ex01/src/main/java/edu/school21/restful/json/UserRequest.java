package edu.school21.restful.json;

import com.sun.istack.NotNull;
import edu.school21.restful.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @Schema(example= "Дарья", description = "Имя пользователя")
    private String firstName;

    @Schema(example= "Словецких", description = "Фамилия пользователя")
    private String lastName;

    @Schema(example= "ADMINISTRATOR", description = "Роль")
    private Role role;

    @NotNull
    @Schema(example= "mdasha1979@yandex.ru", description = "email")
    private String login;

    @Schema(example= "Pers1234", description = "Пароль")
    private String password;
}
