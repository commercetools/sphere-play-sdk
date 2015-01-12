package io.sphere.client;

/** Specifies how products are sorted in {@link SearchRequest#sort}.
 * Use e.g. {@code ProductSort.price.asc}, {@code ProductSort.name.desc}. */
public abstract class ProductSort {
    private static abstract class DirectedProductSearch extends ProductSort {
        private final SortDirection direction;

        protected DirectedProductSearch(SortDirection direction) {
            this.direction = direction;
        }

        public SortDirection getDirection() { return direction; }

        @Override
        public String toString() {
            return getClass().getCanonicalName() + "{" +
                    "direction=" + direction +
                    '}';
        }
    }

    // ---------------------------
    // Relevance
    // ---------------------------
    /** Specifies sorting by descending relevance (always from the most relevant products). This is the default. */
    public static final Relevance relevance = new Relevance();
    /** Specifies sorting by descending relevance (always from the most relevant products). This is the default. */
    public static class Relevance extends ProductSort {
        private Relevance() {}
        @Override public String toString() {
            return "[Relevance]";
        }
    }
    // ---------------------------
    // Price
    // ---------------------------
    /** Specifies sorting by price, ascending or descending. */
    public static final PriceSorts price = new PriceSorts();
    /** Specifies sorting by price, ascending or descending. */
    public static class PriceSorts {
        /** Specifies sorting by price, descending. */
        public final Price desc = new Price(SortDirection.DESC);
        /** Specifies sorting by price, ascending. */
        public final Price asc = new Price(SortDirection.ASC);
    }
    /** Specifies sorting by price. */
    public static class Price extends DirectedProductSearch {
        private Price(SortDirection direction) {
            super(direction);
        }
    }

    // ---------------------------
    // Name
    // ---------------------------
    /** Specifies sorting by name, ascending or descending. */
    public static final NameSorts name = new NameSorts();
    /** Specifies sorting by name, ascending or descending. */
    public static class NameSorts {
        /** Specifies sorting by name, descending. */
        public final Name desc = new Name(SortDirection.DESC);
        /** Specifies sorting by name, ascending. */
        public final Name asc = new Name(SortDirection.ASC);
    }
    /** Specifies sorting by name. */
    public static class Name extends DirectedProductSearch {
        private Name(SortDirection direction) {
            super(direction);
        }
    }

    // ---------------------------
    // attributes
    // ---------------------------
    public static final AttributeSorts attributes = new AttributeSorts();

    public static class AttributeSortsPreDirection {
        public final AttributeProductSort asc;
        public final AttributeProductSort desc;

        AttributeSortsPreDirection(final String fieldSortWithoutDirection) {
            asc = new AttributeProductSort(fieldSortWithoutDirection + " asc");
            desc = new AttributeProductSort(fieldSortWithoutDirection + " desc");
        }
    }

    public static class AttributeSorts {
        public AttributeSortsPreDirection text(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName);
        }

        public AttributeSortsPreDirection date(final String fieldName) {
            return text(fieldName);
        }

        public AttributeSortsPreDirection time(final String fieldName) {
            return text(fieldName);
        }

        public AttributeSortsPreDirection datetime(final String fieldName) {
            return text(fieldName);
        }

        public AttributeSortsPreDirection bool(final String fieldName) {
            return text(fieldName);
        }

        public AttributeSortsPreDirection number(final String fieldName) {
            return text(fieldName);
        }

        public AttributeSortsPreDirection localizedString(final String fieldName, final String lang) {
            return new AttributeSortsPreDirection(fieldName + "." + lang.toLowerCase());
        }

        public AttributeSortsPreDirection enumKey(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".key");
        }

        public AttributeSortsPreDirection localizableEnumKey(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".key");
        }

        public AttributeSortsPreDirection enumLabel(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".key");
        }

        public AttributeSortsPreDirection localizableEnumLabel(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".key");
        }

        public AttributeSortsPreDirection localizableEnumLabel(final String fieldName, final String lang) {
            return new AttributeSortsPreDirection(fieldName + ".label." + lang.toLowerCase());
        }

        public AttributeSortsPreDirection moneyCentAmount(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".centAmount");
        }

        public AttributeSortsPreDirection moneyCurrencyCode(final String fieldName) {
            return new AttributeSortsPreDirection(fieldName + ".currencyCode");
        }
    }
}
