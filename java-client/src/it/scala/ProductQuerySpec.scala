package sphere

import com.google.common.base.Optional
import org.scalatest._

class ProductQuerySpec extends WordSpec with MustMatchers {
  val client = IntegrationTestClient()

  "sphere client" must {
    "must handle absent products" in {
      client.products.byId("not-present").fetch() must be (Optional.absent())
    }
  }
}