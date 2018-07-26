package io.cliffdurden.contracts.catalog.controller;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import io.cliffdurden.contracts.catalog.model.SaleReport;
import io.cliffdurden.contracts.catalog.services.SaleContractServiceImpl;
import io.cliffdurden.contracts.catalog.services.SaleDeclarationServiceImpl;
import io.cliffdurden.contracts.catalog.services.SaleReportServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static io.cliffdurden.contracts.catalog.util.TestUtils.*;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AllContractsController.class)
public class AllContractsControllerTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1";
    private static final String TEST_SALE_DECLARATION_NUMBER = "2";
    private static final String TEST_SALE_REPORT_NUMBER = "3";

    @Autowired
    private MockMvc mvc;

    @MockBean
    SaleContractServiceImpl saleContractService;

    @MockBean
    SaleDeclarationServiceImpl saleDeclarationService;

    @MockBean
    SaleReportServiceImpl saleReportService;

    @Test
    public void whenFindAllContracts_thenReturnJsonArray() throws Exception {
        SaleContract givenSaleContract = createDummySaleContract(TEST_SALE_CONTRACT_NUMBER);
        given(saleContractService.findAll())
                .willReturn(Arrays.asList(givenSaleContract));
        given(saleDeclarationService.findAll())
                .willReturn(Arrays.asList(createDummySaleDeclaration(TEST_SALE_DECLARATION_NUMBER)));
        given(saleReportService.findAll())
                .willReturn(Arrays.asList(createDummySaleReport(TEST_SALE_REPORT_NUMBER, givenSaleContract)));

        mvc.perform(get("/contracts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].number", is(TEST_SALE_CONTRACT_NUMBER)))
                .andExpect(jsonPath("$[1].number", is(TEST_SALE_DECLARATION_NUMBER)))
                .andExpect(jsonPath("$[2].number", is(TEST_SALE_REPORT_NUMBER)));
    }

    private SaleContract createDummySaleContract(String number) {
        return createSaleContract(number, "Contract Test Controller", now(), new BigDecimal(100));
    }

    private SaleDeclaration createDummySaleDeclaration(String number) {
        return createSaleDeclaration(number, "Declaration Test Controller", now(), now());
    }

    private SaleReport createDummySaleReport(String number, SaleContract saleContract) {
        return createSaleReport(number, "Report Test Controller", now(), saleContract);
    }
}