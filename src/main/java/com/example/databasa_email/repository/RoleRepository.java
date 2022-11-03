package com.example.databasa_email.repository;

import com.example.databasa_email.entity.Role;
import com.example.databasa_email.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
