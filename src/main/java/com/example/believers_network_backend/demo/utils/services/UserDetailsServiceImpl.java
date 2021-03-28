package com.example.believers_network_backend.demo.utils.services;

import com.example.believers_network_backend.demo.model.User;
import com.example.believers_network_backend.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(s, s)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User Not Found with username or email: " + s
                ));

        return UserDetailsImpl.build(user);
    }
}

