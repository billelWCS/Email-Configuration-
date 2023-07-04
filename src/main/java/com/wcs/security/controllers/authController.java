package com.wcs.security.controllers;

import com.wcs.security.enums.RoleName;
import com.wcs.security.models.User;
import com.wcs.security.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class authController {

    @Autowired
    UserService userService;

    @PostMapping("/sign-up-user")
    void createUser(@RequestBody User user){
        User result = userService.createUser(user);
        userService.addRoleToUser(result.getEmail(), RoleName.USER);
    }

    @PostMapping("/sign-up-admin")
    void createAdmin(@RequestBody User user){
        User result = userService.createUser(user);
        userService.addRoleToUser(result.getEmail(), RoleName.ADMIN);
    }

    @PostMapping("/login")
    String login(@RequestBody Map<String, String> form){
//        return form.get("email") + form.get("password");
        return userService.login(form.get("email"), form.get("password"));
    }

}


