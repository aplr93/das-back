package com.webdev.dasback.service;

import com.webdev.dasback.model.Product;
import com.webdev.dasback.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService  {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> get(String description) {
        if (description == null) {
            return productRepository.findAll();
        }
        else {
            return productRepository.findByDescription(description);
        }
    }

    public Product getById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isPresent()) {
            return optionalProduct.get();
        }

        return null;
    }

    @Transactional
    public Product save(Product product) {
        Product newProduct = productRepository.save(product);

        return newProduct;
    }

    @Transactional
    public Product update(Product product, long id) {
        Optional<Product> optionalProduct = Optional.of(productRepository.getById(id));

        if (optionalProduct.isPresent()) {
            Product newProduct = optionalProduct.get();
            newProduct.setDescription(product.getDescription());
            productRepository.save(newProduct);
            return newProduct;
        }

        return null;
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Product> optionalProduct = Optional.of(productRepository.getById(id));

        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean deleteAll() {
        try{
            productRepository.deleteAll();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
