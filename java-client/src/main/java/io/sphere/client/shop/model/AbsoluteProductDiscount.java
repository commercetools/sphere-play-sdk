package io.sphere.client.shop.model;

import io.sphere.client.model.Money;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class AbsoluteProductDiscount extends AbsoluteDiscount implements ProductDiscountValue {

    public AbsoluteProductDiscount(@JsonProperty("money") final List<Money> money) {
        super(money);
    }
}
