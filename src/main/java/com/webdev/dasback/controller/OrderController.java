package com.webdev.dasback.controller;

import com.webdev.dasback.model.Order;
import com.webdev.dasback.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")

public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getOrders() {
        return orderService.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrdersById(@PathVariable Long id) {
        Order order = orderService.getById(id);

        if (order != null) {
            return ResponseEntity.ok(order) ;
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order, UriComponentsBuilder uriBuilder) {
        orderService.save(order);

        URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody @Valid Order order) {
        Order changedOrder = orderService.update(id, order);

        if (changedOrder != null) {
            return new ResponseEntity<>(changedOrder, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeOrder(@PathVariable Long id) {
        boolean isSucess = orderService.delete(id);

        if (isSucess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
