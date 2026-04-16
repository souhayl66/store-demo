package com.example.store.service;

import com.example.store.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<Object[]> getBestSellers() {
        return saleRepository.findBestSellers();
    }
}
