package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import io.cliffdurden.contracts.catalog.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaleDeclarationsRepositoryTest {

    private static final String TEST_SALE_DECLARATION_NUMBER = "1337";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SaleDeclarationsRepository saleDeclarationsRepository;

    @Test
    public void whenFindByName_thenReturnSaleDeclaration() {
        SaleDeclaration givenSaleDeclaration = createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER);
        entityManager.persist(givenSaleDeclaration);
        entityManager.flush();

        SaleDeclaration found = saleDeclarationsRepository
                .findByNumber(TEST_SALE_DECLARATION_NUMBER);

        assertThat(givenSaleDeclaration)
                .isEqualTo(found);
    }

    @Test(expected = PersistenceException.class)
    public void whenSaveContractsWithNonUniqueNumber_thenShouldThrowException() {
        entityManager.persist(createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER));
        entityManager.flush();

        entityManager.persist(createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER));
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveContractsWithoutData_thenShouldThrowException() {
        entityManager.persist(createDummySaleDeclarationWithoutData(TEST_SALE_DECLARATION_NUMBER));
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveContractsWithoutTransactionAmount_thenShouldThrowException() {
        entityManager.persist(createDummySaleDeclarationWithoutFilingDate(TEST_SALE_DECLARATION_NUMBER));
        entityManager.flush();
    }

    private SaleDeclaration createDummySaleDeclaration(String number) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        saleDeclaration.setAuthor("Test");
        saleDeclaration.setCreationDate(now());
        saleDeclaration.setFilingDate(now());
        return saleDeclaration;
    }

    private SaleDeclaration createDummySaleDeclarationWithoutData(String number) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        return saleDeclaration;
    }

    private SaleDeclaration createDummySaleDeclarationWithoutFilingDate(String number) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        saleDeclaration.setAuthor("Test");
        saleDeclaration.setCreationDate(now());
        return saleDeclaration;
    }

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public SaleContractService saleContractService() {
            return new SaleContractServiceImpl();
        }

        @Bean
        public SaleDeclarationService saleDeclarationService() {
            return new SaleDeclarationServiceImpl();
        }

        @Bean
        public SaleReportService saleReportService() {
            return new SaleReportServiceImpl();
        }
    }
}