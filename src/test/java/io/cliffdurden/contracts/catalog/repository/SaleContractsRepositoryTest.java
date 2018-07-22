package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleContract;
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
import java.math.BigDecimal;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaleContractsRepositoryTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1337";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SaleContractsRepository saleContractsRepository;

    @Test
    public void whenFindByName_thenReturnSaleContract() {
        SaleContract givenSaleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        entityManager.persist(givenSaleContract);
        entityManager.flush();

        SaleContract found = saleContractsRepository
                .findByNumber(TEST_SALE_CONTRACT_NUMBER);

        assertThat(givenSaleContract)
                .isEqualTo(found);
    }

    @Test(expected = PersistenceException.class)
    public void whenSaveContractsWithNonUniqueNumber_thenShouldThrowException() {
        entityManager.persist(createDummySaleContract(TEST_SALE_CONTRACT_NUMBER));

        entityManager.persist(createDummySaleContract(TEST_SALE_CONTRACT_NUMBER));
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveContractsWithoutData_thenShouldThrowException() {
        entityManager.persist(createDummySaleContractWithoutData(TEST_SALE_CONTRACT_NUMBER));
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveContractsWithoutTransactionAmount_thenShouldThrowException() {
        entityManager.persist(createDummySaleContractWithoutTransactionAmount(TEST_SALE_CONTRACT_NUMBER));
        entityManager.flush();
    }

    private SaleContract createDummySaleContract(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor("Test");
        saleContract.setCreationDate(now());
        saleContract.setTransactionAmount(new BigDecimal(100));
        return saleContract;
    }

    private SaleContract createDummySaleContractWithoutData(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        return saleContract;
    }

    private SaleContract createDummySaleContractWithoutTransactionAmount(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor("Test");
        saleContract.setCreationDate(now());
        return saleContract;
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
        public SaleReportService saleReportService(){
            return new SaleReportServiceImpl();
        }
    }
}