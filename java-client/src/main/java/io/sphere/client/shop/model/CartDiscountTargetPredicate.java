package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LineItemsTargetPredicate.class, name = "lineItems"),
        @JsonSubTypes.Type(value = ShippingTargetPredicate.class, name = "shipping") })
public abstract class CartDiscountTargetPredicate {
}
