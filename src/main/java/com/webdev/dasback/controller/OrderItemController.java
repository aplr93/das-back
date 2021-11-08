package com.webdev.dasback.controller;

import com.webdev.dasback.model.OrderItem;
import com.webdev.dasback.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderItem")

public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping
    public List<OrderItem> getItems() {
        List<OrderItem> items = orderItemRepository.findAll();

        return items;
    }

}
