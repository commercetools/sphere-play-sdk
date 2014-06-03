package io.sphere.client.shop.model;

import com.google.common.collect.Maps;
import io.sphere.client.model.LocalizedString;

import java.util.Locale;
import java.util.Map;

final class LocalizedStringUtils {
    private LocalizedStringUtils() {
    }

    static LocalizedString fromStringStringMap(final Map<String, String> sourceMap) {
        Map<Locale, String> targetMap = Maps.newHashMap();
        for(final Map.Entry<String, String> entry : sourceMap.entrySet()) {
            final Locale key = Locale.forLanguageTag(entry.getKey());
            targetMap.put(key, entry.getValue());
        }
        return new LocalizedString(targetMap);
    }
}
