package com.crumb.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BakeryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  🍫  CRUMB 2.0 — Artisan Bakery API LIVE!    ║");
        System.out.println("║  Frontend → http://localhost:8080/            ║");
        System.out.println("║  Orders   → http://localhost:8080/api/orders  ║");
        System.out.println("║  Products → http://localhost:8080/api/products║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}
