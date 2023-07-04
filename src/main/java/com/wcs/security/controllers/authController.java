package com.wcs.security.controllers;

import com.wcs.security.enums.RoleName;
import com.wcs.security.models.User;
import com.wcs.security.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<String> login(@RequestBody Map<String, String> form){
        String response = userService.login(form.get("email"), form.get("password"));
        if (response == null) {
            return new ResponseEntity<>(
                    "Email ou le mot de passe sont incorrects",
                    HttpStatus.UNAUTHORIZED
            );
        }else
            if (response.equals("" +
                    "")){
                return new ResponseEntity<>(
                        "Vous n'avez pas encore vérifié votre email",
                        HttpStatus.UNAUTHORIZED
                );
            }else{
                return new ResponseEntity<>(
                        response,
                        HttpStatus.OK
                );
            }

    }

    @PostMapping("email-confirmation/{email}")
    boolean emailConfirmation(@PathVariable String email, @RequestBody Map<String, Integer> request){
        return userService.emailConfirmation(email, request.get("code"));
    }

}


