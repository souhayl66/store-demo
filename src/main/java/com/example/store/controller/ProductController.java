package com.example.store.controller;

import com.example.store.entity.Product;
import com.example.store.repository.ProductRepository;
import com.example.store.util.TrialChecker;   // ✅ import the trial checker

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products"; // products.html
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        // ✅ Trial check
        if (TrialChecker.isTrialExpired()) {
            model.addAttribute("error", "Trial expired. Contact Souhayl Aguedai for full access.");
            return "trial-expired";
        }
        return "add-product"; // add-product.html
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam double price,
                             @RequestParam int stock,
                             Model model) {
        // ✅ Trial check
        if (TrialChecker.isTrialExpired()) {
            model.addAttribute("error", "Trial expired. Contact Souhayl Aguedai for full access.");
            return "trial-expired";
        }

        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setStock(stock);
        productRepo.save(p);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).orElseThrow();
        model.addAttribute("product", product);
        return "edit-product"; // edit-product.html
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam double price,
                              @RequestParam int stock) {
        Product product = productRepo.findById(id).orElseThrow();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}
