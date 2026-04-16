package com.example.store.controller;

import com.example.store.entity.Sale;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.SaleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ProductRepository productRepo;
    private final SaleRepository saleRepo;

    public DashboardController(ProductRepository productRepo, SaleRepository saleRepo) {
        this.productRepo = productRepo;
        this.saleRepo = saleRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalProducts = productRepo.count();
        double totalRevenue = saleRepo.findAll().stream()
                .mapToDouble(s -> s.getTotalPrice() != null ? s.getTotalPrice() : 0.0)
                .sum();
        long totalSales = saleRepo.findAll().stream()
                .mapToLong(s -> s.getQuantity() != null ? s.getQuantity() : 0)
                .sum();

        LocalDate today = LocalDate.now();
        List<Sale> daySales = saleRepo.findAll().stream()
                .filter(s -> s.getSaleDate() != null && s.getSaleDate().toLocalDate().equals(today))
                .toList();

        double todayRevenue = daySales.stream()
                .mapToDouble(s -> s.getTotalPrice() != null ? s.getTotalPrice() : 0.0)
                .sum();

        // Build summary rows: [productName, qty, revenue]
        List<Object[]> salesSummary = saleRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct() != null ? s.getProduct().getName() : "Unknown",
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> new Object[]{
                                        list.get(0).getProduct() != null ? list.get(0).getProduct().getName() : "Unknown",
                                        list.stream().mapToInt(Sale::getQuantity).sum(),
                                        list.stream().mapToDouble(s -> s.getTotalPrice() != null ? s.getTotalPrice() : 0.0).sum()
                                }
                        )
                ))
                .values().stream().toList();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("daySales", daySales);
        model.addAttribute("salesSummary", salesSummary);
        model.addAttribute("lowStock", productRepo.findByStockLessThan(5));

        return "dashboard";
    }

}
