package com.webdev.dasback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webdev.dasback.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
