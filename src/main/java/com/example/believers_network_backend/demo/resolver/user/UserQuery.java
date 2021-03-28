package com.example.believers_network_backend.demo.resolver.user;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.believers_network_backend.demo.model.User;
import com.example.believers_network_backend.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery implements GraphQLQueryResolver {
    private UserRepository userRepository;

    @Autowired
    public UserQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> findAllUsers(){
        return userRepository.findAll();
    }
}
