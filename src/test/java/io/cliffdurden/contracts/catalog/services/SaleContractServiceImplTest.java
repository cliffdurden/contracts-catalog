package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.repository.SaleContractsRepository;
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

import static java.time.LocalDateTime.now;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class SaleContractServiceImplTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1337";
    private static final String TEST_SALE_CONTRACT_NUMBER2 = "31337";
    private static final String TEST_SALE_CONTRACT_AUTHOR = "Test Service";
    private static final LocalDateTime TEST_SALE_CONTRACT_CREATION_DATE = now();
    private static final BigDecimal TEST_SALE_CONTRACT_TRANSACTION_AMOUNT = new BigDecimal(100);

    private SaleContract testSaleContract;
    private SaleContract testSaleContract2;

    @Autowired
    private SaleContractService saleContractService;

    @MockBean
    private SaleContractsRepository saleContractsRepository;

    @Before
    public void setUp() {
        testSaleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        testSaleContract2 = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER2);

        when(saleContractsRepository.save(testSaleContract))
                .thenReturn(testSaleContract);
        when(saleContractsRepository.findAll())
                .thenReturn(Arrays.asList(testSaleContract, testSaleContract2));
    }

    @Test
    public void whenCreateSaleContract_thenReturnSavedEntity() {
        SaleContract saved = saleContractService.createSaleContract(
                TEST_SALE_CONTRACT_NUMBER,
                TEST_SALE_CONTRACT_AUTHOR,
                TEST_SALE_CONTRACT_CREATION_DATE,
                TEST_SALE_CONTRACT_TRANSACTION_AMOUNT);

        assertThat(testSaleContract)
                .isEqualTo(saved);
    }

    @Test
    public void whenFindAll_thenReturnSaleContracts() {
        Iterable<SaleContract> findResult = saleContractService.findAll();

        assertThat(testSaleContract)
                .isIn(findResult);
        assertThat(testSaleContract2)
                .isIn(findResult);
        assertThat(stream(findResult.spliterator(), false).count())
                .isEqualTo(2);
    }

    private SaleContract createDummySaleContract(String number) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor(TEST_SALE_CONTRACT_AUTHOR);
        saleContract.setCreationDate(TEST_SALE_CONTRACT_CREATION_DATE);
        saleContract.setTransactionAmount(TEST_SALE_CONTRACT_TRANSACTION_AMOUNT);
        return saleContract;
    }

    @TestConfiguration
    static class SaleContractServiceImplTestContextConfiguration {

        @Bean
        public SaleContractService saleContractService() {
            return new SaleContractServiceImpl();
        }
    }
}