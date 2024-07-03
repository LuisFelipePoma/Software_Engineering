package com.hampcode.bankingservice.mapper;

import com.hampcode.bankingservice.model.dto.SignupFormDTO;
import com.hampcode.bankingservice.model.dto.UserProfileDTO;
import com.hampcode.bankingservice.model.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User convertToEntity(SignupFormDTO signupFormDTO){
        return  modelMapper.map(signupFormDTO, User.class);
    }

    public UserProfileDTO convertToDTO(User user){
        return  modelMapper.map(user, UserProfileDTO.class);
    }
}
