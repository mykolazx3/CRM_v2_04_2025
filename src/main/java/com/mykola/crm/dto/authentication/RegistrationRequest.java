package com.mykola.crm.dto.authentication;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;


    @NotNull(message = "Username is required")
    @Min(value = 18, message = "You must be at least 18 years old")
    private byte yearsOld;
}