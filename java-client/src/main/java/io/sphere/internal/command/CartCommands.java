package io.sphere.internal.command;

import com.google.common.base.Optional;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.client.model.LocalizedString;
import io.sphere.client.model.Money;
import io.sphere.client.model.Reference;
import io.sphere.client.model.ReferenceId;
import io.sphere.client.shop.model.*;
import net.jcip.annotations.Immutable;
import org.codehaus.jackson.annotate.JsonRawValue;

import javax.annotation.Nullable;
import java.util.Currency;

/** Commands issued against the HTTP endpoints for working with shopping carts. */
public class CartCommands {
    @Immutable
    public static final class CreateCart implements Command {
        private Currency currency;
        private String customerId;
        private CountryCode country;
        private Cart.InventoryMode inventoryMode;

        public CreateCart(Currency currency, String customerId, CountryCode country, Cart.InventoryMode inventoryMode) {
            this.currency = currency;
            this.customerId = customerId;
            this.country = country;
            this.inventoryMode = inventoryMode;

        }

        public Currency getCurrency() { return currency; }
        public String getCustomerId() { return customerId; }
        public Cart.InventoryMode getInventoryMode() { return inventoryMode; }
        public CountryCode getCountry() { return country; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CreateCart that = (CreateCart) o;

            if (country != that.country) return false;
            if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
            if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
            if (inventoryMode != that.inventoryMode) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = currency != null ? currency.hashCode() : 0;
            result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
            result = 31 * result + (country != null ? country.hashCode() : 0);
            result = 31 * result + (inventoryMode != null ? inventoryMode.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "CreateCart{" +
                    "currency=" + currency +
                    ", customerId='" + customerId + '\'' +
                    ", country=" + country +
                    ", inventoryMode=" + inventoryMode +
                    '}';
        }
    }

    @Immutable
    public static final class LoginWithAnonymousCart extends CommandBase {
        private final String email;
        private final String password;

        public LoginWithAnonymousCart(String cartId, int cartVersion, String email, String password) {
            super(cartId, cartVersion);
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LoginWithAnonymousCart that = (LoginWithAnonymousCart) o;

            if (email != null ? !email.equals(that.email) : that.email != null) return false;
            if (password != null ? !password.equals(that.password) : that.password != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = email != null ? email.hashCode() : 0;
            result = 31 * result + (password != null ? password.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LoginWithAnonymousCart{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    @Immutable
    public static final class OrderCart extends CommandBase {
        private final PaymentState paymentState;
        @Nullable
        private final String customerNumber;

        public OrderCart(String cartId, int cartVersion, PaymentState paymentState, Optional<String> customerNumber) {
            super(cartId, cartVersion);
            this.paymentState = paymentState;
            this.customerNumber = customerNumber.orNull();
        }

        public PaymentState getPaymentState() { return paymentState; }
        public String getOrderNumber() { return customerNumber; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OrderCart orderCart = (OrderCart) o;

            if (customerNumber != null ? !customerNumber.equals(orderCart.customerNumber) : orderCart.customerNumber != null)
                return false;
            if (paymentState != orderCart.paymentState) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = paymentState != null ? paymentState.hashCode() : 0;
            result = 31 * result + (customerNumber != null ? customerNumber.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "OrderCart{" +
                    "paymentState=" + paymentState +
                    ", customerNumber='" + customerNumber + '\'' +
                    '}';
        }
    }

    public static abstract class CartUpdateAction extends UpdateAction {
        public CartUpdateAction(String action) {
            super(action);
        }
    }

    @Immutable
    public static class AddLineItemFromMasterVariant extends CartUpdateAction {
        private final String productId;
        private final int quantity;
        private final ReferenceId<Channel> supplyChannel;

        public AddLineItemFromMasterVariant(String productId, int quantity, final String channelId) {
            super("addLineItem");
            this.productId = productId;
            this.quantity = quantity;
            this.supplyChannel = Channel.reference(channelId).toReferenceIdOrNull();
        }

        public String getProductId() { return productId; }
        public int getQuantity() { return quantity; }

        public ReferenceId<Channel> getSupplyChannel() {
            return supplyChannel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddLineItemFromMasterVariant that = (AddLineItemFromMasterVariant) o;

            if (quantity != that.quantity) return false;
            if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
            if (supplyChannel != null ? !supplyChannel.equals(that.supplyChannel) : that.supplyChannel != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = productId != null ? productId.hashCode() : 0;
            result = 31 * result + quantity;
            result = 31 * result + (supplyChannel != null ? supplyChannel.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AddLineItemFromMasterVariant{" +
                    "productId='" + productId + '\'' +
                    ", quantity=" + quantity +
                    ", supplyChannel=" + supplyChannel +
                    '}';
        }
    }

    @Immutable
    public static final class AddLineItem extends AddLineItemFromMasterVariant {
        private final int variantId;

        public AddLineItem(String productId, int quantity, int variantId) {
            this(productId, quantity, variantId, null);
        }

        public AddLineItem(String productId, int quantity, int variantId, final String channelId) {
            super(productId, quantity, channelId);
            this.variantId = variantId;
        }

        public int getVariantId() { return variantId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            AddLineItem that = (AddLineItem) o;

            if (variantId != that.variantId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + variantId;
            return result;
        }

        @Override
        public String toString() {
            return "AddLineItem{" +
                    "variantId=" + variantId +
                    '}';
        }
    }

    @Immutable
    public static class RemoveLineItem extends CartUpdateAction {
        private final String lineItemId;

        public RemoveLineItem(String lineItemId) {
            super("removeLineItem");
            this.lineItemId = lineItemId;
        }

        public String getLineItemId() { return lineItemId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RemoveLineItem that = (RemoveLineItem) o;

            if (lineItemId != null ? !lineItemId.equals(that.lineItemId) : that.lineItemId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return lineItemId != null ? lineItemId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "RemoveLineItem{" +
                    "lineItemId='" + lineItemId + '\'' +
                    '}';
        }
    }

    @Immutable
    public static final class AddCustomLineItem extends CartUpdateAction {
        @JsonRawValue private LocalizedString name;
        private Money money;
        private String slug;
        private int quantity;
        private final ReferenceId<TaxCategory> taxCategory;

        public AddCustomLineItem(LocalizedString name, Money money, String slug, ReferenceId<TaxCategory> taxCategory, int quantity) {
            super("addCustomLineItem");
            this.taxCategory = taxCategory;
            this.quantity = quantity;
            this.slug = slug;
            this.money = money;
            this.name = name;
        }

        public LocalizedString getName() { return name; }

        public Money getMoney() { return money; }

        public String getSlug() { return slug; }

        public int getQuantity() { return quantity; }

        public ReferenceId<TaxCategory> getTaxCategory() { return taxCategory; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddCustomLineItem that = (AddCustomLineItem) o;

            if (quantity != that.quantity) return false;
            if (money != null ? !money.equals(that.money) : that.money != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (slug != null ? !slug.equals(that.slug) : that.slug != null) return false;
            if (taxCategory != null ? !taxCategory.equals(that.taxCategory) : that.taxCategory != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (money != null ? money.hashCode() : 0);
            result = 31 * result + (slug != null ? slug.hashCode() : 0);
            result = 31 * result + quantity;
            result = 31 * result + (taxCategory != null ? taxCategory.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AddCustomLineItem{" +
                    "name=" + name +
                    ", money=" + money +
                    ", slug='" + slug + '\'' +
                    ", quantity=" + quantity +
                    ", taxCategory=" + taxCategory +
                    '}';
        }
    }

    @Immutable
    public static class RemoveCustomLineItem extends CartUpdateAction {
        private String customLineItemId;

        public RemoveCustomLineItem(String lineItemId) {
            super("removeCustomLineItem");
            this.customLineItemId = lineItemId;
        }

        public String getCustomLineItemId() { return customLineItemId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RemoveCustomLineItem that = (RemoveCustomLineItem) o;

            if (customLineItemId != null ? !customLineItemId.equals(that.customLineItemId) : that.customLineItemId != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return customLineItemId != null ? customLineItemId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "RemoveCustomLineItem{" +
                    "customLineItemId='" + customLineItemId + '\'' +
                    '}';
        }
    }

    @Immutable
    public static class ChangeLineItemQuantity extends CartUpdateAction {
        private final String lineItemId;
        private final int quantity;

        public ChangeLineItemQuantity(String lineItemId, int quantity) {
            super("changeLineItemQuantity");
            this.lineItemId = lineItemId;
            this.quantity = quantity;
        }

        public String getLineItemId() { return lineItemId; }

        public int getQuantity() { return quantity; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChangeLineItemQuantity that = (ChangeLineItemQuantity) o;

            if (quantity != that.quantity) return false;
            if (lineItemId != null ? !lineItemId.equals(that.lineItemId) : that.lineItemId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = lineItemId != null ? lineItemId.hashCode() : 0;
            result = 31 * result + quantity;
            return result;
        }

        @Override
        public String toString() {
            return "ChangeLineItemQuantity{" +
                    "lineItemId='" + lineItemId + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    @Immutable
    public static final class DecreaseLineItemQuantity extends RemoveLineItem {
        private final int quantity;

        public DecreaseLineItemQuantity(String lineItemId, int quantity) {
            super(lineItemId);
            this.quantity = quantity;
        }

        public int getQuantity() { return quantity; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            DecreaseLineItemQuantity that = (DecreaseLineItemQuantity) o;

            if (quantity != that.quantity) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + quantity;
            return result;
        }

        @Override
        public String toString() {
            return "DecreaseLineItemQuantity{" +
                    "quantity=" + quantity +
                    '}';
        }
    }

    @Immutable
    public static final class SetCustomerEmail extends CartUpdateAction {
        private final String email;

        public SetCustomerEmail(String email) {
            super("setCustomerEmail");
            this.email = email;
        }

        public String getEmail() { return email; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetCustomerEmail that = (SetCustomerEmail) o;

            if (email != null ? !email.equals(that.email) : that.email != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return email != null ? email.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetCustomerEmail{" +
                    "email='" + email + '\'' +
                    '}';
        }
    }

    @Immutable
    public static final class SetBillingAddress extends CartUpdateAction {
        private final Address address;

        public SetBillingAddress(Address address) {
            super("setBillingAddress");
            this.address = address;
        }

        public Address getAddress() { return address; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetBillingAddress that = (SetBillingAddress) o;

            if (address != null ? !address.equals(that.address) : that.address != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return address != null ? address.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetBillingAddress{" +
                    "address=" + address +
                    '}';
        }
    }

    @Immutable
    public static final class SetShippingAddress extends CartUpdateAction {
        private final Address address;

        public SetShippingAddress(Address address) {
            super("setShippingAddress");
            this.address = address;
        }

        public Address getAddress() { return address; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetShippingAddress that = (SetShippingAddress) o;

            if (address != null ? !address.equals(that.address) : that.address != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return address != null ? address.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetShippingAddress{" +
                    "address=" + address +
                    '}';
        }
    }

    @Immutable
    public static final class SetCountry extends CartUpdateAction {
        private final CountryCode country;

        public SetCountry(CountryCode country) {
            super("setCountry");
            this.country = country;
        }

        public CountryCode getCountry() { return country; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetCountry that = (SetCountry) o;

            if (country != that.country) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return country != null ? country.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetCountry{" +
                    "country=" + country +
                    '}';
        }
    }

    @Immutable
    public static final class SetShippingMethod extends CartUpdateAction {
        private final ReferenceId<ShippingMethod> shippingMethod;

        public SetShippingMethod(ReferenceId<ShippingMethod> shippingMethod) {
            super("setShippingMethod");
            this.shippingMethod = shippingMethod;
        }

        public ReferenceId<ShippingMethod> getShippingMethod() { return shippingMethod; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetShippingMethod that = (SetShippingMethod) o;

            if (shippingMethod != null ? !shippingMethod.equals(that.shippingMethod) : that.shippingMethod != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return shippingMethod != null ? shippingMethod.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetShippingMethod{" +
                    "shippingMethod=" + shippingMethod +
                    '}';
        }
    }

    @Immutable
    public static final class SetCustomShippingMethod extends CartUpdateAction {
        private final String shippingMethodName;
        private final ShippingRate shippingRate;
        private final ReferenceId<TaxCategory> taxCategory;

        public SetCustomShippingMethod(String shippingMethodName, ShippingRate shippingRate, ReferenceId<TaxCategory> taxCategory) {
            super("setCustomShippingMethod");
            this.shippingMethodName = shippingMethodName;
            this.shippingRate = shippingRate;
            this.taxCategory = taxCategory;
        }

        public String getShippingMethodName() { return shippingMethodName; }

        public ShippingRate getShippingRate() { return shippingRate; }

        public ReferenceId<TaxCategory> getTaxCategory() { return taxCategory; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetCustomShippingMethod that = (SetCustomShippingMethod) o;

            if (shippingMethodName != null ? !shippingMethodName.equals(that.shippingMethodName) : that.shippingMethodName != null)
                return false;
            if (shippingRate != null ? !shippingRate.equals(that.shippingRate) : that.shippingRate != null)
                return false;
            if (taxCategory != null ? !taxCategory.equals(that.taxCategory) : that.taxCategory != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = shippingMethodName != null ? shippingMethodName.hashCode() : 0;
            result = 31 * result + (shippingRate != null ? shippingRate.hashCode() : 0);
            result = 31 * result + (taxCategory != null ? taxCategory.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "SetCustomShippingMethod{" +
                    "shippingMethodName='" + shippingMethodName + '\'' +
                    ", shippingRate=" + shippingRate +
                    ", taxCategory=" + taxCategory +
                    '}';
        }
    }


    @Immutable
    public static final class SetCustomerId extends CartUpdateAction {
        private final String customerId;

        public SetCustomerId(String customerId) {
            super("setCustomerId");
            this.customerId = customerId;
        }

        public String getCustomerId() {
            return customerId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SetCustomerId that = (SetCustomerId) o;

            if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return customerId != null ? customerId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "SetCustomerId{" +
                    "customerId='" + customerId + '\'' +
                    '}';
        }
    }


    @Immutable
    public static final class AddDiscountCode extends CartUpdateAction {
        private final String discountCode;

        public AddDiscountCode(final String discountCode) {
            super("addDiscountCode");
            this.discountCode = discountCode;
        }

        public String getCode() {
            return discountCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddDiscountCode that = (AddDiscountCode) o;

            if (discountCode != null ? !discountCode.equals(that.discountCode) : that.discountCode != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return discountCode != null ? discountCode.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "AddDiscountCode{" +
                    "discountCode=" + discountCode +
                    '}';
        }
    }

    @Immutable
    public static final class RemoveDiscountCode extends CartUpdateAction {
        private final Reference<DiscountCode> discountCode;

        public RemoveDiscountCode(final Reference<DiscountCode> discountCode) {
            super("removeDiscountCode");
            this.discountCode = discountCode;
        }

        public Reference<DiscountCode> getDiscountCode() {
            return discountCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RemoveDiscountCode that = (RemoveDiscountCode) o;

            if (discountCode != null ? !discountCode.equals(that.discountCode) : that.discountCode != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return discountCode != null ? discountCode.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "RemoveDiscountCode{" +
                    "discountCode=" + discountCode +
                    '}';
        }
    }

    @Immutable
    public static final class RecalculateCartPrices extends CartUpdateAction {

        public RecalculateCartPrices() {
            super("recalculate");
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof RecalculateCartPrices;
        }

        @Override
        public int hashCode() {
            return 5;
        }

        @Override
        public String toString() {
            return "RecalculateCartPrices";
        }
    }
}