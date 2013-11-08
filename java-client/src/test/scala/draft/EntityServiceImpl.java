package draft;

import io.sphere.client.FetchRequest;
import io.sphere.client.QueryRequest;
import io.sphere.client.SearchRequest;
import io.sphere.client.filters.expressions.FilterExpression;

import java.util.Locale;

public class EntityServiceImpl implements EntityService {
    @Override
    public FetchRequest<Entity> byId(String id) {
        throw new NotImplementedException("Just a skeleton to demo APIs.");
    }

    @Override
    public SearchRequest<Entity> filter(Locale locale, FilterExpression filter, FilterExpression... filters) {
        throw new NotImplementedException("Just a skeleton to demo APIs.");
    }

    @Override
    public QueryRequest<Entity> forXyz(String xyz) {
        throw new NotImplementedException("Just a skeleton to demo APIs.");
    }

    @Override
    public QueryRequest<Entity> query(QueryPredicate queryPredicate) {
        throw new NotImplementedException("Just a skeleton to demo APIs.");
    }
}
