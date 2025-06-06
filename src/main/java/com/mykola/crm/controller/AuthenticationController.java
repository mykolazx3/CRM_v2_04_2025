package com.mykola.crm.controller;

import com.mykola.crm.dto.authentication.LoginRequest;
import com.mykola.crm.dto.authentication.RegistrationRequest;
import com.mykola.crm.dto.authentication.TokenResponse;
import com.mykola.crm.model.User;
import com.mykola.crm.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        TokenResponse tokenResponse = authenticationService.register(registrationRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@AuthenticationPrincipal User currentUser) {
        TokenResponse tokenResponse = authenticationService.refreshToken(currentUser);
        return ResponseEntity.ok(tokenResponse);
    }

}
