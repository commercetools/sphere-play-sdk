package sphere;

import sphere.util.OAuthClient;

import com.ning.http.client.AsyncHttpClient;
import de.commercetools.sphere.client.SphereShopClient;

import java.util.concurrent.TimeUnit;

/** Provides default configured and initialized instance of {@link SphereClient}.
 *  The instance is designed to be shared by all controllers in your application. */
public class Sphere {
    /** This is a static class. */
    private Sphere() {}

    private final static SphereClient client = createClient();

    /** Returns singleton instance of the Sphere class. */
    public static SphereClient getShopClient() { return client; }

    private static SphereClient createClient() {
        Config config = Config.root();
        ClientCredentials clientCredentials = ClientCredentials.create(config, new OAuthClient());
        Validation<Void> refreshedTokens = clientCredentials.refreshAsync().getWrappedPromise().await(60, TimeUnit.SECONDS).get(); // HACK TEMP because of tests
        // TODO switch to Futures API that rethrows exceptions (next Play release?)
        if (refreshedTokens.isError()) {
            throw new RuntimeException(refreshedTokens.getError().getMessage());
        }
        ProjectEndpoints projectEndpoints = Endpoints.forProject(config.coreEndpoint(), config.projectID());
        SphereShopClient shopClient = new SphereShopClient(new AsyncHttpClient(), config.shopClientConfig());

        return new SphereClient(
            shopClient,
            clientCredentials,
            new DefaultProducts(clientCredentials, projectEndpoints),
            new DefaultProductDefinitions(clientCredentials, projectEndpoints),
            new DefaultCategories(clientCredentials, projectEndpoints)
        );
    }
}
