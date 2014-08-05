package sphere

import java.util.Arrays

import io.sphere.client.shop.model._
import sphere.IntegrationTest.Implicits._
import org.scalatest._

class DiscountSpec extends WordSpec with MustMatchers {
  val client = IntegrationTestClient()

  "sphere client" must {
    val referencePath = "masterVariant.prices[*].discounted.discount"
    "retrieve relative product discounts" in {
      val productWithDiscount = client.products.
        bySlug("girls-hartbreak-crew1381415075541").expand(referencePath).fetch.get
      val price: Price = productWithDiscount.getPrice
      price.getValue must be ("34.00" EUR)
      val discounted: DiscountedPrice = price.getDiscounted.get
      discounted.getValue must be ("30.60" EUR)
      val discount: ProductDiscount = discounted.getDiscount.get
      discount.getDescription.get must be ("descr" toEnLoc)
      discount.isActive must be (true)
      val productDiscountValue = discount.getValue.asInstanceOf[RelativeProductDiscount]
      productDiscountValue.getPermyriad must be (1000)
      productDiscountValue.getPermyriad must be (productDiscountValue.getBasisPoint)
    }

    "retrieve absolute product discounts" in {
      val productWithDiscount = client.products.
        bySlug("mb-premium-tech-t1381415075704").expand(referencePath).fetch.get
      val productDiscount = productWithDiscount.getPrice.getDiscounted.get.getDiscount.get
      val value = productDiscount.getValue.asInstanceOf[AbsoluteProductDiscount]
      value.getMoney must be (Arrays.asList("43.21" EUR))
    }
  }
}
