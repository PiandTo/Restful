package edu.school21.restful.converter;
import edu.school21.restful.models.User;
import edu.school21.restful.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<UserDTO, User> {
    @Override
    public UserDTO convert(User source) {
       UserDTO userDTO = new UserDTO();
       userDTO.setId(source.getId());
       userDTO.setName(source.getFirstName());
       userDTO.setSurname(source.getLastName());
       userDTO.setRole(source.getRole());
       userDTO.setLogin(source.getLogin());
       userDTO.setPassword(source.getPassword());

       return userDTO;
    }
}
