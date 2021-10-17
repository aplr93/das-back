package com.webdev.dasback.controller;

import com.webdev.dasback.model.Customer;
import com.webdev.dasback.model.Product;
import com.webdev.dasback.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getProducts(String description) {
        List<Product> product;
        if (description == null) {
            product = productRepository.findAll();
        }
        else {
            System.out.println(description);
            product = productRepository.findByDescriptionEquals(description);
        }
        return product;

    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product, UriComponentsBuilder uriBuilder) {
        productRepository.save(product);

        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

}
