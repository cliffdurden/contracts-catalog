package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import io.cliffdurden.contracts.catalog.repository.SaleDeclarationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaleDeclarationServiceImpl implements SaleDeclarationService {

    @Autowired
    private SaleDeclarationsRepository saleDeclarationsRepository;

    @Override
    public SaleDeclaration createSaleDeclaration(String number, String author, LocalDateTime creationDate, LocalDateTime filingDate) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        saleDeclaration.setAuthor(author);
        saleDeclaration.setCreationDate(creationDate);
        saleDeclaration.setFilingDate(filingDate);
        return saleDeclarationsRepository.save(saleDeclaration);
    }

    @Override
    public Iterable<SaleDeclaration> findAll() {
        return saleDeclarationsRepository.findAll();
    }
}
