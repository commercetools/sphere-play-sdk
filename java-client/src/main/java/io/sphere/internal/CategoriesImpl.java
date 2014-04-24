package io.sphere.internal;

import io.sphere.client.ProjectEndpoints;
import io.sphere.client.QueryRequest;
import io.sphere.client.model.QueryResult;
import io.sphere.client.model.products.BackendCategory;
import io.sphere.internal.request.RequestFactory;
import net.jcip.annotations.Immutable;
import org.codehaus.jackson.type.TypeReference;

@Immutable
public final class CategoriesImpl extends ProjectScopedAPI<BackendCategory> implements Categories {
    public CategoriesImpl(RequestFactory requestFactory, ProjectEndpoints endpoints) {
        super(requestFactory, endpoints, new TypeReference<BackendCategory>() {}, new TypeReference<QueryResult<BackendCategory>>() { });
    }

    /** Queries categories. */
    public QueryRequest<BackendCategory> query() {
        return queryImpl(endpoints.categories.querySorted());
    }

    /** Queries categories. */
    public QueryRequest<BackendCategory> all() {
        return query();
    }
}
