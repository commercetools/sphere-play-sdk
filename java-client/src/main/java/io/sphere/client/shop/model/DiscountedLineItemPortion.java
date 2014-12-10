package io.sphere.client.shop.model;

import io.sphere.client.model.Money;
import io.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class DiscountedLineItemPortion {
    private final Reference<CartDiscount> discount;
    private final Money discountedAmount;

    @JsonCreator
    DiscountedLineItemPortion(@JsonProperty("discount") Reference<CartDiscount> discount, @JsonProperty("discountedAmount") Money discountedAmount) {
        this.discount = discount;
        this.discountedAmount = discountedAmount;
    }

    public static DiscountedLineItemPortion create(final Reference<CartDiscount> discount, final Money discountedAmount) {
        return new DiscountedLineItemPortion(discount, discountedAmount);
    }

    public Reference<CartDiscount> getDiscount() {
        return discount;
    }

    public Money getDiscountedAmount() {
        return discountedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountedLineItemPortion that = (DiscountedLineItemPortion) o;

        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        if (discountedAmount != null ? !discountedAmount.equals(that.discountedAmount) : that.discountedAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = discount != null ? discount.hashCode() : 0;
        result = 31 * result + (discountedAmount != null ? discountedAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscountedLineItemPortion{" +
                "discount=" + discount +
                ", discountedAmount=" + discountedAmount +
                '}';
    }
}
