package com.example.believers_network_backend.demo.resolver.user;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.believers_network_backend.demo.model.User;
import com.example.believers_network_backend.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutation implements GraphQLMutationResolver
{
    private UserRepository userRepository;

    @Autowired
    public UserMutation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String fullName, String email, String username){
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);

        userRepository.save(user);

        return user;
    }
}
