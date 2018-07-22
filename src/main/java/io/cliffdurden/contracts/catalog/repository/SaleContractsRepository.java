package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleContract;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "salecontracts", path = "salecontracts")
public interface SaleContractsRepository extends PagingAndSortingRepository<SaleContract, Long> {
    SaleContract findByNumber(@Param("number") String number);
}
