package io.sphere.client.shop.model;

import com.google.common.base.Optional;
import io.sphere.client.model.LocalizedString;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class CartDiscount {
    private final String id;
    private final int version;
    private final DiscountValue discountValue;
    private final String cartPredicate;
    private final CartDiscountTargetPredicate target;
    private final LocalizedString name;
    private final Optional<LocalizedString> description;
    private final Optional<DateTime> validFrom;
    private final Optional<DateTime> validUntil;
    private final boolean isActive;
    private final boolean requiresDiscountCode;
    private final String sortOrder;
    private final DateTime createdAt;
    private final DateTime lastModifiedAt;

    @JsonCreator
    CartDiscount(@JsonProperty("id") String id, @JsonProperty("version") int version, @JsonProperty("discountValue") DiscountValue discountValue,
                 @JsonProperty("cartPredicate") String cartPredicate, @JsonProperty("target") CartDiscountTargetPredicate target,
                 @JsonProperty("name") LocalizedString name, @JsonProperty("description") LocalizedString description,
                 @JsonProperty("validFrom") DateTime validFrom, @JsonProperty("validUntil") DateTime validUntil,
                 @JsonProperty("isActive") boolean isActive, @JsonProperty("requiresDiscountCode") boolean requiresDiscountCode,
                 @JsonProperty("sortOrder") String sortOrder, @JsonProperty("createdAt") DateTime createdAt,
                 @JsonProperty("lastModifiedAt") DateTime lastModifiedAt) {
        this(id, version, discountValue, cartPredicate, target, name, Optional.fromNullable(description), Optional.fromNullable(validFrom),
                Optional.fromNullable(validUntil), isActive, requiresDiscountCode, sortOrder, createdAt, lastModifiedAt);
    }

    @JsonIgnore
    public CartDiscount(final String id, final int version, final DiscountValue discountValue, final String cartPredicate,
                        final CartDiscountTargetPredicate target, final LocalizedString name, final Optional<LocalizedString> description,
                        final Optional<DateTime> validFrom, final Optional<DateTime> validUntil, final boolean isActive,
                        final boolean requiresDiscountCode, final String sortOrder, final DateTime createdAt, final DateTime lastModifiedAt) {
        this.id = id;
        this.version = version;
        this.discountValue = discountValue;
        this.cartPredicate = cartPredicate;
        this.target = target;
        this.name = name;
        this.description = description;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.isActive = isActive;
        this.requiresDiscountCode = requiresDiscountCode;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public DiscountValue getDiscountValue() {
        return discountValue;
    }

    public String getCartPredicate() {
        return cartPredicate;
    }

    public CartDiscountTargetPredicate getTarget() {
        return target;
    }

    public LocalizedString getName() {
        return name;
    }

    public Optional<LocalizedString> getDescription() {
        return description;
    }

    public Optional<DateTime> getValidFrom() {
        return validFrom;
    }

    public Optional<DateTime> getValidUntil() {
        return validUntil;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isRequiresDiscountCode() {
        return requiresDiscountCode;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartDiscount that = (CartDiscount) o;

        if (isActive != that.isActive) return false;
        if (requiresDiscountCode != that.requiresDiscountCode) return false;
        if (version != that.version) return false;
        if (cartPredicate != null ? !cartPredicate.equals(that.cartPredicate) : that.cartPredicate != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (discountValue != null ? !discountValue.equals(that.discountValue) : that.discountValue != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lastModifiedAt != null ? !lastModifiedAt.equals(that.lastModifiedAt) : that.lastModifiedAt != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sortOrder != null ? !sortOrder.equals(that.sortOrder) : that.sortOrder != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        if (validFrom != null ? !validFrom.equals(that.validFrom) : that.validFrom != null) return false;
        if (validUntil != null ? !validUntil.equals(that.validUntil) : that.validUntil != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + version;
        result = 31 * result + (discountValue != null ? discountValue.hashCode() : 0);
        result = 31 * result + (cartPredicate != null ? cartPredicate.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (validFrom != null ? validFrom.hashCode() : 0);
        result = 31 * result + (validUntil != null ? validUntil.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (requiresDiscountCode ? 1 : 0);
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (lastModifiedAt != null ? lastModifiedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CartDiscount{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", discountValue=" + discountValue +
                ", cartPredicate='" + cartPredicate + '\'' +
                ", target=" + target +
                ", name=" + name +
                ", description=" + description +
                ", validFrom=" + validFrom +
                ", validUntil=" + validUntil +
                ", isActive=" + isActive +
                ", requiresDiscountCode=" + requiresDiscountCode +
                ", sortOrder='" + sortOrder + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}