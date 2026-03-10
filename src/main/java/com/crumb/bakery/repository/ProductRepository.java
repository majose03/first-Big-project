package com.crumb.bakery.repository;

import com.crumb.bakery.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIgnoreCaseAndAvailableTrue(String category);
    List<Product> findByAvailableTrueOrderBySortOrderAsc();
    List<Product> findByAvailableFalseOrderBySortOrderAsc();
    List<Product> findAllByOrderBySortOrderAsc();
    List<Product> findByNameContainingIgnoreCaseAndAvailableTrue(String name);
}
