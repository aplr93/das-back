package com.webdev.dasback.controller;

import com.webdev.dasback.model.Product;
import com.webdev.dasback.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProduct(String description) {
        List<Product> product = productService.get(description);

        return product;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
	}

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product, UriComponentsBuilder uriBuilder) {

        productService.save(product);

        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        Product newProduct = productService.update(product, id);

        if (newProduct != null) {
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeProduct(@PathVariable Long id) {
        boolean isSuccess = productService.delete(id);

        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity removeAllProducts() {
        boolean isSuccess = productService.deleteAll();
        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }

}
