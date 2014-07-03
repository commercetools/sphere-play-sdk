package instantiating;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.client.model.*;
import io.sphere.client.shop.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;

public class ProductInstantiatingTest {

    @Test
    public void product() throws Exception {
        final LocalizedString localizedString = new LocalizedString(Locale.ENGLISH, "foo");
        final Price price = new Price(Money.createFromCentAmount(500L, "EUR"), CountryCode.DE, EmptyReference.<CustomerGroup>create("customerGroup"));
        final Variant masterVariant = Variant.create(1, "sku", Arrays.asList(price), Collections.<Image>emptyList(), Collections.<Attribute>emptyList(), VariantAvailability.create(true, 0));
        final Product product = new Product(VersionedId.create("id", 1), localizedString, localizedString, localizedString, localizedString, localizedString, localizedString, masterVariant, Collections.<Variant>emptyList(), Collections.<Category>emptyList(), Collections.<Reference<Catalog>>emptySet(), EmptyReference.<Catalog>create("catalog"), ReviewRating.empty(), EmptyReference.<TaxCategory>create("taxCategory"));
        assertThat(product.getId()).isEqualTo("id");
    }
}
