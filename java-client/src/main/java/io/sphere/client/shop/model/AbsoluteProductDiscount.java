package io.sphere.client.shop.model;

import io.sphere.client.model.Money;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * An absolute product discount with corresponding values of price reductions.
 */
public class AbsoluteProductDiscount extends ProductDiscountValue {
    private final List<Money> money;

    public AbsoluteProductDiscount(@JsonProperty("money") final List<Money> money) {
        this.money = money;
    }

    /**
     * The array contains money values in different currencies. An absolute product discount will only match a price if this array contains a value with the same currency. If it contains 10€ and 15$, the matching € price will be decreased by 10€ and the matching $ price will be decreased by 15$.
     * @return list of discount values
     */
    public List<Money> getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbsoluteProductDiscount that = (AbsoluteProductDiscount) o;

        if (money != null ? !money.equals(that.money) : that.money != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (money != null ? money.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbsoluteProductDiscount{" +
                "money=" + money +
                '}';
    }
}
