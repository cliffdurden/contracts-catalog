package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleReport;
import io.cliffdurden.contracts.catalog.repository.SaleContractsRepository;
import io.cliffdurden.contracts.catalog.repository.SaleReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
public class SaleReportServiceImpl implements SaleReportService {

    @Autowired
    private SaleReportsRepository saleReportsRepository;
    @Autowired
    private SaleContractsRepository saleContractsRepository;

    @Override
    public SaleReport createSaleReport(String number, String author, LocalDateTime creationDate, String saleContractNumber) {
        SaleContract saleContract = saleContractsRepository.findByNumber(saleContractNumber);
        if (saleContract == null) {
            throw new NoSuchElementException(format("Can not find Sale Contract %s", saleContractNumber));
        }
        SaleReport saleReport = new SaleReport();
        saleReport.setNumber(number);
        saleReport.setAuthor(author);
        saleReport.setCreationDate(creationDate);
        saleReport.setSaleContract(saleContract);
        return saleReportsRepository.save(saleReport);
    }

    @Override
    public Iterable<SaleReport> findAll() {
        return saleReportsRepository.findAll();
    }
}
