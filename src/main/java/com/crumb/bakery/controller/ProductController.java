package com.crumb.bakery.controller;

import com.crumb.bakery.model.Product;
import com.crumb.bakery.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired private ProductService productService;

    /** Public: only available products */
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllAvailable());
    }

    /** Admin: all products including unavailable */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllAdmin() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return productService.getById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String q) {
        return ResponseEntity.ok(productService.search(q));
    }

    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@Valid @RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.ok(Map.of("success", true, "product", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.getById(id).map(existing -> {
            product.setId(id);
            product.setCreatedAt(existing.getCreatedAt());
            Product saved = productService.save(product);
            return ResponseEntity.ok((Object) Map.of("success", true, "product", saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Product deleted"));
    }
}
