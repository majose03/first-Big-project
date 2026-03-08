package com.crumb.bakery.service;

import com.crumb.bakery.model.Product;
import com.crumb.bakery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAllAvailable() {
        return repo.findByAvailableTrueOrderBySortOrderAsc();
    }

    public List<Product> getAll() {
        return repo.findAllByOrderBySortOrderAsc();
    }

    public List<Product> getByCategory(String category) {
        return repo.findByCategoryIgnoreCaseAndAvailableTrue(category);
    }

    public Optional<Product> getById(String id) {
        return repo.findById(id);
    }

    public List<Product> search(String name) {
        return repo.findByNameContainingIgnoreCaseAndAvailableTrue(name);
    }

    public Product save(Product product) {
        return repo.save(product);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public long getCount() { return repo.count(); }
}
