package sphere

import io.sphere.client.shop.model.{Price, DiscountedPrice}
import sphere.IntegrationTest.Implicits._
import org.scalatest._

class DiscountSpec extends WordSpec with MustMatchers {
  val client = IntegrationTestClient()

  "sphere client" must {
    "retrieve product discounts" in {
      val productWithDiscount = client.products.bySlug("girls-hartbreak-crew1381415075541").fetch.get
      val price: Price = productWithDiscount.getPrice
      val discounted: DiscountedPrice = price.getDiscounted.get
      discounted.getValue must be ("30.60" EUR)
      price.getValue must be ("34.00" EUR)
    }
  }
}