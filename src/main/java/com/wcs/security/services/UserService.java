package com.wcs.security.services;

import com.wcs.security.enums.RoleName;
import com.wcs.security.models.User;

import java.util.List;

public interface UserService {
    User getUserByEmail (String email);
    User createUser (User user) ;
    void addRoleToUser (String email, RoleName roleName) ;

    String login (String email, String password);
    List<User> getAllUser();
}
