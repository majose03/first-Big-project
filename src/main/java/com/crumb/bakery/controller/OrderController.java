package com.crumb.bakery.controller;

import com.crumb.bakery.model.Order;
import com.crumb.bakery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> createOrder(@Valid @RequestBody Order order) {
        Order saved = orderService.createOrder(order);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "🎂 Order placed! We'll WhatsApp you within 30 minutes.",
            "orderId", saved.getId(),
            "order", saved
        ));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        return orderService.getOrderById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<Order>> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            Order updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(Map.of("success", true, "orderId", id, "newStatus", updated.getStatus()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Order deleted"));
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String,Object>> getSummary() {
        return ResponseEntity.ok(Map.of(
            "total", orderService.getTotalOrderCount(),
            "byStatus", orderService.getStatusSummary()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String,Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "CRUMB 2.0 — MongoDB",
            "totalOrders", orderService.getTotalOrderCount(),
            "timestamp", new Date()
        ));
    }
}
