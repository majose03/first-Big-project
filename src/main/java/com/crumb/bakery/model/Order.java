package com.crumb.bakery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 80)
    private String name;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits with no spaces")
    private String phone;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Item is required")
    private String item;

    @NotBlank(message = "Address is required")
    private String address;

    private String deliveryDate;

    @Min(value = 1)
    @Max(value = 50)
    private int quantity = 1;

    private double unitPrice;
    private double totalPrice;
    private String notes;
    private String optionalItems; // e.g. "extra cream, no onion"

    // pending | confirmed | preparing | out-for-delivery | delivered | cancelled
    private String status = "pending";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
