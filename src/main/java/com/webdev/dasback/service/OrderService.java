package com.webdev.dasback.service;

import com.webdev.dasback.model.Order;
import com.webdev.dasback.model.OrderItem;
import com.webdev.dasback.repository.OrderItemRepository;
import com.webdev.dasback.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> get() {
        return orderRepository.findAll();
    }

    public Order getById(long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if(orderOptional.isPresent()) {
            return orderOptional.get();
        }
        return null;
    }

    @Transactional
    public void save(Order order) {
        orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }
    }

    @Transactional
    public Order update(Long id, Order order) {
        Optional<Order> optionalOrder = Optional.of(orderRepository.getById(id));

        if (optionalOrder.isPresent()) {

            orderItemRepository.deleteAll(optionalOrder.get().getItems());

            Order changedOrder = orderRepository.getById(id);
            changedOrder.setDate(order.getDate());

            for (OrderItem item : order.getItems()) {
                item.setOrder(changedOrder);
                orderItemRepository.save(item);
            }

            orderRepository.save(changedOrder);

            return changedOrder;
        }
        return null;
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Order> optionalOrder = Optional.of(orderRepository.getById(id));

        if (optionalOrder.isPresent()) {

            for (OrderItem item : optionalOrder.get().getItems()) {
                item.setOrder(optionalOrder.get());
                orderItemRepository.delete(item);
            }
            orderRepository.deleteById(id);

            return true;
        }

        return false;

    }

}
