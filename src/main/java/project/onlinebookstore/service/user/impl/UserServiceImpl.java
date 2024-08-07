package project.onlinebookstore.service.user.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
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
import project.onlinebookstore.service.shopingcart.ShoppingCartService;
import project.onlinebookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException("Can't registration user. User with email: "
                    + registrationRequestDto.getEmail()
                    + " already exist");
        }
        Role userRole = roleRepository
                .findByRole(Role.RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role: "
                                + Role.RoleName.ROLE_USER));
        User user = userMapper.toUserModel(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        shoppingCartService.createShoppingCart(user);

        return userMapper.toUserDto(user);
    }
}
