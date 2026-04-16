package com.example.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportsController {

    @GetMapping("/reports")
    public String reportsPage(Model model) {
        return "reports"; // reports.html
    }

    @GetMapping("/reports/best-sellers")
    public String bestSellers(Model model) {
        // Add salesSummary attribute in service layer
        return "best-sellers"; // best-sellers.html
    }

    @GetMapping("/reports/daily")
    public String dailyRevenue(Model model) {
        // Add dailyRevenue attribute in service layer
        return "daily-revenue"; // daily-revenue.html
    }
}
