package sphere

import com.google.common.base.Optional
import io.sphere.client.shop.SphereClient
import org.scalatest._


class CartIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()

  "sphere client" must {
     "handle absent carts" in {
       client.carts().byId("not-present").fetch() must be (Optional.absent())
     }
  }
}