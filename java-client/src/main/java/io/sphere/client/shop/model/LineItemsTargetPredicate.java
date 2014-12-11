package io.sphere.client.shop.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class LineItemsTargetPredicate extends CartDiscountTargetPredicate {
    private final String predicate;

    public LineItemsTargetPredicate(@JsonProperty("predicate") final String predicate) {
        this.predicate = predicate;
    }

    public String getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineItemsTargetPredicate that = (LineItemsTargetPredicate) o;

        if (predicate != null ? !predicate.equals(that.predicate) : that.predicate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return predicate != null ? predicate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LineItemsTargetPredicate{" +
                "predicate='" + predicate + '\'' +
                '}';
    }
}