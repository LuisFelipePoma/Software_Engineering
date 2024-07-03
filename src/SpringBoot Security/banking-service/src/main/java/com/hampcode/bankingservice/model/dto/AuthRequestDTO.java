package com.hampcode.bankingservice.model.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}