package draft;

import io.sphere.client.filters.expressions.FilterExpression;
import io.sphere.client.model.QueryResult;
import org.joda.time.DateTime;

import static io.sphere.client.filters.FilterExpr.dateTimeAttribute;

public class UsageExample {
    static void example() {
        //Entity could be Category, Review, Product, ...
        final EntityService entities = new EntityServiceImpl();


        Entity.QueryableFields attr = Entity.attributes();
        final FilterExpression idIsRight = attr.id().equalsAnyOf("foo", "bar", "baz");
        final FilterExpression versionIsRight = attr.score().atLeast(2.0);
        final FilterExpression beforeYesterday = dateTimeAttribute("createdAt").atMost(DateTime.now().minusDays(1));
        QueryPredicate queryPredicate = QueryPredicate.and(idIsRight, versionIsRight).or(QueryPredicate.not(beforeYesterday));
        QueryResult<Entity> result = entities.query(queryPredicate)/*.sort("createdAt", ASC)*/.pageSize(100).page(10).fetch();

        /*
        TODO decouple the attributes from FilterExpression if possible
        static helpers to not require writing the name of the attribute as String
        still possible to use Strings for custom fields in products
        */
    }

}
