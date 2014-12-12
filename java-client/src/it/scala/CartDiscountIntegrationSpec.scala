package sphere

import java.util.Arrays.asList

import io.sphere.client.exceptions.{SphereBackendException}
import io.sphere.client.model.Reference
import io.sphere.client.shop.SphereClient
import io.sphere.client.shop.model.{DiscountCode, DiscountCodeState, DiscountCodeWithState, CartUpdate}
import org.scalatest._
import Fixtures._
import com.google.common.base.Optional

class CartDiscountIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()
  val AbsoluteDiscountCode = "absolute discount code"

  "sphere client" must {
     "read relative cart discounts with 10% off" in {
       val cart = newCartWithLineItems

       val cartUpdate = new CartUpdate().addDiscountCode("relative discount code")
       val updatedCart = client.carts.updateCart(cart.getIdAndVersion, cartUpdate).execute()
       updatedCart.getTotalPrice.getAmount.doubleValue must be((cart.getTotalPrice.getAmount.doubleValue * 0.9) +- 0.1)
       updatedCart.getDiscountCodes must be(asList(new DiscountCodeWithState(Optional.of(DiscountCodeState.MatchesCart),
         Reference.create[DiscountCode]("discount-code", "caf7023c-228f-4a2d-9354-e6e3c15a4898"))))
       cart.getLineItems.get(0).getDiscountedPrice must be(Optional.absent())
       (updatedCart.getLineItems.get(0).getDiscountedPrice.get.getValue.getCentAmount * 10) must
         be(updatedCart.getLineItems.get(0).getPrice.getValue.getCentAmount * 9)
     }

    "read absolute cart discounts with 2 EUR off and remove it again" in {
      val cart = newCartWithLineItems
      val cartUpdate = new CartUpdate().addDiscountCode(AbsoluteDiscountCode)
      val updatedCart = client.carts.updateCart(cart.getIdAndVersion, cartUpdate).execute()
      updatedCart.getTotalPrice.getCentAmount must be(cart.getTotalPrice.getCentAmount - 200)

      val discountCodeRef = updatedCart.getDiscountCodes.get(0).getDiscountCode
      val removeDiscountAction = new CartUpdate().removeDiscountCode(discountCodeRef)
      val cartWithRemovedDiscountCode = client.carts.updateCart(updatedCart.getIdAndVersion, removeDiscountAction).execute()
      cartWithRemovedDiscountCode.getTotalPrice.getCentAmount must be(cart.getTotalPrice.getCentAmount)
    }
    
    "case invalid discount code" in {
      val cart = newCartWithLineItems
      val cartUpdate = new CartUpdate().addDiscountCode("NONSENSE")
      intercept[SphereBackendException] {
        client.carts.updateCart(cart.getIdAndVersion, cartUpdate).execute()
      }
    }
  }
}