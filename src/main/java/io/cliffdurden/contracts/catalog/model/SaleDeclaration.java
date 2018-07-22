package io.cliffdurden.contracts.catalog.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class SaleDeclaration extends Contract {

    @NotNull
    private LocalDateTime filingDate;

    public LocalDateTime getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDateTime filingDate) {
        this.filingDate = filingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleDeclaration)) return false;
        SaleDeclaration that = (SaleDeclaration) o;
        return Objects.equals(getNumber(), that.getNumber())
                && Objects.equals(getAuthor(), that.getAuthor())
                && Objects.equals(getCreationDate(), that.getCreationDate())
                && Objects.equals(filingDate, that.filingDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNumber(), getAuthor(), getCreationDate(), filingDate);
    }
}
