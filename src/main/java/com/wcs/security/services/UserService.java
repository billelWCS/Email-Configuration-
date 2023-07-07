package com.wcs.security.services;

import com.wcs.security.Exceptions.JWTException;
import com.wcs.security.Exceptions.UserNotFoundException;
import com.wcs.security.enums.RoleName;
import com.wcs.security.models.User;

import java.util.List;
import java.util.jar.JarException;

public interface UserService {
    User getUserByEmail (String email);
    User createUser (User user) ;
    User addRoleToUser (String email, RoleName roleName) ;

    String login (String email, String password);
    List<User> getAllUser();

    boolean emailConfirmation(String email, int code) throws UserNotFoundException;

    boolean resetPasswordRequest(String email) throws UserNotFoundException;

    void resetPassword(String token, String password) throws UserNotFoundException, JWTException;
}
