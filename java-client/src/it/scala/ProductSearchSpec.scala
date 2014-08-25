package sphere

import IntegrationTest.Implicits._
import io.sphere.client.ProductSort
import org.scalatest._
import io.sphere.client.shop.model.{Attribute, ProductUpdate}

import scala.util.Try

class ProductSearchSpec extends WordSpec with MustMatchers {
  val client = IntegrationTestClient()

  "sphere client" must {
    import scala.collection.JavaConversions._
    def fetchProducts(sort: ProductSort) = client.products().all().sort(sort).fetch()

    "sort products asc" in {
      val searchResult = fetchProducts(ProductSort.name.asc)
      searchResult.getCount must be >= (2)
      val names = searchResult.getResults.map(_.getName(locale))
      names must be (names.sorted)
    }

    "sort products desc" in {
      val searchResult = fetchProducts(ProductSort.name.desc)
      searchResult.getCount must be >= (2)
      val names = searchResult.getResults.map(_.getName(locale))
      names must be (names.sorted.reverse)
    }
    
    "set and remove attribute" in {
      def fetchProduct = client.products.byId("8dab3562-1778-44f9-b4f9-548184081bc3").fetch.get

      val attributeName = "custom-attribute"
      val attribute = new Attribute(attributeName, "value1")

      def deleteAttribute() = {
        val product = fetchProduct
        val cmd = new ProductUpdate().removeAttribute(product.getMasterVariant.getId, attributeName, false)
        client.products().updateProduct(product.getIdAndVersion, cmd).execute
      }

      def setAttribute() = {
        val product = fetchProduct
        val cmd = new ProductUpdate().setAttribute(product.getMasterVariant.getId, attribute, false)
        client.products().updateProduct(product.getIdAndVersion, cmd).execute()
      }

      Try(deleteAttribute())

      info("set attribute")
      val attributeSet = setAttribute()
      attributeSet.getAttribute(attributeName) must be (attribute)

      info("remove attribute")
      val attributeUnset = deleteAttribute()
      attributeUnset.getAttribute(attributeName) must be (null)
    }
  }
}