package io.cliffdurden.contracts.catalog;

import io.cliffdurden.contracts.catalog.services.SaleContractService;
import io.cliffdurden.contracts.catalog.services.SaleDeclarationService;
import io.cliffdurden.contracts.catalog.services.SaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

import static java.time.LocalDateTime.now;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private SaleContractService saleContractService;

    @Autowired
    private SaleDeclarationService saleDeclarationService;

    @Autowired
    private SaleReportService saleReportService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) {
        // Create default Sale Contracts
        saleContractService
                .createSaleContract("1", "A. Einstein", now(), BigDecimal.valueOf(10000));
        saleContractService
                .createSaleContract("2", "B. Grin", now(), BigDecimal.valueOf(500));
        saleContractService
                .createSaleContract("3", "S. Hawking", now(), BigDecimal.valueOf(200000));
        saleContractService
                .createSaleContract("4", "N. Tesla", now(), BigDecimal.valueOf(30));

        // Create default Sale Declarations
        saleDeclarationService
                .createSaleDeclaration("21", "E. Gamma", now(), now());
        saleDeclarationService
                .createSaleDeclaration("22", "R. Helm", now(), now());
        saleDeclarationService
                .createSaleDeclaration("23", "R. Johnson", now(), now());
        saleDeclarationService
                .createSaleDeclaration("24", "J. Vlissides ", now(), now());

        // Create default Sale Reports
        saleReportService
                .createSaleReport("31", "J. Smith", now(), "1");
        saleReportService
                .createSaleReport("32", "J. Smith", now(), "2");
        saleReportService
                .createSaleReport("33", "J. Smith", now(), "3");
        saleReportService
                .createSaleReport("34", "J. Smith", now(), "4");
    }
}
