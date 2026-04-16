package com.example.store.service;

import com.example.store.entity.Product;
import com.example.store.entity.Sale;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.SaleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class SaleService {
    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;

    public SaleService(SaleRepository saleRepo, ProductRepository productRepo) {
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    public Sale recordSale(Long productId, int quantity) {
        Product product = productRepo.findById(productId).orElseThrow();
        double total = product.getPrice() * quantity;
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(quantity);
        sale.setTotalPrice(total);
        sale.setSaleDate(LocalDateTime.now());
        return saleRepo.save(sale);
    }
}
