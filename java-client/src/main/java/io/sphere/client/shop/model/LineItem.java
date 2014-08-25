package io.sphere.client.shop.model;

import io.sphere.client.model.LocalizedString;
import io.sphere.client.model.Money;
import java.util.Locale;

import io.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Single product variant in a {@link Cart} or {@link Order}, with a quantity. */
public class LineItem {
    @Nonnull private String id;
    @Nonnull private String productId;
    @Nonnull @JsonProperty("name") private LocalizedString productName = Attribute.defaultLocalizedString;
    @Nonnull @JsonProperty("variant") private Variant variant;
    private int quantity;
    @Nonnull private Price price;
    private TaxRate taxRate;
    private Reference<Channel> supplyChannel = Channel.emptyReference();

    // for JSON deserializer
    private LineItem() {}

    private LineItem(final String id, final String productId, final LocalizedString productName, final Variant variant,
                    final int quantity, final Price price, final TaxRate taxRate, final Reference<Channel> supplyChannel) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.variant = variant;
        this.quantity = quantity;
        this.price = price;
        this.taxRate = taxRate;
        this.supplyChannel = supplyChannel;
    }

    public static LineItem create(final String id, final String productId, final LocalizedString productName,
                                  final Variant variant, final int quantity, final Price price, final TaxRate taxRate,
                                  final Reference<Channel> supplyChannel) {
        return new LineItem(id, productId, productName, variant, quantity, price, taxRate, supplyChannel);
    }

    /** Unique id of this line item. */
    @Nonnull public String getId() { return id; }

    /** Unique id of the product. */
    @Nonnull public String getProductId() { return productId; }

    /** Name of the product. If there is only one translation it will return this. Otherwise,
        it will return a random one. If there are no translations will return the empty string. */
    @Nonnull public String getProductName() { return productName.get(); }

    /**
     * @param locale
     * @return The product name in the requested locale. It it doesn't exist will return the empty string.
     */
    @Nonnull public String getProductName(Locale locale) { return productName.get(locale); }

    /** Copy of the product variant from the time when time line item was created. */
    @Nonnull public Variant getVariant() { return variant; }

    /** Number of items ordered. */
    public int getQuantity() { return quantity; }

    /**
     *  The total price of this line item, that is price value times quantity.
     *  When line item is discounted, the total price is calculated based on this discounted price. */
    @Nonnull public Money getTotalPrice() {
        return getUnitPrice().multiply(quantity);
    }

    /**
     * The base price of this line item, for single unit.
     * When the line item is discounted, the base price is this discounted price. */
    @Nonnull public Money getUnitPrice() {
        if (price.getDiscounted().isPresent()) {
            return price.getDiscounted().get().getValue();
        } else {
            return price.getValue();
        }
    }

    /**
     *  The total price of this line item before discount, that is price value times quantity. */
    @Nonnull public Money getTotalPriceBeforeDiscount() {
        return getUnitPriceBeforeDiscount().multiply(quantity);
    }

    /**
     * The base price of this line item before discount, for single unit. */
    @Nonnull public Money getUnitPriceBeforeDiscount() {
        return price.getValue();
    }

    /** The price. */
    @Nonnull public Price getPrice() { return price; }

    /** The tax rate of this line item. Optional.
     *
     *  <p>The tax rate is selected based on the cart's shipping address and is only set when the
     *  shipping address is set. */
    @Nullable public TaxRate getTaxRate() { return taxRate; }

    /**
     * The optional assigned Channel for this LineItem.
     */
    public Reference<Channel> getSupplyChannel() {
        return supplyChannel;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", productName=" + productName +
                ", variant=" + variant +
                ", quantity=" + quantity +
                ", price=" + price +
                ", taxRate=" + taxRate +
                ", supplyChannel=" + supplyChannel +
                '}';
    }
}
