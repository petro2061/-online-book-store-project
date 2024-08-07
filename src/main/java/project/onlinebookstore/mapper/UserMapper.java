package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toUserModel(UserRegistrationRequestDto registrationRequestDto);
}
