package com.crumb.bakery.service;

import com.crumb.bakery.model.Order;
import com.crumb.bakery.model.OrderItem;
import com.crumb.bakery.model.Product;
import com.crumb.bakery.repository.OrderRepository;
import com.crumb.bakery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BakeryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order processNewOrder(Order order) {
        // If the frontend does not send a full items structure, preserve its current total and unit price.
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return orderRepository.save(order);
        }

        // Compute total price and update stock
        double totalOrderPrice = 0;

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));

            int quantity = item.getQuantity();

            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStock() + ", Requested: " + quantity);
            }

            // Deduct stock
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            // Set order back-reference on item and compute price
            item.setOrder(order);
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(product.getPrice() * quantity);

            totalOrderPrice += item.getTotalPrice();
        }

        order.setUnitPrice(0); // Optional: Not really needed if we use Items
        order.setTotalPrice(totalOrderPrice);

        return orderRepository.save(order);
    }
}
