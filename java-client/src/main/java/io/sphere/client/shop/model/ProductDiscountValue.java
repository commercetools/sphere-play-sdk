package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Defines discount type with the corresponding value. The type can be relative or absolute.
 *
 * @see RelativeProductDiscount
 * @see AbsoluteProductDiscount
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RelativeProductDiscount.class, name = "relative"),
        @JsonSubTypes.Type(value = AbsoluteProductDiscount.class, name = "absolute") })
public interface ProductDiscountValue extends DiscountValue {
}
