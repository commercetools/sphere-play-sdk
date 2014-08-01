package io.sphere.client.shop.model;

import com.google.common.base.Optional;
import io.sphere.client.model.LocalizedString;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class ProductDiscount {
    private final String id;
    private final int version;
    private final DateTime lastModifiedAt;
    private final DateTime createdAt;
    private final Optional<LocalizedString> description;
    private final ProductDiscountValue value;
    private final String predicate;
    private final String sortOrder;
    private final boolean isActive;

    @JsonCreator
    public ProductDiscount(@JsonProperty("id") String id, @JsonProperty("version") int version,
                            @JsonProperty("lastModifiedAt") DateTime lastModifiedAt,
                            @JsonProperty("createdAt") DateTime createdAt,
                            @JsonProperty("description") LocalizedString description,
                            @JsonProperty("value") ProductDiscountValue value,
                            @JsonProperty("predicate") String predicate, @JsonProperty("sortOrder") String sortOrder,
                            @JsonProperty("isActive") boolean isActive) {
        this.id = id;
        this.version = version;
        this.lastModifiedAt = lastModifiedAt;
        this.createdAt = createdAt;
        this.description = Optional.fromNullable(description);
        this.value = value;
        this.predicate = predicate;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public DateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalizedString> getDescription() {
        return description;
    }

    /**
     * Defines discount type with the corresponding value. The type can be relative or absolute.
     * @return value of the discount
     */
    public ProductDiscountValue getValue() {
        return value;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDiscount that = (ProductDiscount) o;

        if (isActive != that.isActive) return false;
        if (version != that.version) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        if (!description.equals(that.description)) return false;
        if (!id.equals(that.id)) return false;
        if (!lastModifiedAt.equals(that.lastModifiedAt)) return false;
        if (!predicate.equals(that.predicate)) return false;
        if (!sortOrder.equals(that.sortOrder)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + version;
        result = 31 * result + lastModifiedAt.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + predicate.hashCode();
        result = 31 * result + sortOrder.hashCode();
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDiscount{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", lastModifiedAt=" + lastModifiedAt +
                ", createdAt=" + createdAt +
                ", description=" + description +
                ", value=" + value +
                ", predicate='" + predicate + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
