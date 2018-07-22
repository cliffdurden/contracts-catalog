package io.cliffdurden.contracts.catalog.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class SaleContract extends Contract {

    @NotNull
    private BigDecimal transactionAmount;

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleContract)) return false;
        SaleContract that = (SaleContract) o;
        return Objects.equals(getNumber(), that.getNumber())
                && Objects.equals(getAuthor(), that.getAuthor())
                && Objects.equals(getCreationDate(), that.getCreationDate())
                && Objects.equals(transactionAmount, that.transactionAmount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNumber(), getAuthor(), getCreationDate(), transactionAmount);
    }
}
