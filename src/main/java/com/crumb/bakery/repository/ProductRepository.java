package com.crumb.bakery.repository;

import com.crumb.bakery.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryIgnoreCaseAndAvailableTrue(String category);
    List<Product> findByAvailableTrueOrderBySortOrderAsc();
    List<Product> findByAvailableFalseOrderBySortOrderAsc();
    List<Product> findAllByOrderBySortOrderAsc();
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String name);
}
