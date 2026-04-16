package com.example.store.service;

import com.example.store.entity.Product;
import com.example.store.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepo;

    public DataLoader(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) {
        if (productRepo.count() == 0) {
            Product p1 = new Product();
            p1.setName("Coffee");
            p1.setPrice(20.0);
            p1.setStock(50);

            Product p2 = new Product();
            p2.setName("Tea");
            p2.setPrice(15.0);
            p2.setStock(30);

            Product p3 = new Product();
            p3.setName("pizza");
            p3.setPrice(30.0);
            p3.setStock(10);

            productRepo.save(p1);
            productRepo.save(p2);
            productRepo.save(p3);
        }
    }
}
