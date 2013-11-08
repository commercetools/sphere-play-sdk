package draft;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import static io.sphere.client.filters.FilterExpr.*;

public class Entity {
    @Nonnull private String id;
    private int version;
    private Double score;
    @Nonnull private DateTime createdAt;

    public static QueryableFields attributes() {
     return new QueryableFields();
    }

    //TODO generate if possible
    public static class QueryableFields {
        public StringAttrDSL id() {
            return stringAttribute("id");
        }

        public NumberAttributeDSL score() {
            return numberAttribute("score");
        }

        public DateTimeAttributeDSL createdAt() {
            return dateTimeAttribute("createdAt");
        }
    }
}

