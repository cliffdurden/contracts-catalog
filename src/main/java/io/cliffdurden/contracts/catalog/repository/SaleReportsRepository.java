package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleReport;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "salereports", path = "salereports")
public interface SaleReportsRepository extends PagingAndSortingRepository<SaleReport, Long> {
    SaleReport findByNumber(@Param("number") String number);
}
