package com.example.store.controller;

import com.example.store.entity.Product;
import com.example.store.entity.Sale;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.SaleRepository;
import com.example.store.util.TrialChecker;   // ✅ make sure this import is here

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class SaleController {

    private static final Logger log = LoggerFactory.getLogger(SaleController.class);

    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;

    public SaleController(SaleRepository saleRepo, ProductRepository productRepo) {
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/sales")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "sales";
    }

    @GetMapping("/sales/add/{productId}")
    public String addSaleForm(@PathVariable Long productId, Model model) {
        // ✅ Trial check
        if (TrialChecker.isTrialExpired()) {
            model.addAttribute("error", "Trial expired. Contact Souhayl Aguedai for full access.");
            return "trial-expired";
        }

        Optional<Product> opt = productRepo.findById(productId);
        if (opt.isEmpty()) {
            return "redirect:/sales?error=productNotFound";
        }
        model.addAttribute("product", opt.get());
        return "add-sale";
    }

    @PostMapping("/sales/add/{productId}")
    @Transactional
    public String recordSale(@PathVariable Long productId,
                             @RequestParam int quantity,
                             Model model) {
        // ✅ Trial check
        if (TrialChecker.isTrialExpired()) {
            model.addAttribute("error", "Trial expired. Contact Souhayl Aguedai for full access.");
            return "trial-expired";
        }

        try {
            Optional<Product> opt = productRepo.findById(productId);
            if (opt.isEmpty()) {
                return "redirect:/sales?error=productNotFound";
            }

            Product product = opt.get();

            if (quantity <= 0) {
                return "redirect:/sales/add/" + productId + "?error=invalidQuantity";
            }

            Integer stock = product.getStock() == null ? 0 : product.getStock();
            if (quantity > stock) {
                return "redirect:/sales/add/" + productId + "?error=stock";
            }

            // Update stock
            product.setStock(stock - quantity);
            productRepo.save(product);

            // Create sale
            Sale sale = new Sale();
            sale.setProduct(product);
            sale.setQuantity(quantity);
            double price = product.getPrice() == null ? 0.0 : product.getPrice();
            sale.setTotalPrice(price * quantity);
            sale.setSaleDate(LocalDateTime.now());
            saleRepo.save(sale);

            return "redirect:/sales/history?success=true";
        } catch (Exception ex) {
            log.error("Failed to record sale for productId {} quantity {}: {}", productId, quantity, ex.getMessage(), ex);
            return "redirect:/sales/add/" + productId + "?error=server";
        }
    }

    @GetMapping("/sales/history")
    public String salesHistory(Model model) {
        model.addAttribute("sales", saleRepo.findAll());
        return "sales-history";
    }
}
