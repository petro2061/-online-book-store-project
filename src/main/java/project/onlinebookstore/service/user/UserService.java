package project.onlinebookstore.service.user;

import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException;
}
