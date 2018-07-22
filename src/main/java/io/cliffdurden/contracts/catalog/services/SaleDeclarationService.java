package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleDeclaration;

import java.time.LocalDateTime;

public interface SaleDeclarationService {
    SaleDeclaration createSaleDeclaration(String number, String author, LocalDateTime creationDate, LocalDateTime filingDate);

    Iterable<SaleDeclaration> findAll();
}
