package com.example.believers_network_backend.demo.resolver.user;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.believers_network_backend.demo.error.UserAlreadyExistsException;
import com.example.believers_network_backend.demo.model.ERole;
import com.example.believers_network_backend.demo.model.Role;
import com.example.believers_network_backend.demo.model.User;
import com.example.believers_network_backend.demo.payload.request.SignUpRequest;
import com.example.believers_network_backend.demo.repository.RoleRepository;
import com.example.believers_network_backend.demo.repository.UserRepository;
import com.example.believers_network_backend.demo.security.Unsecured;
import com.example.believers_network_backend.demo.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Component
@Validated
public class UserMutation implements GraphQLMutationResolver
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

//    User Creating account mutation
    @Unsecured
    public User registerUser(@Valid SignUpRequest signUpRequest)
    {
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
        {
            throw new UserAlreadyExistsException("An account already exists with this username, please try another one");
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            throw new UserAlreadyExistsException("An account already exists with this email, please try another one");
        }

//        Create user account
        User user = new User(
                signUpRequest.getFullname(),
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            System.out.println(userRole);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role){
                    case "super_admin":
                        Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error Role is not found."));
                        roles.add((superAdminRole));
                        break;
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "content_creator":
                        Role contentCreatorRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(contentCreatorRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setEmailVerified(false);
        user.setEnabled(false);
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getEmail(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateJwtToken(authentication);

        return user;
    }

//    @Unsecured
//    public User loginUser(String usernameOrEmail, String password){
//
//    };
}
