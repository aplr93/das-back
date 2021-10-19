package com.webdev.dasback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webdev.dasback.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    List<Customer> findByCpf(String cpf);
}
