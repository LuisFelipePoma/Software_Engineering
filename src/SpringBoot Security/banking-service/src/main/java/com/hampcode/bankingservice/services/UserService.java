package com.hampcode.bankingservice.services;

import com.hampcode.bankingservice.exceptions.BadRequestException;
import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.UserMapper;
import com.hampcode.bankingservice.model.dto.SignupFormDTO;
import com.hampcode.bankingservice.model.dto.UserProfileDTO;
import com.hampcode.bankingservice.model.entities.User;
import com.hampcode.bankingservice.model.entities.enums.Role;
import com.hampcode.bankingservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    public UserProfileDTO signup(SignupFormDTO signupFormDTO) {
        boolean emailAlreadyExists = userRepository.existsByEmail(signupFormDTO.getEmail());

        if (emailAlreadyExists) {
            throw new BadRequestException("El email ya está siendo usado por otro usuario.");
        }

        User user = userMapper.convertToEntity(signupFormDTO);
        user.setPassword(passwordEncoder.encode(signupFormDTO.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        // TODO: generar un token de verificación
        // TODO: envía un email con el token de verificación
        // el estado actual sería no verificado

        return  userMapper.convertToDTO(user);
    }

    public UserProfileDTO findByEmail(String email) {
        User user = userRepository
                .findOneByEmail(email)
                .orElseThrow(ResourceNotFoundException::new);

        return userMapper.convertToDTO(user);
    }

}
