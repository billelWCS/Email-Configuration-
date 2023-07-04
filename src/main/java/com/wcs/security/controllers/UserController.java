package com.wcs.security.controllers;

import com.wcs.security.models.User;
import com.wcs.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("")
    List<User> getAllUser (){
        return userService.getAllUser();
    }

    @GetMapping("/{email}")
    User getUserByEmail (@PathVariable String email){
        return userService.getUserByEmail(email);
    }

}
