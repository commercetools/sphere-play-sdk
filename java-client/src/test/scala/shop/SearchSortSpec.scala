package shop

import java.util.Locale

import io.sphere.client.{ProductSort, MockRequestHolder, SearchRequest}
import io.sphere.client.model.SearchResult
import io.sphere.client.model.products.BackendProductProjection
import io.sphere.internal.request.SearchRequestImpl
import org.codehaus.jackson.`type`.TypeReference
import org.scalatest._

class SearchSortSpec extends WordSpec with MustMatchers {
  "sorting by String" in {
    val x = newSearchRequest.sort("variants.attributes.foo desc")
    urlOf(x) must be ("base?sort=variants.attributes.foo+desc")
  }

  "sorting by DSL" in {
    val x = newSearchRequest.sort(ProductSort.attributes.localizableEnumLabel("bar", "de").desc)
    urlOf(x) must be ("base?sort=variants.attributes.bar.label.de+desc")
  }

  def newSearchRequest: SearchRequest[BackendProductProjection] = {
    val a = new MockRequestHolder[SearchResult[BackendProductProjection]]("base", "GET", 200, "{}")
    val request: SearchRequest[BackendProductProjection] = new SearchRequestImpl(a, new TypeReference[SearchResult[BackendProductProjection]]() {}, Locale.ENGLISH)
    request
  }

  def urlOf(x: SearchRequest[BackendProductProjection]): String = {
    x.asInstanceOf[SearchRequestImpl[BackendProductProjection]].getRequestHolder.getUrl
  }
}
