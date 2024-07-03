package com.hampcode.bankingservice.controllers;

import com.hampcode.bankingservice.model.dto.AuthRequestDTO;
import com.hampcode.bankingservice.model.dto.AuthResponseDTO;
import com.hampcode.bankingservice.model.dto.UserProfileDTO;
import com.hampcode.bankingservice.security.TokenProvider;
import com.hampcode.bankingservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class JWTController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDTO> getAccessToken(@RequestBody AuthRequestDTO authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
;
        String accessToken = tokenProvider.createAccessToken(authentication);
        UserProfileDTO userProfileDTO = userService.findByEmail(authRequest.getEmail());
        AuthResponseDTO authResponse = new AuthResponseDTO(accessToken, userProfileDTO);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(authResponse);
    }

}
