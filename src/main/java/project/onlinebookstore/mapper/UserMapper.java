package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserDto(User user);

    User toUserModel(UserRegistrationRequestDto registrationRequestDto);
}
