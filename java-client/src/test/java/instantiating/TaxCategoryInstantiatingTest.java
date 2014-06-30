package instantiating;

import io.sphere.client.shop.model.TaxCategory;
import io.sphere.client.shop.model.TaxRate;
import org.junit.Test;

import java.util.Arrays;

import static com.neovisionaries.i18n.CountryCode.DE;
import static org.fest.assertions.Assertions.assertThat;

public class TaxCategoryInstantiatingTest {
    @Test
    public void taxCategory() throws Exception {
        final TaxRate taxRate = TaxRate.create("name", 0.19, true, DE, "BER");
        final TaxCategory taxCategory = TaxCategory.create("id", 1, "name", "desc", Arrays.asList(taxRate));
        assertThat(taxCategory.getId()).isEqualTo("id");
    }
}
