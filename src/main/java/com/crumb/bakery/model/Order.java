package com.crumb.bakery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 80)
    private String name;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Item is required")
    private String item;

    @NotBlank(message = "Address is required")
    private String address;

    private String deliveryDate;

    @Min(value = 1) @Max(value = 50)
    private int quantity = 1;

    private double unitPrice;
    private double totalPrice;
    private String notes;
    private String optionalItems;  // e.g. "extra cream, no onion"

    // pending | confirmed | preparing | out-for-delivery | delivered | cancelled
    private String status = "pending";

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
