package io.sphere.client.shop.model;

import com.google.common.base.Optional;
import io.sphere.client.model.Money;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.List;

public class ProductDiscountValue {
    private final String type;
    private final Optional<Integer> permyriad;
    private final List<Money> money;

    @JsonCreator
    private ProductDiscountValue(@JsonProperty("type") String type, @JsonProperty("permyriad") Integer permyriad,
                                 @JsonProperty("money") List<Money> money) {
        this.type = type;
        this.permyriad = Optional.fromNullable(permyriad);
        this.money = money != null ? money : Collections.<Money>emptyList();
    }

    public boolean isRelative() {
        return "relative".equals(type);
    }

    public boolean isAbsolute() {
        return "absolute".equals(type);
    }

    public Optional<Integer> getPermyriad() {
        return permyriad;
    }

    public List<Money> getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDiscountValue that = (ProductDiscountValue) o;

        if (money != null ? !money.equals(that.money) : that.money != null) return false;
        if (permyriad != null ? !permyriad.equals(that.permyriad) : that.permyriad != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (permyriad != null ? permyriad.hashCode() : 0);
        result = 31 * result + (money != null ? money.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDiscountValue{" +
                "type='" + type + '\'' +
                ", permyriad=" + permyriad +
                ", money=" + money +
                '}';
    }
}
