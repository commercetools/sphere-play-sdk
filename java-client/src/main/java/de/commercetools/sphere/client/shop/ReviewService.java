package de.commercetools.sphere.client.shop;

import de.commercetools.sphere.client.CommandRequest;
import de.commercetools.sphere.client.FetchRequest;
import de.commercetools.sphere.client.QueryRequest;
import de.commercetools.sphere.client.shop.model.*;

/** Sphere HTTP API for working with reviews in a given project. */
public interface ReviewService {
    /** Finds a review by an id. */
    FetchRequest<Review> byId(String id);

    /** Queries all reviews in current project. */
    QueryRequest<Review> all();

    /** Queries all reviews of given customer. */
    public QueryRequest<Review> byCustomerId(String customerId);

    /** Queries all reviews of given customer for a specific product. */
    public QueryRequest<Review> byCustomerIdProductId(String customerId, String productId);

    /** Queries all reviews for given product. */
    public QueryRequest<Review> byProductId(String productId);

    /** Creates a review. At least one of the three optional parameters (title, text, score) must be set. */
    public CommandRequest<Review> createReview(
            String productId, String customerId, String authorName, String title, String text, Double score);

    /** Updates a review. At least one of the three optional parameters (title, text, score) must be set. */
    public CommandRequest<Review> updateReview(String reviewId, int reviewVersion, String title, String text, Double score);
}