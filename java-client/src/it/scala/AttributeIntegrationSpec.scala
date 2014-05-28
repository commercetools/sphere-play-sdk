package sphere

import org.scalatest._
import io.sphere.client.shop.model._
import io.sphere.client.shop._
import java.util.Locale
import io.sphere.client.model.LocalizedString

class AttributeIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()

  "sphere client" must {
    "read attributes which are localized strings" in {
      val locale = Locale.ENGLISH
      val product = client.products.bySlug(locale, "product-with-localized-enum-1401098281104").fetch.get
      val attribute = product.getAttribute("locen")
      attribute must not be(null)
      attribute.getLocalizableEnum must be(new LocalizableEnum("third", new LocalizedString(locale, "third key")))
      attribute.getLocalizableEnum.getLabel.get(locale) must be("third key")
    }
  }
}
