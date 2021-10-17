package com.webdev.dasback.repository;

import com.webdev.dasback.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDescriptionEquals(String description);
}
