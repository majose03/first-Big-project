package com.crumb.bakery.service;

import com.crumb.bakery.model.Order;
import com.crumb.bakery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    private static final Set<String> VALID = Set.of(
        "pending","confirmed","preparing","out-for-delivery","delivered","cancelled"
    );

    public Order createOrder(Order order) {
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        if (order.getUnitPrice() > 0)
            order.setTotalPrice(order.getUnitPrice() * order.getQuantity());
        return repo.save(order);
    }

    public List<Order> getAllOrders() {
        return repo.findByOrderByCreatedAtDesc();
    }

    public Optional<Order> getOrderById(String id) {
        return repo.findById(id);
    }

    public List<Order> getOrdersByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<Order> getOrdersByStatus(String status) {
        return repo.findByStatus(status);
    }

    public Order updateStatus(String id, String status) {
        if (!VALID.contains(status.toLowerCase()))
            throw new IllegalArgumentException("Invalid status: " + status);
        Order o = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        o.setStatus(status.toLowerCase());
        o.setUpdatedAt(LocalDateTime.now());
        return repo.save(o);
    }

    public void deleteOrder(String id) {
        repo.deleteById(id);
    }

    public Map<String, Long> getStatusSummary() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (String s : VALID) m.put(s, repo.countByStatus(s));
        return m;
    }

    public long getTotalOrderCount() { return repo.count(); }
}
