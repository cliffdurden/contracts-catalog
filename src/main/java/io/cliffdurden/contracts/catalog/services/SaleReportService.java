package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleReport;

import java.time.LocalDateTime;

public interface SaleReportService {
    SaleReport createSaleReport(String number, String author, LocalDateTime creationDate, String saleContractNumber);

    Iterable<SaleReport> findAll();
}
