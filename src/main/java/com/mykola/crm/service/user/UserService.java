package com.mykola.crm.service.user;

import com.mykola.crm.dto.user.UserDetailsResponse;
import com.mykola.crm.model.User;

public interface UserService {

    UserDetailsResponse getUserDetails(User currentUser);
}