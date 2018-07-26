package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.Application;
import io.cliffdurden.contracts.catalog.model.SaleContract;
import io.cliffdurden.contracts.catalog.model.SaleReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static io.cliffdurden.contracts.catalog.util.TestUtils.createSaleContract;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@TestPropertySource(
        locations = "classpath:application-integrationtest.yml")
public class SaleReportsRepositoryIntegrationTest {

    private static final String TEST_SALE_REPORT_NUMBER = "43";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSaleReportsRestRepository() {
        String saleContractUri = saveSaleContract(createSaleContract(
                "42", "I. Test", now(), new BigDecimal(100500)));
        SaleReportDto saleReportDto = new SaleReportDto(
                TEST_SALE_REPORT_NUMBER, "I.Rep Test", now(), saleContractUri);

        URI saleReportUri = saveSaleReport(saleReportDto);

        ParameterizedTypeReference<Resource<SaleReport>> saleReportResource
                = new ParameterizedTypeReference<Resource<SaleReport>>() {
        };

        // Просматривать объект контракта по идентификатору, со всеми полями.
        ResponseEntity<Resource<SaleReport>> getResult
                = restTemplate.exchange(saleReportUri.toString(), GET, null, saleReportResource);

        assertThat(TEST_SALE_REPORT_NUMBER).isEqualTo(getResult.getBody().getContent().getNumber());
        assertThat(HttpStatus.OK.value()).isEqualTo(getResult.getStatusCode().value());

        // Удалять зарегистрированный объект контракта.
        restTemplate.delete(saleReportUri.toString());

        // Проверить результат удаления объекта контракта.
        ResponseEntity<Resource<SaleReport>> resultByNumber =
                restTemplate.exchange(
                        UriComponentsBuilder.fromPath("/salereports/search/findByNumber")
                                .queryParam("number", TEST_SALE_REPORT_NUMBER).build().toString(),
                        GET,
                        null,
                        saleReportResource);

        assertThat(resultByNumber.getBody()).isEqualTo(null);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(resultByNumber.getStatusCode().value());

    }

    private String saveSaleContract(SaleContract saleContract) {
        return restTemplate.postForLocation("/salecontracts", saleContract).toString();
    }

    private URI saveSaleReport(SaleReportDto saleReportDto) {
        return restTemplate.postForLocation("/salereports", saleReportDto);
    }

    class SaleReportDto {

        private final String number;
        private final String author;
        private final LocalDateTime creationDate;
        private final String saleContract;

        SaleReportDto(String number, String author, LocalDateTime creationDate, String saleContract) {
            this.number = number;
            this.author = author;
            this.creationDate = creationDate;
            this.saleContract = saleContract;
        }

        public String getNumber() {
            return number;
        }

        public String getAuthor() {
            return author;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public String getSaleContract() {
            return saleContract;
        }
    }
}