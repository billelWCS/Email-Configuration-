package com.wcs.security.serviceImplem;

import com.wcs.security.enums.RoleName;
import com.wcs.security.models.Role;
import com.wcs.security.models.User;
import com.wcs.security.repositories.RoleRepository;
import com.wcs.security.repositories.UserRepository;
import com.wcs.security.services.EmailService;
import com.wcs.security.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserImplem implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User getUserByEmail(String email) {
        return  userRepository.findByEmail(email).orElse(null);
    }


    @Override
    public User createUser(User user) {
        String password = user.getPassword();
        String passwordEncoded = this.passwordEncoder.encode(password);
        user.setPassword(passwordEncoded);
        emailService.sendEmail(
                user.getEmail(),
                "Test",
                "Test"
        );
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(String email, RoleName roleName)  {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Role> role = roleRepository.findByName(roleName);
        if (user.isPresent() && role.isPresent()){
            user.get().getRoles().add(role.get());
        }
    }

    @Override
    public String login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    password
                            )
                    );
            return jwtService.generateToken(user.get());
        }else {
            return null;
        }

    }

    @Override
    public List<User> getAllUser (){
        return userRepository.findAll();
    }
}
