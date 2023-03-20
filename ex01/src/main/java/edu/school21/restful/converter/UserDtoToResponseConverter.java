package edu.school21.restful.converter;

import edu.school21.restful.json.UserResponse;
import edu.school21.restful.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToResponseConverter implements Converter<UserResponse, UserDTO> {

    @Override
    public UserResponse convert(UserDTO source) {

        return new UserResponse(
                source.getId(),
                source.getName(),
                source.getSurname()
        );
    }
}
