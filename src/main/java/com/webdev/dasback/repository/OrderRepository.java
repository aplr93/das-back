package com.webdev.dasback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webdev.dasback.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByCustomerId(Long id);

}
