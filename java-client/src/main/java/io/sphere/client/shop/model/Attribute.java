package io.sphere.client.shop.model;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import io.sphere.client.model.LocalizedString;
import io.sphere.internal.util.Log;
import io.sphere.client.model.Money;
import io.sphere.internal.util.Util;
import net.jcip.annotations.Immutable;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.annotation.Nonnull;
import java.util.*;

/** Custom attribute of a {@link io.sphere.client.shop.model.Product}. */
@Immutable
public class Attribute {
    @Nonnull private final String name;
    private final Object value;

    /** Name of this custom attribute. */
    @Nonnull public String getName() { return name; }

    /** Value of this custom attribute. */
    public Object getValue() { return value; }

    @JsonCreator
    public Attribute(@JsonProperty("name") String name, @JsonProperty("value") Object value) {
        if (Strings.isNullOrEmpty(name)) throw new IllegalArgumentException("Attribute name can't be empty.");
        this.name = name;
        this.value = value;
    }

    // ------------------------------
    // Defaults
    // ------------------------------

    static String                   defaultString           = "";
    static int                      defaultInt              = 0;
    static double                   defaultDouble           = 0.0;
    static Money                    defaultMoney            = null;
    static DateTime                 defaultDateTime         = null;
    static Enum                     defaultEnum             = new Enum("", "");
    public static LocalizedString   defaultLocalizedString  = new LocalizedString(ImmutableMap.<Locale, String>of());

    // ------------------------------
    // Typed value getters
    // ------------------------------

    /** If this is a string attribute, returns the string value.
     *  @return The value or empty string if the value is not a string. */
    public String getString() {
        Object v = getValue();
        if (v == null || !(v instanceof String)) return defaultString;
        return (String)v;
    }

    /** If this is a string attribute, returns the string value.
     *  @return The value or empty string if the value is not a string. */
    public LocalizedString getLocalizedString() {
        Object v = getValue();
        if (v == null || !(v instanceof LocalizedString)) return defaultLocalizedString;
        return (LocalizedString)v;
    }

    /** If this is a number attribute, returns the integer value.
     *  @return The value or 0 if the value is not an integer. */
    public int getInt() {
        Object v = getValue();
        if (!(v instanceof Integer)) return defaultInt;
        return (Integer)v;
    }

    /** If this is a number attribute, returns the double value.
     *  @return The value or 0.0 if the value is not an double. */
    public double getDouble() {
        Object v = getValue();
        // getDouble should work for integers too
        if (v instanceof Integer) return getInt();
        if (!(v instanceof Double)) return defaultDouble;
        return (Double)v;
    }

    /** If this is a money attribute, returns the money value.
     *  @return The value or null if the value is not a money instance. */
    public Money getMoney() {
        // Jackson has no way of knowing that an attribute is a money attribute and its value should be parsed as Money.
        // It sees a json object {'currencyCode':'EUR','centAmount':1200} and parses it as LinkedHashMap.
        Object v = getValue();
        if (!(v instanceof Map)) return defaultMoney;
        return convertToMoney(v);
    }

    private Money convertToMoney(final Object v) {
        return new ObjectMapper().convertValue(v, Money.class);
    }

    /** If this is an enum attribute, returns the value.
     *  @return The value or the empty string if the value is not an enum instance. */
    public Enum getEnum() {
        Object v = getValue();
        if (!(v instanceof Map)) return defaultEnum;
        else {
            return extractEnum((Map) v);
        }
    }

    private Enum extractEnum(Map map) {
        String label = (String) map.get("label");
        if (Strings.isNullOrEmpty(label)){
            return defaultEnum;
        }
        else return new Enum((String) map.get("key"), label);
    }

    @SuppressWarnings("unchecked")//since object has no type information it needs to be casted
    public LocalizableEnum getLocalizableEnum() {
        LocalizableEnum result = new LocalizableEnum("", defaultLocalizedString);
        if (getValue() instanceof Map) {
            final Map data = (Map) getValue();
            result = extractLocalizableEnum(data);
        }
        return result;
    }

    @SuppressWarnings("unchecked")//since object has no type information it needs to be casted
    private LocalizableEnum extractLocalizableEnum(Map data) {
        final String key = data.get("key").toString();
        final Map<String, String> labelsStringMap = (Map<String, String>) data.get("label");
        final Map<Locale, String> labelsLocaleMap = Maps.newHashMap();
        for (Map.Entry<String, String> entry : labelsStringMap.entrySet()) {
            labelsLocaleMap.put(Util.fromLanguageTag(entry.getKey()), entry.getValue());
        }
        return new LocalizableEnum(key, new LocalizedString(labelsLocaleMap));
    }

    @SuppressWarnings("unchecked")
    public <T> ImmutableSet<T> getSet(final Class<T> clazz) {
        ImmutableSet<T> result = ImmutableSet.of();


        //TODO make static
        final Map<Class, Function<Object, Object>> mappers = Maps.newHashMap();
        mappers.put(LocalizableEnum.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return extractLocalizableEnum((Map) input);
            }
        });
        mappers.put(LocalizedString.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return LocalizedStringUtils.fromStringStringMap((Map<String, String>) input);
            }
        });
        final Function<Object, Object> primitive = new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return input;
            }
        };
        mappers.put(String.class, primitive);
        mappers.put(Enum.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return extractEnum((Map) input);
            }
        });
        mappers.put(Integer.class, primitive);
        mappers.put(Double.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                Object result = input;
                if (result.getClass().equals(Integer.class)) {
                    result = ((Integer)input).doubleValue();
                }
                return result;
            }
        });
        mappers.put(Money.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                return convertToMoney(input);
            }
        });
        mappers.put(DateTime.class, new Function<Object, Object>() {
            @Override
            public Object apply(final Object input) {
                final String dateTimeAsString = input.toString();
                DateTime result;
                final String timePattern = "HH:mm:ss";
                final String datePattern = "yyyy-MM-dd";
                if (dateTimeAsString.length() == timePattern.length()) {
                    result = DateTimeFormat.forPattern(timePattern).parseDateTime(dateTimeAsString);
                } else if (dateTimeAsString.length() == datePattern.length()) {
                    result = DateTimeFormat.forPattern(datePattern).parseDateTime(dateTimeAsString);
                } else {
                    result = dateTimeFormat.parseDateTime(dateTimeAsString);
                }
                return result;
            }
        });
        if (getValue() instanceof List) {
            if (mappers.containsKey(clazz)) {
                final List data = (List) getValue();
                result = ImmutableSet.copyOf(Iterables.transform(data, new Function() {
                    @Override
                    public T apply(final Object value) {
                        return (T) mappers.get(clazz).apply(value);
                    }
                }));
            } else {
                Log.warn("mapper not present for " + clazz);
            }
        } else {
            Log.warn("unexpected class " + getValue() + " " + getValue().getClass());
        }
        return result;
    }

    private static DateTimeFormatter dateTimeFormat = ISODateTimeFormat.dateTimeParser();
    /** If this is a DateTime attribute, returns the DateTime value.
     *  @return The value or null if the value is not a DateTime. */
    public DateTime getDateTime() {
        // In case user creates attributes with LocalDate values
        Object v = getValue();
        if (v instanceof DateTime) return (DateTime)v;
        // The backend returns dates and times as strings
        String s = getString();
        if (Strings.isNullOrEmpty(s)) return defaultDateTime;
        try {
            return dateTimeFormat.parseDateTime(s);
        } catch (IllegalArgumentException e) {
            // To handle getDate() called on string attributes
            Log.error("Invalid DateTime: " + e.getMessage());
            return defaultDateTime;
        }
    }

    @Override public String toString() {
        return "[" + getName() + ": " + getValue() + "]";
    }

    // ---------------------------------
    // equals() and hashCode()
    // ---------------------------------

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        if (!name.equals(attribute.name)) return false;
        if (value != null ? !value.equals(attribute.value) : attribute.value != null) return false;
        return true;
    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    /**
     * The value of a custom enum attribute.
     */
    public static class Enum {
        /**
         * The unique and machine-readable value for this enum value.
         */
        public final String key;
        /**
         * The human-readable, and translated label for this value.
         */
        public final String label;

        public Enum(String key, String label) {
            this.key = key;
            this.label = label;
        }

        @Override
        public String toString() {
            return "[Enum key='" + key + "' value='" + label +"']";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Enum anEnum = (Enum) o;

            if (!key.equals(anEnum.key)) return false;
            if (!label.equals(anEnum.label)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }
}
