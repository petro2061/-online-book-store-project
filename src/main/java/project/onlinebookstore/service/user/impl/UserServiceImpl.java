package project.onlinebookstore.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.exception.RegistrationException;
import project.onlinebookstore.mapper.UserMapper;
import project.onlinebookstore.model.User;
import project.onlinebookstore.repository.user.UserRepository;
import project.onlinebookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(registrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't registration user. User with email: "
                    + registrationRequestDto.getEmail()
                    + " already exist");
        }
        User user = new User();
        user.setEmail(registrationRequestDto.getEmail());
        user.setPassword(registrationRequestDto.getPassword());
        user.setFirstName(registrationRequestDto.getFirstName());
        user.setLastName(registrationRequestDto.getLastName());
        user.setShippingAddress(registrationRequestDto.getShippingAddress());
        return userMapper.toUserDto(userRepository.save(user));
    }
}
