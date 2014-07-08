package io.sphere.internal.errors;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.sphere.client.SphereError;

import javax.annotation.Nonnull;
import java.util.List;

/** Response object returned by the Sphere Projects Web Service in case of an error.
 *  @see <a href="http://sphere.io/dev/HTTP_API_Projects_Errors.html">API documentation</a> */
public class SphereErrorResponse {
    private int statusCode;
    private String message = "";
    @Nonnull private List<SphereError> errors = ImmutableList.of();

    // for JSON deserializer
    private SphereErrorResponse() {}

    private SphereErrorResponse(final int statusCode) {
        this.statusCode = statusCode;
    }

    /** The HTTP status code. */
    public int getStatusCode() { return statusCode; }

    /** The message of the first error, for convenience. */
    public String getMessage() { return message; }

    /** The individual errors. */
    @Nonnull public List<SphereError> getErrors() { return errors; }

    @Override public String toString() {
        return String.format("[" + getStatusCode() + "]" + "\n  " + Joiner.on("\n  ").join(getErrors()));
    }

    public static SphereErrorResponse of(final int statusCode) {
        return new SphereErrorResponse(statusCode);
    }
}
