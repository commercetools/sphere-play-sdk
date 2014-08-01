package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A relative product discount with the corresponding basis point value.
 */
public class RelativeProductDiscount extends ProductDiscountValue {
    private final int permyriad;

    public RelativeProductDiscount(@JsonProperty("permyriad") final int permyriad) {
        this.permyriad = permyriad;
    }

    /**
     * Per ten thousand. The fraction the price is reduced. 1000 will result in a 10% price reduction.
     * @return permyriad
     */
    public int getPermyriad() {
        return permyriad;
    }

    /**
     * Alias for {@link RelativeProductDiscount#getPermyriad()}
     * @return permyriad
     */
    public int getBasisPoint() {
        return getPermyriad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RelativeProductDiscount that = (RelativeProductDiscount) o;

        if (permyriad != that.permyriad) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + permyriad;
        return result;
    }

    @Override
    public String toString() {
        return "RelativeProductDiscount{" +
                "permyriad=" + permyriad +
                '}';
    }
}