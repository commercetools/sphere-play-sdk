package draft;

import io.sphere.client.FetchRequest;
import io.sphere.client.QueryRequest;
import io.sphere.client.SearchRequest;
import io.sphere.client.filters.expressions.FilterExpression;

import java.util.Locale;

public interface EntityService {

    // methods to receive one entity
    FetchRequest<Entity> byId(String id);
    // ...

    // methods to use the search API
    SearchRequest<Entity> filter(Locale locale, FilterExpression filter, FilterExpression... filters);
    // ...


    // existing prepared queries
    public QueryRequest<Entity> forXyz(String xyz);
    // ...

    //this method will be in every service that can query data
    //to set the offset and limit already methods exists in io.sphere.client.QueryRequest
    //to set the sort parameter io.sphere.client.QueryRequest will be extended
    public QueryRequest<Entity> query(QueryPredicate queryPredicate);
}
