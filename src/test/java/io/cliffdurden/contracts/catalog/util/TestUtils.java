package io.cliffdurden.contracts.catalog.util;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import io.cliffdurden.contracts.catalog.model.SaleReport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtils {

    public static SaleContract createSaleContract(String number, String author, LocalDateTime creationDate,
                                                  BigDecimal transactionAmount) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor(author);
        saleContract.setCreationDate(creationDate);
        saleContract.setTransactionAmount(transactionAmount);
        return saleContract;
    }

    public static SaleDeclaration createSaleDeclaration(String number, String author, LocalDateTime creationDate,
                                                        LocalDateTime filingDate) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        saleDeclaration.setAuthor(author);
        saleDeclaration.setCreationDate(creationDate);
        saleDeclaration.setFilingDate(filingDate);
        return saleDeclaration;
    }

    public static SaleReport createSaleReport(String number, String author, LocalDateTime creationDate,
                                              SaleContract saleContract) {
        SaleReport saleReport = new SaleReport();
        saleReport.setNumber(number);
        saleReport.setAuthor(author);
        saleReport.setCreationDate(creationDate);
        saleReport.setSaleContract(saleContract);
        return saleReport;
    }
}
