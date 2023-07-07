package com.wcs.security.serviceImplem;

import com.wcs.security.Exceptions.JWTException;
import com.wcs.security.Exceptions.UserNotFoundException;
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
import java.util.Random;
import java.util.jar.JarException;

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
        Random random = new Random();
        int code = random.nextInt(1000,10000);
        user.setVerificationEmailCode(
                code
        );

        emailService.sendEmail(
                user.getEmail(),
                "Verification de l'adresse email",
                "Voilà le code de vérification de votre email : " +
                        code
        );

        return userRepository.save(user);
    }

    @Override
    public User addRoleToUser(String email, RoleName roleName)  {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Role> role = roleRepository.findByName(roleName);
        if (user.isPresent() && role.isPresent()){
            user.get().getRoles().add(role.get());
            return user.get();
        }
        else return null;
    }

    @Override
    public String login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            if (user.get().isEmailVerified()){
                authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        password
                                )
                        );
                return jwtService.generateToken(user.get());
            }
            return "-1";
        }else {
            return null;
        }
    }

    @Override
    public List<User> getAllUser (){
        return userRepository.findAll();
    }

    @Override
    public boolean emailConfirmation(String email, int code) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            int codeUser = user.get().getVerificationEmailCode();
            System.out.println(code);
            System.out.println(codeUser);
            if(code == codeUser){
                user.get().setEmailVerified(true);
                return true;
            }
            else
                return false;
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean resetPasswordRequest(String email) throws UserNotFoundException {
       User user = userRepository.findByEmail(email).orElseThrow(
               () -> new UserNotFoundException()
       );
       String jwt = jwtService.generateToken(user);
       StringBuilder sb = new StringBuilder();
       sb.append("Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien suivant : \n");
       sb.append("http://localhost:4200/reset-password/"+jwt);
       emailService.sendEmail(
               email,
               "Réinitialisation du mot de passe",
               sb.toString()
       );
       return true;
    }

    @Override
    public void resetPassword(String token, String password) throws UserNotFoundException, JWTException {
        try {
            String userEmail = jwtService.extractUsername(token);
            User user = userRepository.findByEmail(userEmail).orElseThrow(
                    () -> new UserNotFoundException()
            );
            String passwordEncoded = this.passwordEncoder.encode(password);
            user.setPassword(passwordEncoded);
        }catch (Exception e){
            throw new JWTException();
        }

    }
}
