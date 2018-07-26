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

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSaleReportsRestRepository() {
        SaleContract saleContract = createSaleContract(
                "42",
                "I. Test",
                now(),
                new BigDecimal(100500));

        // Добавить новый объект контракта в реестр контрактов, с заполнением всех полей.
        URI saleContractUri = restTemplate.postForLocation("/salecontracts", saleContract);

        SaleReportDto saleReportDto = new SaleReportDto(
                "43",
                "I.Rep Test",
                now(),
                saleContractUri.toString());

        // Добавить новый объект контракта в реестр контрактов, с заполнением всех полей.
        URI uri = restTemplate.postForLocation("/salereports", saleReportDto);

        ParameterizedTypeReference<Resource<SaleReport>> responseType
                = new ParameterizedTypeReference<Resource<SaleReport>>() {
        };

        // Просматривать объект контракта по идентификатору, со всеми полями.
        ResponseEntity<Resource<SaleReport>> getResult
                = restTemplate.exchange(uri.toString(), GET, null, responseType);

        assertThat(saleReportDto.getNumber()).isEqualTo(getResult.getBody().getContent().getNumber());
        assertThat(HttpStatus.OK.value()).isEqualTo(getResult.getStatusCode().value());

        // Удалять зарегистрированный объект контракта.
        restTemplate.delete(uri.toString());

        // Проверить результат удаления объекта контракта.
        ResponseEntity<Resource<SaleReport>> resultByNumber =
                restTemplate.exchange(
                        UriComponentsBuilder.fromPath("/salereports/search/findByNumber")
                                .queryParam("number", saleReportDto.getNumber()).build().toString(),
                        GET,
                        null,
                        responseType);

        assertThat(resultByNumber.getBody()).isEqualTo(null);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(resultByNumber.getStatusCode().value());

    }

    private SaleContract createSaleContract(
            String number,
            String author,
            LocalDateTime creationDate,
            BigDecimal transactionAmount) {
        SaleContract saleContract = new SaleContract();
        saleContract.setNumber(number);
        saleContract.setAuthor(author);
        saleContract.setCreationDate(creationDate);
        saleContract.setTransactionAmount(transactionAmount);
        return saleContract;
    }

    class SaleReportDto {

        private final String number;
        private final String author;
        private final LocalDateTime creationDate;
        private final String saleContract;

        public SaleReportDto(String number, String author, LocalDateTime creationDate, String saleContract) {
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