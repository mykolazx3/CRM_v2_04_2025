package com.mykola.crm.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //LocalStorage
//        String token = extractTokenFromHeader(request);
        //Cookie
        String token = extractTokenFromCookie(request);// Отримуємо JWT з cookie

        if (token != null && jwtUtil.isValidToken(token)) {
            String username = jwtUtil.extractUsername(token); // Отримуємо ім'я користувача з токена
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // Створюємо аутентифікацію для користувача
            SecurityContextHolder.getContext().setAuthentication(authentication); // Встановлюємо аутентифікацію в контекст безпеки
        }

        filterChain.doFilter(request, response);
    }
    //LocalStorage
//    private String extractTokenFromHeader(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
//            return authHeader.substring("Bearer ".length());
//        }
//        return null;
//    }

    //Cookie
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();// Отримуємо cookie з запиту
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}