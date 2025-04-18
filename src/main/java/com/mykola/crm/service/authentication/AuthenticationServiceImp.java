package com.mykola.crm.service.authentication;

import com.mykola.crm.config.security.JwtUtil;
import com.mykola.crm.dto.authentication.LoginRequest;
import com.mykola.crm.dto.authentication.RegistrationRequest;
import com.mykola.crm.dto.authentication.TokenResponse;
import com.mykola.crm.exception.UserAlreadyExistsException;
import com.mykola.crm.model.User;
import com.mykola.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse register(RegistrationRequest registrationRequest) {

        if (userRepository.existsByUsernameOrEmail(registrationRequest.getUsername(),
                registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username or Email already exists");
        }

        // Збереження користувача
        User user = userRepository.save(new User(
                registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                registrationRequest.getEmail(),
                registrationRequest.getYearsOld()
        ));

        // Аутентифікація нового користувача
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                registrationRequest.getPassword()));

        // Оновлення SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерація токена
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        return new TokenResponse(jwtToken);
    }

    public TokenResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            return new TokenResponse(jwtToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public TokenResponse refreshToken(User currentUser) {
        // Генеруємо новий токен
        String newJwtToken = jwtUtil.generateToken(currentUser.getUsername());

        // Повертаємо новий токен в об'єкті TokenResponse
        return new TokenResponse(newJwtToken);
    }
}
