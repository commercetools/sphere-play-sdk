package sphere

import com.google.common.base.Optional
import io.sphere.client.shop.SphereClient
import io.sphere.client.shop.model.CartUpdate
import org.scalatest._
import sphere.Fixtures._


class CartIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()

  "sphere client" must {
     "handle absent carts" in {
       client.carts().byId("not-present").fetch() must be (Optional.absent())
     }

    "set an existing customer ID on a cart" in {
      val cart = newCart
      val customer = newCustomer

      val setCustomerIdUpdate = new CartUpdate().setCustomerId(customer.getId)
      val cartWithCustomerId = client.carts().updateCart(cart.getIdAndVersion, setCustomerIdUpdate).execute()
      cartWithCustomerId.getCustomerId must be (customer.getId)

      info("unset the customer ID")
      val unsetCustomerIdUpdate = new CartUpdate().setCustomerId(null)
      val cartWithoutCustomerId = client.carts().updateCart(cartWithCustomerId.getIdAndVersion, unsetCustomerIdUpdate).execute()
      cartWithoutCustomerId.getCustomerId must be (empty)
    }

    "fail when settings a non-existing customer ID on a cart" in {
      val cart = newCart

      val setCustomerIdUpdate = new CartUpdate().setCustomerId("non-existing-customer-id")

      an [Exception] must be thrownBy {
        client.carts().updateCart(cart.getIdAndVersion, setCustomerIdUpdate).execute()
      }
    }

  }
}