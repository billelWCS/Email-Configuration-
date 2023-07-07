package com.wcs.security.controllers;

import com.wcs.security.DTOs.UserDTO;
import com.wcs.security.Exceptions.JWTException;
import com.wcs.security.Exceptions.UserNotFoundException;
import com.wcs.security.enums.RoleName;
import com.wcs.security.models.User;
import com.wcs.security.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class authController {

    @Autowired
    UserService userService;

    @PostMapping("/sign-up-user")
    ResponseEntity<?> createUser(@RequestBody User user){
        User result = userService.createUser(user);
        result = userService.addRoleToUser(result.getEmail(), RoleName.USER);
        if(result != null){
            List<String> roles = new ArrayList<>();
            result.getRoles().forEach(
                    role -> roles.add(role.getName().name())
            );

            return new ResponseEntity<>(
                   RegisterResponse.builder()
                           .email(result.getEmail())
                           .build(),
                    HttpStatus.CREATED
            );
        }else{
            return new ResponseEntity<>(
                    "Une erreur est survenu",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/sign-up-admin")
    void createAdmin(@RequestBody User user){
        User result = userService.createUser(user);
        userService.addRoleToUser(result.getEmail(), RoleName.ADMIN);
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody Map<String, String> form){
        String response = userService.login(form.get("email"), form.get("password"));
        if (response == null ){
            return new ResponseEntity<String>(
                    "Error",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return new ResponseEntity<>(
                LoginResponse.builder().jwt(response).build(),
                HttpStatus.OK
        );
    }

    @PostMapping("email-confirmation/{email}")
    ResponseEntity<?> emailConfirmation(@PathVariable String email, @RequestBody Map<String, Integer> request){
        Map<String, Object> body = new HashMap<>();
        try {
            if(userService.emailConfirmation(email, request.get("code"))){
                body.put("message", "Code correct");
                return new ResponseEntity<>(
                        body,
                        HttpStatus.OK
                );
            }else {
                body.put("message", "Code incorrect");
                return new ResponseEntity<>(
                        body,
                        HttpStatus.UNAUTHORIZED
                );
            }
        }catch (UserNotFoundException e){
            body.put("message", "User not found");
            return new ResponseEntity<>(
                    body,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("reset-password-request")
    ResponseEntity<?> resetPasswordRequest (@RequestBody Map<String, String> request){
        Map<String, Object> body = new HashMap<>();
        try {
            if (userService.resetPasswordRequest(request.get("email"))){
                body.put("message", "un email de réinitailisation vous a été envoyé");
                return new ResponseEntity<>(
                        body,
                        HttpStatus.OK
                );
            }else {
                body.put("message", "Une erreur est survenu, veuillez ressayé plus tard !");
                return new ResponseEntity<>(
                        body,
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }catch (UserNotFoundException e){
            body.put("message", "User Not found");
            return new ResponseEntity<>(
                    body,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("reset-password")
    ResponseEntity<?> resetPassword (@RequestBody Map<String , String> request) {
        Map<String, Object> body = new HashMap<>();
        try{
            userService.resetPassword(request.get("token"), request.get("password"));
            body.put("message", "Le mot de passe a été modifié");
            return new ResponseEntity<>(
                    body,
                    HttpStatus.OK
            );
        }catch (UserNotFoundException e){
            body.put("message", "User not found" );
            return new ResponseEntity<>(
                    body,
                    HttpStatus.NOT_FOUND
            );
        }catch (JWTException e){
            body.put("message", "Le lien n'est pas valide !");
            return new ResponseEntity<>(
                    body,
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @Data
    public class Form {
        String email;
        String password;
    }

}


