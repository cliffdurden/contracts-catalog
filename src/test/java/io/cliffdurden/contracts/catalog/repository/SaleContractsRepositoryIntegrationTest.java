package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.Application;
import io.cliffdurden.contracts.catalog.model.SaleContract;
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
public class SaleContractsRepositoryIntegrationTest {

    private static final String TEST_SALE_CONTRACT_NUMBER = "1337";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSaleContractRestRepository() {
        URI saleContractUri = saveSaleContract(createSaleContract(
                TEST_SALE_CONTRACT_NUMBER, "I. Test", now(), new BigDecimal(100500)));
        ParameterizedTypeReference<Resource<SaleContract>> responseType
                = new ParameterizedTypeReference<Resource<SaleContract>>() {
        };

        // Просматривать объект контракта по идентификатору, со всеми полями.
        ResponseEntity<Resource<SaleContract>> getResult
                = restTemplate.exchange(saleContractUri.toString(), GET, null, responseType);

        assertThat(TEST_SALE_CONTRACT_NUMBER).isEqualTo(getResult.getBody().getContent().getNumber());
        assertThat(HttpStatus.OK.value()).isEqualTo(getResult.getStatusCode().value());

        // Удалять зарегистрированный объект контракта.
        restTemplate.delete(saleContractUri.toString());

        // Проверить результат удаления объекта контракта.
        ResponseEntity<Resource<SaleContract>> resultByNumber =
                restTemplate.exchange(
                        UriComponentsBuilder.fromPath("/salecontracts/search/findByNumber")
                                .queryParam("number", TEST_SALE_CONTRACT_NUMBER).build().toString(),
                        GET,
                        null,
                        responseType);

        assertThat(resultByNumber.getBody()).isEqualTo(null);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(resultByNumber.getStatusCode().value());
    }

    private URI saveSaleContract(SaleContract saleContract) {
        return restTemplate.postForLocation("/salecontracts", saleContract);
    }
}