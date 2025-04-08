package com.mykola.crm.service.user;

import com.mykola.crm.dto.user.UserDetailsResponse;
import com.mykola.crm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    public UserDetailsResponse getUserDetails(User currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return new UserDetailsResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getYearsOld()
        );
    }
}