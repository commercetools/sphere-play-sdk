package io.sphere.client.shop.model;

import com.google.common.base.Optional;
import io.sphere.client.model.LocalizedString;
import io.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import java.util.List;

public class DiscountCode {
    private final String id;
    private final int version;
    private final String code;
    private final Optional<LocalizedString> name;
    private final Optional<LocalizedString> description;
    private final List<Reference<CartDiscount>> cartDiscounts;
    private final boolean isActive;
    private final Optional<Long> maxApplications;
    private final Optional<Long> maxApplicationsByCustomer;
    private final Optional<String> cartPredicate;
    private final DateTime createdAt;
    private final DateTime lastModifiedAt;

    @JsonCreator
    DiscountCode(@JsonProperty("id") String id, @JsonProperty("value") int version, @JsonProperty("version") String code,
                 @JsonProperty("name") LocalizedString name, @JsonProperty("description") LocalizedString description,
                 @JsonProperty("cartDiscounts") List<Reference<CartDiscount>> cartDiscounts, @JsonProperty("isActive") boolean isActive,
                 @JsonProperty("maxNrOfApplications") Long maxApplications,
                 @JsonProperty("maxApplicationsBySingleCustomer") Long maxApplicationsByCustomer,
                 @JsonProperty("cartPredicate") String cartPredicate, @JsonProperty("createdAt") DateTime createdAt,
                 @JsonProperty("lastModifiedAt") DateTime lastModifiedAt) {
        this(id, version, code, Optional.fromNullable(name), Optional.fromNullable(description), cartDiscounts, isActive,
                Optional.fromNullable(maxApplications), Optional.fromNullable(maxApplicationsByCustomer),
                Optional.fromNullable(cartPredicate), createdAt, lastModifiedAt);
    }

    @JsonIgnore
    public DiscountCode(final String id, final int version, final String code, final Optional<LocalizedString> name,
                        final Optional<LocalizedString> description, final List<Reference<CartDiscount>> cartDiscounts,
                        final boolean isActive, final Optional<Long> maxApplications, final Optional<Long> maxApplicationsByCustomer,
                        final Optional<String> cartPredicate, final DateTime createdAt, final DateTime lastModifiedAt) {
        this.id = id;
        this.version = version;
        this.code = code;
        this.name = name;
        this.description = description;
        this.cartDiscounts = cartDiscounts;
        this.isActive = isActive;
        this.maxApplications = maxApplications;
        this.maxApplicationsByCustomer = maxApplicationsByCustomer;
        this.cartPredicate = cartPredicate;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getCode() {
        return code;
    }

    public Optional<LocalizedString> getName() {
        return name;
    }

    public Optional<LocalizedString> getDescription() {
        return description;
    }

    public List<Reference<CartDiscount>> getCartDiscounts() {
        return cartDiscounts;
    }

    public boolean isActive() {
        return isActive;
    }

    public Optional<Long> getMaxApplications() {
        return maxApplications;
    }

    public Optional<Long> getMaxApplicationsByCustomer() {
        return maxApplicationsByCustomer;
    }

    public Optional<String> getCartPredicate() {
        return cartPredicate;
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

        DiscountCode that = (DiscountCode) o;

        if (isActive != that.isActive) return false;
        if (version != that.version) return false;
        if (cartDiscounts != null ? !cartDiscounts.equals(that.cartDiscounts) : that.cartDiscounts != null)
            return false;
        if (cartPredicate != null ? !cartPredicate.equals(that.cartPredicate) : that.cartPredicate != null)
            return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lastModifiedAt != null ? !lastModifiedAt.equals(that.lastModifiedAt) : that.lastModifiedAt != null)
            return false;
        if (maxApplications != null ? !maxApplications.equals(that.maxApplications) : that.maxApplications != null)
            return false;
        if (maxApplicationsByCustomer != null ? !maxApplicationsByCustomer.equals(that.maxApplicationsByCustomer) : that.maxApplicationsByCustomer != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + version;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cartDiscounts != null ? cartDiscounts.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (maxApplications != null ? maxApplications.hashCode() : 0);
        result = 31 * result + (maxApplicationsByCustomer != null ? maxApplicationsByCustomer.hashCode() : 0);
        result = 31 * result + (cartPredicate != null ? cartPredicate.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (lastModifiedAt != null ? lastModifiedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscountCode{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", code='" + code + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", cartDiscounts=" + cartDiscounts +
                ", isActive=" + isActive +
                ", maxApplications=" + maxApplications +
                ", maxApplicationsByCustomer=" + maxApplicationsByCustomer +
                ", cartPredicate=" + cartPredicate +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
