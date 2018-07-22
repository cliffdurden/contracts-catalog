package io.cliffdurden.contracts.catalog.repository;

import io.cliffdurden.contracts.catalog.model.SaleDeclaration;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "saledeclarations", path = "saledeclarations")
public interface SaleDeclarationsRepository extends PagingAndSortingRepository<SaleDeclaration, Long> {
    SaleDeclaration findByNumber(@Param("number") String number);
}
