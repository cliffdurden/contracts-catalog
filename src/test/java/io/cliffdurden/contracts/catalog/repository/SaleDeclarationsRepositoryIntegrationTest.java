package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.Application;
import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
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
public class SaleDeclarationsRepositoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSaleDeclarationsRestRepository() {
        SaleDeclaration saleDeclaration = createSaleDeclaration(
                "42",
                "I Test",
                now(),
                now());

        // Добавить новый объект контракта в реестр контрактов, с заполнением всех полей.
        URI uri = restTemplate.postForLocation("/saledeclarations", saleDeclaration);
        ParameterizedTypeReference<Resource<SaleDeclaration>> responseType
                = new ParameterizedTypeReference<Resource<SaleDeclaration>>() {
        };

        // Просматривать объект контракта по идентификатору, со всеми полями.
        ResponseEntity<Resource<SaleDeclaration>> getResult
                = restTemplate.exchange(uri.toString(), GET, null, responseType);

        assertThat(saleDeclaration.getNumber()).isEqualTo(getResult.getBody().getContent().getNumber());
        assertThat(HttpStatus.OK.value()).isEqualTo(getResult.getStatusCode().value());

        // Удалить зарегистрированный объект контракта.
        restTemplate.delete(uri.toString());


        // Проверить результат удаления объекта контракта.
        ResponseEntity<Resource<SaleDeclaration>> resultByNumber =
                restTemplate.exchange(
                        UriComponentsBuilder.fromPath("/salecontracts/search/findByNumber")
                                .queryParam("number", saleDeclaration.getNumber()).build().toString(),
                        GET,
                        null,
                        responseType);

        assertThat(resultByNumber.getBody()).isEqualTo(null);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(resultByNumber.getStatusCode().value());

    }

    private SaleDeclaration createSaleDeclaration(
            String number,
            String author,
            LocalDateTime creationDate,
            LocalDateTime filingDate) {
        SaleDeclaration saleDeclaration = new SaleDeclaration();
        saleDeclaration.setNumber(number);
        saleDeclaration.setAuthor(author);
        saleDeclaration.setCreationDate(creationDate);
        saleDeclaration.setFilingDate(filingDate);
        return saleDeclaration;

    }
}