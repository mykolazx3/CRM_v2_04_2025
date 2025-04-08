package com.mykola.crm.controller.user;

import com.mykola.crm.dto.user.UserDetailsResponse;
import com.mykola.crm.model.User;
import com.mykola.crm.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/get-user")
    public ResponseEntity<UserDetailsResponse> getUserDetails (@AuthenticationPrincipal User currentUser){
        UserDetailsResponse userDetailsResponse = userService.getUserDetails (currentUser);
        return ResponseEntity.ok(userDetailsResponse);
    }
}