package io.cliffdurden.contracts.catalog.controller;

import io.cliffdurden.contracts.catalog.model.Contract;
import io.cliffdurden.contracts.catalog.services.SaleContractServiceImpl;
import io.cliffdurden.contracts.catalog.services.SaleDeclarationServiceImpl;
import io.cliffdurden.contracts.catalog.services.SaleReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class AllContractsController {

    @Autowired
    SaleContractServiceImpl saleContractService;
    @Autowired
    SaleDeclarationServiceImpl saleDeclarationService;
    @Autowired
    SaleReportServiceImpl saleReportService;

    @GetMapping("contracts")
    public ResponseEntity<List<Contract>> findAllContracts() {
        List<Contract> result = new LinkedList<>();
        saleContractService.findAll().forEach(result::add);
        saleDeclarationService.findAll().forEach(result::add);
        saleReportService.findAll().forEach(result::add);
        return ResponseEntity.ok(result);
    }
}