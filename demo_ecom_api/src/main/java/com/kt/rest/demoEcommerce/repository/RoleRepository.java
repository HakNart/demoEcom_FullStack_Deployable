package com.kt.rest.demoEcommerce.repository;

import com.kt.rest.demoEcommerce.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
