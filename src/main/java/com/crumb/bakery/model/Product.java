package com.crumb.bakery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name required")
    private String name;

    @NotBlank(message = "Description required")
    private String description;

    @NotNull(message = "Price required")
    @DecimalMin(value = "0.01")
    private Double price;

    private String category;   // brownie | cake | cupcake | special
    private String imageEmoji;
    private String badge;
    private boolean available = true;
    private int sortOrder = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Product(String name, String description, double price,
                   String category, String imageEmoji, String badge) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageEmoji = imageEmoji;
        this.badge = badge;
        this.available = true;
        this.createdAt = LocalDateTime.now();
    }
}
