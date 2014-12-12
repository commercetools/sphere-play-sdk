package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class RelativeProductDiscount extends RelativeDiscount implements ProductDiscountValue {

    public RelativeProductDiscount(@JsonProperty("permyriad") final int permyriad) {
        super(permyriad);
    }

}
