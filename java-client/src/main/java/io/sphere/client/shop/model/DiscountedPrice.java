package io.sphere.client.shop.model;

import io.sphere.client.model.Money;
import io.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class DiscountedPrice {
    private final Money value;
    private final Reference<ProductDiscount> discount;

    @JsonCreator
    DiscountedPrice(@JsonProperty("value") Money value,
                    @JsonProperty("discount") Reference<ProductDiscount> discount) {
        this.value = value;
        this.discount = discount;
    }

    public Money getValue() {
        return value;
    }

    public Reference<ProductDiscount> getDiscount() {
        return discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountedPrice that = (DiscountedPrice) o;

        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscountedPrice{" +
                "value=" + value +
                ", discount=" + discount +
                '}';
    }
}
