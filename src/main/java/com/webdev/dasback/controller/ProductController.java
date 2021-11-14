package com.webdev.dasback.controller;

import com.webdev.dasback.model.Product;
import com.webdev.dasback.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
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
    public List<Product> getProduct(String description) {
        List<Product> product;
        if (description == null) {
            product = productRepository.findAll();
        }
        else {
            product = productRepository.findByDescription(description);
        }
        return product;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		if(productOptional.isPresent()) {
			return ResponseEntity.ok(productOptional.get()) ;
		}
		return ResponseEntity.notFound().build();
	}

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product, UriComponentsBuilder uriBuilder) {
        productRepository.save(product);

        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        Optional<Product> productData = Optional.of(productRepository.getById(id));

        if (productData.isPresent()) {
            Product newProduct = productData.get();
            newProduct.setDescription(product.getDescription());
            return new ResponseEntity<>(productRepository.save(newProduct), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity removeProduct(@PathVariable Long id) {
        Optional<Product> productData = Optional.of(productRepository.getById(id));

        if (productData.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity removeAllProducts() {
        try{
            productRepository.deleteAll();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }

}
