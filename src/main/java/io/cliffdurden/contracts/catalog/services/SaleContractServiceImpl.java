package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.repository.SaleContractsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SaleContractServiceImpl implements SaleContractService {

    @Autowired
    private SaleContractsRepository saleContractsRepository;

    @Override
    public SaleContract createSaleContract(String number, String author, LocalDateTime creationDate, BigDecimal transactionAmount) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor(author);
        saleContract.setCreationDate(creationDate);
        saleContract.setTransactionAmount(transactionAmount);
        return saleContractsRepository.save(saleContract);
    }

    @Override
    public Iterable<SaleContract> findAll() {
        return saleContractsRepository.findAll();
    }
}
