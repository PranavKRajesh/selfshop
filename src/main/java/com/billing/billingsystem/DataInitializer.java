package com.billing.billingsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepo;

    public DataInitializer(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only insert if table is empty — won't duplicate on restart
        if (productRepo.count() == 0) {
            productRepo.save(makeProduct("Basmati Rice (1kg)",       85.00));
            productRepo.save(makeProduct("Toor Dal (500g)",          65.00));
            productRepo.save(makeProduct("Sunflower Oil (1L)",      140.00));
            productRepo.save(makeProduct("Wheat Flour (2kg)",        90.00));
            productRepo.save(makeProduct("Sugar (1kg)",              45.00));
            productRepo.save(makeProduct("Salt (1kg)",               20.00));
            productRepo.save(makeProduct("Milk (1L)",                60.00));
            productRepo.save(makeProduct("Butter (100g)",            55.00));
            productRepo.save(makeProduct("Eggs (12 pcs)",           780.00));  // per dozen
            productRepo.save(makeProduct("Bread (400g)",             40.00));
            productRepo.save(makeProduct("Tomato Ketchup (500g)",    95.00));
            productRepo.save(makeProduct("Biscuits - Parle G",       10.00));
            productRepo.save(makeProduct("Tea Powder (250g)",        75.00));
            productRepo.save(makeProduct("Coffee Powder (100g)",     80.00));
            productRepo.save(makeProduct("Soap (Dove 100g)",         45.00));
            productRepo.save(makeProduct("Shampoo (Head & Shoulders 180ml)", 185.00));
            productRepo.save(makeProduct("Toothpaste (Colgate 150g)", 65.00));
            productRepo.save(makeProduct("Washing Powder (500g)",    60.00));
            productRepo.save(makeProduct("Coconut Oil (500ml)",     120.00));
            productRepo.save(makeProduct("Green Chilli Sauce (200g)", 50.00));
        }
    }

    private Product makeProduct(String name, double price) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(100);
        return p;
    }
}