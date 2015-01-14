package io.sphere.client;

public class AttributeProductSort extends ProductSort {
    private static final String base = "variants.attributes.";
    private final String fieldSort;

    public AttributeProductSort(final String fieldSort) {
        this.fieldSort = fieldSort;
    }

    public String toSphereSort() {
        return base + fieldSort;
    }
}
