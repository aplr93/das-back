package com.webdev.dasback.repository;

import com.webdev.dasback.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
