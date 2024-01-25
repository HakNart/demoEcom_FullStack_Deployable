package com.kt.rest.demoEcommerce.repository;

import com.kt.rest.demoEcommerce.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select o from Order o join o.user u where u.id = :userId")
    List<Order> findAllByUserId(Integer userId);
}
