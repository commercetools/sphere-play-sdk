package io.sphere.sdk.utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.DateTime;

final class Iso8601DateTimeJacksonModule extends SimpleModule {
    private static final long serialVersionUID = 0L;

    public Iso8601DateTimeJacksonModule() {
        addSerializer(DateTime.class, new Iso8601JsonSerializer());
        addDeserializer(DateTime.class, new Iso8601JsonDeserializer());
    }
}
