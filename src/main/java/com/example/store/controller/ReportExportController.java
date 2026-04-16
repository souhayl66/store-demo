package com.example.store.controller;

import com.example.store.entity.Sale;
import com.example.store.repository.SaleRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ReportExportController {

    private final SaleRepository saleRepo;

    public ReportExportController(SaleRepository saleRepo) {
        this.saleRepo = saleRepo;
    }

    @GetMapping("/reports/export-sales")
    public ResponseEntity<byte[]> exportSalesExcel() {
        List<Sale> sales = saleRepo.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sales Report");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Product");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Total (MAD)");

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            int rowIdx = 1;
            for (Sale s : sales) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(s.getSaleDate() != null ? s.getSaleDate().format(fmt) : "");
                row.createCell(1).setCellValue(s.getProduct() != null ? s.getProduct().getName() : "Unknown");
                row.createCell(2).setCellValue(s.getQuantity() != null ? s.getQuantity() : 0);
                row.createCell(3).setCellValue(s.getTotalPrice() != null ? s.getTotalPrice() : 0.0);
            }

            for (int i = 0; i < 4; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sales_report.xlsx\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel", e);
        }
    }
}
