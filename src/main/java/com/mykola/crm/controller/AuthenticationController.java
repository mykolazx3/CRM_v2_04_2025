package com.mykola.crm.controller;

import com.mykola.crm.dto.authentication.LoginRequest;
import com.mykola.crm.dto.authentication.RegistrationRequest;
import com.mykola.crm.dto.authentication.TokenResponse;
import com.mykola.crm.model.User;
import com.mykola.crm.service.authentication.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Value("${jwt.expiration.time}")
    private int expiration;
    private final AuthenticationService authenticationService;


//    LocalStorage
//    @PostMapping("/register")
//    public ResponseEntity<TokenResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
//        TokenResponse tokenResponse = authenticationService.register(registrationRequest);
//        return ResponseEntity.ok(tokenResponse);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
//        TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);
//        return ResponseEntity.ok(tokenResponse);
//    }
//
//
//    @GetMapping("refresh-token")
//    public ResponseEntity<TokenResponse> refreshToken(@AuthenticationPrincipal User currentUser) {
//        TokenResponse tokenResponse = authenticationService.refreshToken(currentUser);
//        return ResponseEntity.ok(tokenResponse);
//    }




    //Cookie
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest registrationRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authenticationService.register(registrationRequest);
        String jwtToken = tokenResponse.getToken(); // Викликаємо допоміжний метод для налаштування cookie
        addJwtCookie(response, jwtToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);
        String jwtToken = tokenResponse.getToken(); // Викликаємо допоміжний метод для налаштування cookie
        addJwtCookie(response, jwtToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@AuthenticationPrincipal User currentUser, HttpServletResponse response) {
        TokenResponse tokenResponse = authenticationService.refreshToken(currentUser);
        String jwtToken = tokenResponse.getToken(); // Викликаємо допоміжний метод для налаштування cookie
        addJwtCookie(response, jwtToken);
        return ResponseEntity.ok().build();
    }

    private void addJwtCookie(HttpServletResponse response, String jwtToken) {
        Cookie cookie = new Cookie("JWT", jwtToken);
        cookie.setHttpOnly(true);  // Зробити cookie недоступним для JavaScript
        cookie.setSecure(false);   // Якщо сайт використовує HTTPS
        cookie.setPath("/");       // Встановлюємо шлях доступу до cookie
//        cookie.setMaxAge(3600 * expiration);    // Термін дії cookie в секундах (1 година)
        cookie.setMaxAge(60 * expiration);    // Термін дії cookie в секундах (1 година)
        response.addCookie(cookie);
    }
}