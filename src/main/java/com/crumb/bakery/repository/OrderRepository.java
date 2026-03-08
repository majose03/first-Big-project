package com.crumb.bakery.repository;

import com.crumb.bakery.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByEmail(String email);
    List<Order> findByStatus(String status);
    List<Order> findByOrderByCreatedAtDesc();
    long countByStatus(String status);
}
