package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleContract;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SaleContractService {
    SaleContract createSaleContract(String number, String author, LocalDateTime creationDate, BigDecimal transactionAmount);

    Iterable<SaleContract> findAll();
}
