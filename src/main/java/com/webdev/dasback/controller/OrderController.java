package com.webdev.dasback.controller;

import com.webdev.dasback.model.Order;
import com.webdev.dasback.model.OrderItem;
import com.webdev.dasback.model.Product;
import com.webdev.dasback.repository.OrderItemRepository;
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

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping
    public List<Order> getOrders() {
        List<Order> order = orderRepository.findAll();

        return order;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrdersById(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()) {
            return ResponseEntity.ok(orderOptional.get()) ;
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order, UriComponentsBuilder uriBuilder) {
        orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody @Valid Order order) {
        Optional<Order> orderData = Optional.of(orderRepository.getById(id));

        if (orderData.isPresent()) {

            orderItemRepository.deleteAll(orderData.get().getItems());
            orderRepository.save(orderData.get());

            Order newOrder = orderRepository.getById(id);
            newOrder.setDate(order.getDate());

            for (OrderItem item : order.getItems()) {
                item.setOrder(newOrder);
                orderItemRepository.save(item);
            }
            orderRepository.save(newOrder);

            return new ResponseEntity<>(order, HttpStatus.OK);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity removeOrder(@PathVariable Long id) {
        Optional<Order> orderData = Optional.of(orderRepository.getById(id));

        if (orderData.isPresent()) {

            for (OrderItem item : orderData.get().getItems()) {
                item.setOrder(orderData.get());
                orderItemRepository.delete(item);
            }
            orderRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
