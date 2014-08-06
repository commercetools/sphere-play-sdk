package shop

import io.sphere.client.TestUtil
import TestUtil.eur
import com.google.common.base.Optional
import io.sphere.client.shop.model.{DiscountedPrice, LineItem, Price}
import org.scalatest._

class LineItemSpec extends WordSpec with MustMatchers  {

  val originalMoney = eur(200)
  val discountedMoney = eur(100)

  "Price uses original price when is not discounted" in {
    val p = new Price(originalMoney, null, null, Optional.absent())
    val li = LineItem.create(null, null, null, null, 2, p, null, null)
    li.getUnitPrice must be (p.getValue)
    li.getTotalPrice must be (p.getValue.multiply(2))
  }

  "Price uses discounted price when is discounted" in {
    val dp = DiscountedPrice.create(discountedMoney, null)
    val p = new Price(originalMoney, null, null, Optional.of(dp))
    val li = LineItem.create(null, null, null, null, 2, p, null, null)
    li.getUnitPrice must be (p.getDiscounted.get().getValue)
    li.getTotalPrice must be (p.getDiscounted.get().getValue.multiply(2))
  }
}
