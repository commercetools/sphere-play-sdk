package io.sphere.client.shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import io.sphere.client.model.EmptyReference;
import io.sphere.client.model.Money;
import io.sphere.client.model.Reference;
import io.sphere.client.model.VersionedId;
import com.neovisionaries.i18n.CountryCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

/** Superclass of {@link Cart} and {@link Order}. */
@JsonIgnoreProperties("type")
public abstract class LineItemContainer {
    @Nonnull private String id = "";
    @JsonProperty("version") private int version;
    @Nonnull private List<LineItem> lineItems = new ArrayList<LineItem>();
    @Nonnull private List<CustomLineItem> customLineItems = new ArrayList<CustomLineItem>();
    private String customerId = "";
    private String customerEmail = "";
    @Nonnull private DateTime lastModifiedAt;
    @Nonnull private DateTime createdAt;
    @Nonnull protected Money totalPrice;
    private TaxedPrice taxedPrice;
    private Address shippingAddress;
    private Address billingAddress;
    private CountryCode country;
    @Nonnull private Reference<CustomerGroup> customerGroup = EmptyReference.create("customerGroup");
    private ShippingInfo shippingInfo;
    private List<Reference<DiscountCodeWithState>> discountCodes = new ArrayList<Reference<DiscountCodeWithState>>();

    protected LineItemContainer() {}

    /** The sum of quantities of line items. */
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (LineItem lineItem: this.getLineItems()) {
            totalQuantity += lineItem.getQuantity();
        }
        return totalQuantity;
    }

    // for tests
    protected LineItemContainer(String id, int version) {
        this.id = id;
        this.version = version;
    }

    /** Calculates the total of all line items (without the custom line items). */
    @Nonnull public Money getLineItemTotalPrice() {
        Money total = new Money(new BigDecimal(0), totalPrice.getCurrencyCode());
        for (LineItem lineItem: this.getLineItems()) {
            total = total.plus(lineItem.getTotalPrice());
        }
        return total;
    }

    public Optional<LineItem> getLineItemById(final String id) {
        return Iterables.tryFind(getLineItems(), new Predicate<LineItem>() {
            @Override
            public boolean apply(@Nullable LineItem lineItem) {
                return lineItem.getId() == id;
            }
        });
    }

    public Optional<CustomLineItem> getCustomLineItemById(final String id) {
        return Iterables.tryFind(getCustomLineItems(), new Predicate<CustomLineItem>() {
            @Override
            public boolean apply(@Nullable CustomLineItem customLineItem) {
                return customLineItem.getId() == id;
            }
        });
    }


    // --------------------------------------------------------
    // Getters
    // --------------------------------------------------------

    /** The unique id. */
    @Nonnull public String getId() { return id; }

    /** The {@link #getId() id} with version at which this object was fetched. */
    @Nonnull public VersionedId getIdAndVersion() { return VersionedId.create(id, version); }

    /** The items in this cart or order. Does not fire a query to the backend. */
    @Nonnull public List<LineItem> getLineItems() { return lineItems; }

    /** The custom items in this cart or order. Does not fire a query to the backend. */
    @Nonnull public List<CustomLineItem> getCustomLineItems() { return customLineItems; }

    /** The date and time when this object was last modified. */
    @Nonnull public DateTime getLastModifiedAt() { return lastModifiedAt; }

    /** The date and time when this object was created. */
    @Nonnull public DateTime getCreatedAt() { return createdAt; }

    /** The shipping address. Optional. */
    public Address getShippingAddress() { return shippingAddress; }

    /** The billing address. Optional. */
    public Address getBillingAddress() { return billingAddress; }

    /** The customer to who this Cart or Order belongs. Can be empty if the customer hasn't registered yet. */
    public String getCustomerId() { return customerId; }

    /** The email of the anonymous customer to who this Cart or Order belongs. This field is intended for the case
     * when the customer completes a checkout without registration, only providing email and shipping address. */
    public String getCustomerEmail() { return customerEmail; }

    /** The customer group of the customer, used for price calculations. */
    @Nonnull public Reference<CustomerGroup> getCustomerGroup() { return customerGroup; }

    /** The sum of prices of all line items (including custom line items) plus the shipping rate. */
    @Nonnull public Money getTotalPrice() { return totalPrice; }

    /** The currency. */
    public Currency getCurrency() { return Currency.getInstance(totalPrice.getCurrencyCode()); }

    /** The taxed total price, defined only when the shipping address is set.
     *  Tax rates are determined by the backend based on the country and state of the shipping address. */
    public TaxedPrice getTaxedPrice() { return taxedPrice; }

    /** The country used for price calculations. Optional. */
    public CountryCode getCountry() { return country; }

    /** The shipping information. Set once cart the shipping method is set. */
    public ShippingInfo getShippingInfo() { return shippingInfo; }

    /**
     * The references to the discount codes applied to this cart.
     */
    public List<Reference<DiscountCodeWithState>> getDiscountCodes() {
        return discountCodes;
    }

    @Override
    public String toString() {
        return "LineItemContainer{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", lineItems=" + lineItems +
                ", customLineItems=" + customLineItems +
                ", customerId='" + customerId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", createdAt=" + createdAt +
                ", totalPrice=" + totalPrice +
                ", taxedPrice=" + taxedPrice +
                ", shippingAddress=" + shippingAddress +
                ", billingAddress=" + billingAddress +
                ", country=" + country +
                ", customerGroup=" + customerGroup +
                ", shippingInfo=" + shippingInfo +
                ", discountCodes=" + discountCodes +
                '}';
    }
}