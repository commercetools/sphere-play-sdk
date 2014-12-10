package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Defines discount type with the corresponding value. The type can be relative or absolute.
 *
 * @see RelativeDiscount
 * @see AbsoluteDiscount
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RelativeDiscount.class, name = "relative"),
        @JsonSubTypes.Type(value = AbsoluteDiscount.class, name = "absolute") })
public abstract class DiscountValue {
}
