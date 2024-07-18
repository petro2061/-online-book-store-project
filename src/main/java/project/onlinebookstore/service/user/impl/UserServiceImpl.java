package project.onlinebookstore.service.user.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.user.UserRegistrationRequestDto;
import project.onlinebookstore.dto.user.UserResponseDto;
import project.onlinebookstore.exception.RegistrationException;
import project.onlinebookstore.mapper.UserMapper;
import project.onlinebookstore.model.Role;
import project.onlinebookstore.model.User;
import project.onlinebookstore.repository.role.RoleRepository;
import project.onlinebookstore.repository.user.UserRepository;
import project.onlinebookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException("Can't registration user. User with email: "
                    + registrationRequestDto.getEmail()
                    + " already exist");
        }
        Role userRole
                = roleRepository
                .findByRole(Role.RoleName.USER)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role: USER"));

        User savedUserModel = userMapper.toUserModel(registrationRequestDto);

        savedUserModel
                .setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        savedUserModel
                .getRoles().add(userRole);
        return userMapper.toUserDto(userRepository.save(savedUserModel));
    }
}
