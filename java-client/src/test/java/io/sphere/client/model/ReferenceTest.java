package io.sphere.client.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;


import static org.fest.assertions.Assertions.assertThat;


public class ReferenceTest {

    private static final Reference<String> STRING_REFERENCE = Reference.<String>create("typeId", "id", "value");

    @Test
    public void serialize() throws Exception {
        final String json = new ObjectMapper().writer().writeValueAsString(STRING_REFERENCE);
        assertThat(json).isEqualTo("{\"typeId\":\"typeId\",\"id\":\"id\"}");
    }

    @Test
    public void deserialize() throws Exception {
        final Reference value = new ObjectMapper().reader(Reference.class).<Reference>readValue("{\"id\":\"id\",\"typeId\":\"typeId\",\"obj\":\"value\"}");
        assertThat(value.getId()).isEqualTo(STRING_REFERENCE.getId());
        assertThat(value.getTypeId()).isEqualTo(STRING_REFERENCE.getTypeId());
        assertThat(value.get()).isEqualTo("value");
    }
}