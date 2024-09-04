package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.Role;
import com.enigma.challenge.tokohapeya.helper.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
