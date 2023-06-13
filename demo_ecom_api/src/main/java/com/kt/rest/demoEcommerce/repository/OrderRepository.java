package com.kt.rest.demoEcommerce.repository;

import com.kt.rest.demoEcommerce.models.shopEntities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query("select o from Order o join o.user u where u.id = :userId")
    List<Order> findAllByUserId(Long userId);
}
