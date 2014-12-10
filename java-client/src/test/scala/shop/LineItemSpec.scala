package shop

import java.util.Locale

import com.google.common.collect.ImmutableMap
import io.sphere.client.TestUtil
import TestUtil.eur
import com.google.common.base.Optional
import io.sphere.client.model.LocalizedString
import io.sphere.client.shop.model.{DiscountedPrice, LineItem, Price}
import org.scalatest._

class LineItemSpec extends WordSpec with MustMatchers  {

  val originalMoney = eur(200)
  val discountedMoney = eur(100)

  "Gets original price when is not discounted" in {
    val p = new Price(originalMoney, null, null, Optional.absent())
    val li = LineItem.create(null, null, null, null, 2, p, Optional.absent(), null, null)
    li.getUnitPrice must be (p.getValue)
    li.getTotalPrice must be (p.getValue.multiply(2))
  }

  "Gets discounted price when is discounted" in {
    val dp = DiscountedPrice.create(discountedMoney, null)
    val p = new Price(originalMoney, null, null, Optional.of(dp))
    val li = LineItem.create(null, null, null, null, 2, p, Optional.absent(), null, null)
    li.getUnitPrice must be (p.getDiscounted.get().getValue)
    li.getTotalPrice must be (p.getDiscounted.get().getValue.multiply(2))
  }

  "Gets original price before discount" in {
    val dp = DiscountedPrice.create(discountedMoney, null)
    val p = new Price(originalMoney, null, null, Optional.of(dp))
    val li = LineItem.create(null, null, null, null, 2, p, Optional.absent(), null, null)
    li.getUnitPriceBeforeDiscount must be (p.getValue)
    li.getTotalPriceBeforeDiscount must be (p.getValue.multiply(2))
  }

  "Gets localized name" in {
    val ls = new LocalizedString(ImmutableMap.of(Locale.ENGLISH, "english-name", Locale.GERMAN, "german-name"))
    val li = LineItem.create(null, null, ls, null, 1, null, Optional.absent(), null, null)
    li.getProductName(Locale.ENGLISH) must be("english-name")
    li.getProductName(Locale.GERMAN) must be("german-name")
    li.getProductName(Locale.FRENCH) must be("")
  }
}
