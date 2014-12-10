package instantiating;

import com.google.common.base.Optional;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.client.model.EmptyReference;
import io.sphere.client.model.LocalizedString;
import io.sphere.client.model.Money;
import io.sphere.client.shop.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static com.neovisionaries.i18n.CountryCode.DE;
import static org.fest.assertions.Assertions.assertThat;

public class CartItemsInstantiationTest {

    public static final String ID = "id";
    public static final String PRODUCT_ID = "product-id";
    public static final LocalizedString LOCALIZED_STRING = new LocalizedString(Locale.ENGLISH, "foo");
    public static final TaxRate TAX_RATE = TaxRate.create("name", 0.19, true, DE, "BER");
    public static final Money MONEY = Money.createFromCentAmount(500L, "EUR");

    @Test
    public void lineItem() throws Exception {
        final Price price = new Price(MONEY, CountryCode.DE, EmptyReference.<CustomerGroup>create("customerGroup"));
        final Variant masterVariant = Variant.create(1, "sku", Arrays.asList(price), Collections.<Image>emptyList(), Collections.<Attribute>emptyList(), VariantAvailability.create(true, 0));
        final LineItem lineItem = LineItem.create(ID, PRODUCT_ID, LOCALIZED_STRING, masterVariant, 5, price, Optional.<DiscountedLineItemPrice>absent(), TAX_RATE, Channel.emptyReference());
        assertThat(lineItem.getId()).isEqualTo(ID);
    }

    @Test
    public void customLineItem() throws Exception {
        final CustomLineItem customLineItem = CustomLineItem.create(ID, LOCALIZED_STRING, MONEY, "slug", 10, TAX_RATE);
        assertThat(customLineItem.getId()).isEqualTo(ID);
    }
}
