package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleReport;
import io.cliffdurden.contracts.catalog.repository.SaleContractsRepository;
import io.cliffdurden.contracts.catalog.repository.SaleReportsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.now;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class SaleReportServiceImplTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1337";
    private static final String TEST_SALE_CONTRACT_NUMBER2 = "31337";
    private static final String TEST_SALE_CONTRACT_AUTHOR = "Test Service";
    private static final LocalDateTime TEST_SALE_CONTRACT_CREATION_DATE = now();
    private static final BigDecimal TEST_SALE_CONTRACT_TRANSACTION_AMOUNT = new BigDecimal(100);

    private static final String TEST_SALE_REPORT_NUMBER = "42";
    private static final String TEST_SALE_REPORT_NUMBER2 = "43";
    private static final String TEST_SALE_REPORT_AUTHOR = "Test Service Report";
    private static final LocalDateTime TEST_SALE_REPORT_CREATION_DATE = now();

    private SaleReport testSaleReport;
    private SaleReport testSaleReport2;

    @Autowired
    private SaleReportService saleReportService;

    @MockBean
    private SaleReportsRepository saleReportsRepository;

    @MockBean
    private SaleContractsRepository saleContractsRepository;

    @Before
    public void setUp() {
        SaleContract testSaleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        SaleContract testSaleContract2 = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER2);
        testSaleReport = createDummySaleReport(TEST_SALE_REPORT_NUMBER, testSaleContract);
        testSaleReport2 = createDummySaleReport(TEST_SALE_REPORT_NUMBER2, testSaleContract2);

        when(saleContractsRepository.findByNumber(TEST_SALE_CONTRACT_NUMBER))
                .thenReturn(testSaleContract);
        when(saleContractsRepository.findByNumber(TEST_SALE_CONTRACT_NUMBER2))
                .thenReturn(testSaleContract2);

        when(saleReportsRepository.save(testSaleReport))
                .thenReturn(testSaleReport);
        when(saleReportsRepository.findAll())
                .thenReturn(Arrays.asList(testSaleReport, testSaleReport2));
    }

    @Test
    public void whenCreateSaleReport_thenReturnSavedEntity() {
        SaleReport saved = saleReportService.createSaleReport(
                TEST_SALE_REPORT_NUMBER,
                TEST_SALE_REPORT_AUTHOR,
                TEST_SALE_REPORT_CREATION_DATE,
                TEST_SALE_CONTRACT_NUMBER);

        assertThat(testSaleReport)
                .isEqualTo(saved);
    }

    @Test(expected = NoSuchElementException.class)
    public void whenCreateSaleReportWithNotExistingSaleContract_thenShouldThrowException() {
        saleReportService.createSaleReport(
                TEST_SALE_REPORT_NUMBER,
                TEST_SALE_REPORT_AUTHOR,
                TEST_SALE_REPORT_CREATION_DATE,
                "NOT EXISTING");
    }

    @Test
    public void whenFindAll_thenReturnSaleReports() {
        Iterable<SaleReport> findResult = saleReportService.findAll();

        assertThat(testSaleReport)
                .isIn(findResult);
        assertThat(testSaleReport2)
                .isIn(findResult);
        assertThat(stream(findResult.spliterator(), false).count())
                .isEqualTo(2);
    }

    @Test
    public void findAll() {
    }

    @TestConfiguration
    static class SaleReportServiceImplTestContextConfiguration {

        @Bean
        public SaleReportService saleContractService() {
            return new SaleReportServiceImpl();
        }
    }

    private SaleReport createDummySaleReport(String number, SaleContract saleContract) {
        SaleReport saleReport = new SaleReport();
        saleReport.setNumber(number);
        saleReport.setAuthor(TEST_SALE_REPORT_AUTHOR);
        saleReport.setCreationDate(TEST_SALE_REPORT_CREATION_DATE);
        saleReport.setSaleContract(saleContract);
        return saleReport;
    }

    private SaleContract createDummySaleContract(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor(TEST_SALE_CONTRACT_AUTHOR);
        saleContract.setCreationDate(TEST_SALE_CONTRACT_CREATION_DATE);
        saleContract.setTransactionAmount(TEST_SALE_CONTRACT_TRANSACTION_AMOUNT);
        return saleContract;
    }
}