package com.hampcode.bankingservice.controllers;


import com.hampcode.bankingservice.model.dto.SignupFormDTO;
import com.hampcode.bankingservice.model.dto.UserProfileDTO;
import com.hampcode.bankingservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public UserProfileDTO signup(@RequestBody @Validated SignupFormDTO signupFormDTO) {
        return userService.signup(signupFormDTO);
    }

}
