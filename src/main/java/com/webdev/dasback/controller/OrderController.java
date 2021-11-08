package com.webdev.dasback.controller;

import com.webdev.dasback.model.Order;
import com.webdev.dasback.repository.OrderRepository;
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
@RequestMapping("/orders")

public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public List<Order> getOrders() {
        List<Order> order = orderRepository.findAll();

        return order;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order, UriComponentsBuilder uriBuilder) {
        orderRepository.save(order);

        System.out.println(order.getItems());

        URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody @Valid Order order) {
        Optional<Order> orderData = Optional.of(orderRepository.getById(id));

        if (orderData.isPresent()) {
            Order newOrder = orderData.get();
            newOrder.setDate(order.getDate());
            return new ResponseEntity<>(orderRepository.save(newOrder), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity removeOrder(@PathVariable Long id) {
        Optional<Order> orderData = Optional.of(orderRepository.getById(id));

        if (orderData.isPresent()) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
