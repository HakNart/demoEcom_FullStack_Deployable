package com.kt.rest.demoEcommerce.repository;

import com.kt.rest.demoEcommerce.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
        select u from User u where u.username = :username
    """)
    Optional<User> findOneByUsername(String username);

//    Optional<User> findByEmail(String email);
//    boolean existsByEmail(String email);

}
