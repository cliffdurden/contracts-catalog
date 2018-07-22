package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleReport;
import io.cliffdurden.contracts.catalog.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SaleReportsRepositoryTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1337";
    private static final String TEST_SALE_REPORT_NUMBER = "31337";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SaleReportsRepository saleReportsRepository;

    @Test
    public void whenFindByName_thenReturnSaleReport() {
        SaleContract saleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        SaleReport givenSaleReport = createDummySaleReport(TEST_SALE_REPORT_NUMBER, saleContract);
        entityManager.persist(saleContract);
        entityManager.persist(givenSaleReport);
        entityManager.flush();

        SaleReport found = saleReportsRepository
                .findByNumber(TEST_SALE_REPORT_NUMBER);

        assertThat(givenSaleReport)
                .isEqualTo(found);
    }

    @Test(expected = IllegalStateException.class)
    public void whenSaveReportWithNonUniqueNumber_thenShouldThrowException() {
        SaleContract saleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        SaleReport saleReport = createDummySaleReport(TEST_SALE_REPORT_NUMBER, saleContract);
        entityManager.persist(saleReport);

        entityManager.persist(saleReport);
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveReportWithoutData_thenShouldThrowException() {
        entityManager.persist(createDummySaleReporWithoutData(TEST_SALE_REPORT_NUMBER));
        entityManager.flush();
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveReportWithoutSaleContract_thenShouldThrowException() {
        entityManager.persist(createDummySaleReport(TEST_SALE_REPORT_NUMBER, null));
        entityManager.flush();
    }

    private SaleReport createDummySaleReport(String number, SaleContract saleContract) {
        SaleReport saleReport = new SaleReport();
        saleReport.setNumber(number);
        saleReport.setAuthor("Test");
        saleReport.setCreationDate(now());
        saleReport.setSaleContract(saleContract);
        return saleReport;
    }

    private SaleReport createDummySaleReporWithoutData(String number) {
        SaleReport saleReport = new SaleReport();
        saleReport.setNumber(number);
        return saleReport;
    }

    private SaleContract createDummySaleContract(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor("Test SaleContract");
        saleContract.setCreationDate(now());
        saleContract.setTransactionAmount(new BigDecimal(100));
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
        public SaleReportService saleReportService() {
            return new SaleReportServiceImpl();
        }
    }
}