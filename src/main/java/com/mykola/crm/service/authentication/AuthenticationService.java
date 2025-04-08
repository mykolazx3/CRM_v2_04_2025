package com.mykola.crm.service.authentication;

import com.mykola.crm.dto.authentication.LoginRequest;
import com.mykola.crm.dto.authentication.RegistrationRequest;
import com.mykola.crm.dto.authentication.TokenResponse;
import com.mykola.crm.model.User;

public interface AuthenticationService {
    TokenResponse register(RegistrationRequest registrationRequest);
    TokenResponse authenticate(LoginRequest loginRequest);
    TokenResponse refreshToken(User currentUser);
}