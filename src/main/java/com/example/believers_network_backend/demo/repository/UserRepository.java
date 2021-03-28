package com.example.believers_network_backend.demo.repository;

import com.example.believers_network_backend.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
