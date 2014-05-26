package io.sphere.client.shop.model;

import io.sphere.client.model.LocalizedString;

/**
 * @since 0.58.0
 */
public final class LocalizableEnum {

    private final String key;
    private final LocalizedString label;

    public LocalizableEnum(final String key, final LocalizedString label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public LocalizedString getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalizableEnum)) return false;

        LocalizableEnum that = (LocalizableEnum) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        if (!label.equals(that.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocalizableEnum{" +
                "key='" + key + '\'' +
                ", label=" + label +
                '}';
    }
}