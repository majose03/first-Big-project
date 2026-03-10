package com.crumb.bakery.config;

import com.crumb.bakery.model.Product;
import com.crumb.bakery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            System.out.println("🌱 Seeding CRUMB 2.0 menu into Local Database...");
            productRepository.saveAll(List.of(
                new Product("Dark Chocolate Fudge Brownie",
                    "Belgian 70% dark chocolate, real butter, flaky sea salt — dense, fudgy, obsessive.",
                    299.0, "brownie", "🍫", "Best Seller"),
                new Product("Salted Caramel Brownie",
                    "Swirls of house-made salted caramel through our signature fudge base.",
                    329.0, "brownie", "🍮", "Fan Favourite"),
                new Product("Nutella Stuffed Brownie",
                    "Double chocolate brownie with a hidden Nutella heart in every bite.",
                    349.0, "brownie", "🫐", "New"),
                new Product("Espresso Walnut Brownie",
                    "Bold espresso brownie studded with crunchy walnuts. Coffee lover's dream.",
                    319.0, "brownie", "☕", null),
                new Product("Vegan Date-Cocoa Brownie",
                    "100% plant-based: Medjool dates, raw cocoa, coconut oil. Zero compromise.",
                    359.0, "brownie", "🌿", "Vegan"),
                new Product("Triple Chocolate Layer Cake",
                    "Dark, milk & white chocolate layers with Belgian ganache frosting.",
                    899.0, "cake", "🎂", "Signature"),
                new Product("Burnt Basque Cheesecake",
                    "San Sebastián-style, deliberately charred top, molten heart. No crust.",
                    750.0, "cake", "🍰", "Artisan"),
                new Product("Mango Cardamom Cake",
                    "Alphonso mango mousse, cardamom sponge, edible gold — summer in every slice.",
                    699.0, "cake", "🥭", "Seasonal"),
                new Product("Red Velvet Cream Cake",
                    "Classic crimson sponge, silky cream cheese frosting.",
                    749.0, "cake", "❤️", null),
                new Product("Chocolate Lava Cupcake",
                    "Warm cupcake with a molten dark chocolate centre. Serve warm.",
                    149.0, "cupcake", "🧁", "Hot New"),
                new Product("Vanilla Cloud Cupcake",
                    "Light vanilla sponge piled with whipped Swiss meringue buttercream.",
                    129.0, "cupcake", "☁️", null),
                new Product("Custom Celebration Cake",
                    "Designed to your vision — flavour, size, décor. 48-hour advance order.",
                    1499.0, "special", "✨", "Custom"),
                new Product("Brownie Gift Box (12 pcs)",
                    "6 dark fudge + 3 salted caramel + 3 Nutella stuffed. Gift-ready.",
                    999.0, "special", "🎁", "Gift")
            ));
            System.out.println("✅ Seeded " + productRepository.count() + " products!");
        } else {
            System.out.println("📦 Menu already loaded — " + productRepository.count() + " products in Local Database.");
        }
    }
}
