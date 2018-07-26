package io.cliffdurden.contracts.catalog.services;

import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import io.cliffdurden.contracts.catalog.repository.SaleDeclarationsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

import static io.cliffdurden.contracts.catalog.util.TestUtils.createSaleDeclaration;
import static java.time.LocalDateTime.now;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
public class SaleDeclarationServiceImplTest {

    private static final String TEST_SALE_DECLARATION_NUMBER = "1337";
    private static final String TEST_SALE_DECLARATION_NUMBER2 = "31337";
    private static final String TEST_SALE_DECLARATION_AUTHOR = "Test Service";
    private static final LocalDateTime TEST_SALE_DECLARATION_CREATION_DATE = now();
    private static final LocalDateTime TEST_SALE_DECLARATION_FILING_DATE = now();

    private SaleDeclaration testSaleDeclaration;
    private SaleDeclaration testSaleDeclaration2;

    @Autowired
    private SaleDeclarationService saleDeclarationService;

    @MockBean
    private SaleDeclarationsRepository saleDeclarationsRepository;

    @Before
    public void setUp() {
        testSaleDeclaration = createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER);
        testSaleDeclaration2 = createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER2);

        when(saleDeclarationsRepository.save(testSaleDeclaration))
                .thenReturn(testSaleDeclaration);
        when(saleDeclarationsRepository.findAll())
                .thenReturn(Arrays.asList(testSaleDeclaration, testSaleDeclaration2));
    }

    @Test
    public void whenCreateSaleDeclaration_thenReturnSavedEntity() {
        SaleDeclaration saved = saleDeclarationService.createSaleDeclaration(
                TEST_SALE_DECLARATION_NUMBER,
                TEST_SALE_DECLARATION_AUTHOR,
                TEST_SALE_DECLARATION_CREATION_DATE,
                TEST_SALE_DECLARATION_FILING_DATE);

        assertThat(testSaleDeclaration)
                .isEqualTo(saved);
    }

    @Test
    public void whenFindAll_thenReturnSaleDeclarations() {
        Iterable<SaleDeclaration> findResult = saleDeclarationService.findAll();

        assertThat(testSaleDeclaration)
                .isIn(findResult);
        assertThat(testSaleDeclaration2)
                .isIn(findResult);
        assertThat(stream(findResult.spliterator(), false).count())
                .isEqualTo(2);
    }

    private SaleDeclaration createDummySaleDeclaration(String number) {
        return createSaleDeclaration(number,
                TEST_SALE_DECLARATION_AUTHOR,
                TEST_SALE_DECLARATION_CREATION_DATE,
                TEST_SALE_DECLARATION_FILING_DATE);
    }

    @TestConfiguration
    static class SaleDeclarationServiceImplTestContextConfiguration {

        @Bean
        public SaleDeclarationService saleContractService() {
            return new SaleDeclarationServiceImpl();
        }
    }
}