package edu.school21.restful.DTO;

import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UserDTO extends RepresentationModel<UserDTO> {
    private long id;
    private String name;
    private String surname;
    private Role role;
    private String login;
    private String password;
}
