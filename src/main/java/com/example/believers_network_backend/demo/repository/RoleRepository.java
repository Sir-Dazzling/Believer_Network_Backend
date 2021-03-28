package com.example.believers_network_backend.demo.repository;

import com.example.believers_network_backend.demo.model.ERole;
import com.example.believers_network_backend.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(ERole name);
}