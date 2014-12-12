package io.sphere.client.shop.model;

import com.google.common.base.Optional;
import io.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DiscountCodeWithState {
    private final Optional<DiscountCodeState> state;
    private final Reference<DiscountCode> discountCode;

    @JsonCreator
    DiscountCodeWithState(@JsonProperty("state") DiscountCodeState state,
                          @JsonProperty("discountCode") Reference<DiscountCode> discountCode) {
        this(Optional.fromNullable(state), discountCode);
    }

    @JsonIgnore
    public DiscountCodeWithState(final Optional<DiscountCodeState> state, final Reference<DiscountCode> discountCode) {
        this.state = state;
        this.discountCode = discountCode;
    }

    public Optional<DiscountCodeState> getState() {
        return state;
    }

    public Reference<DiscountCode> getDiscountCode() {
        return discountCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountCodeWithState that = (DiscountCodeWithState) o;

        if (discountCode != null ? !discountCode.equals(that.discountCode) : that.discountCode != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (discountCode != null ? discountCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscountCodeWithState{" +
                "state=" + state +
                ", reference=" + discountCode +
                '}';
    }
}