package io.cliffdurden.contracts.catalog.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class SaleReport extends Contract {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "saleContract_id")
    private SaleContract saleContract;

    public SaleContract getSaleContract() {
        return saleContract;
    }

    public void setSaleContract(SaleContract saleContract) {
        this.saleContract = saleContract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleReport)) return false;
        SaleReport that = (SaleReport) o;
        return Objects.equals(getNumber(), that.getNumber())
                && Objects.equals(getAuthor(), that.getAuthor())
                && Objects.equals(getCreationDate(), that.getCreationDate())
                && Objects.equals(saleContract, that.saleContract);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNumber(), getAuthor(), getCreationDate(), saleContract);
    }
}
