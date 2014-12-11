package io.sphere.client.shop.model;

import io.sphere.client.model.Money;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class DiscountedLineItemPrice {
    private final Money value;
    private final List<DiscountedLineItemPortion> includedDiscounts;

    @JsonCreator
    DiscountedLineItemPrice(@JsonProperty("value") Money value, @JsonProperty("includedDiscounts") List<DiscountedLineItemPortion> includedDiscounts) {
        this.value = value;
        this.includedDiscounts = includedDiscounts;
    }

    public static DiscountedLineItemPrice create(final Money value, final List<DiscountedLineItemPortion> includedDiscounts) {
        return new DiscountedLineItemPrice(value, includedDiscounts);
    }

    public Money getValue() {
        return value;
    }

    public List<DiscountedLineItemPortion> getIncludedDiscounts() {
        return includedDiscounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountedLineItemPrice that = (DiscountedLineItemPrice) o;

        if (includedDiscounts != null ? !includedDiscounts.equals(that.includedDiscounts) : that.includedDiscounts != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (includedDiscounts != null ? includedDiscounts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscountedLineItemPrice{" +
                "value=" + value +
                ", includedDiscounts=" + includedDiscounts +
                '}';
    }
}