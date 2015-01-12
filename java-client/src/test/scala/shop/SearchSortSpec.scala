package shop

import java.util.Locale

import io.sphere.client.{MockRequestHolder, SearchRequest}
import io.sphere.client.model.SearchResult
import io.sphere.client.model.products.BackendProductProjection
import io.sphere.internal.request.SearchRequestImpl
import org.codehaus.jackson.`type`.TypeReference
import org.scalatest._

class SearchSortSpec extends WordSpec with MustMatchers {
  "sorting by String" in {
    val a =  new MockRequestHolder[SearchResult[BackendProductProjection]]("base", "GET", 200, "{}")
    val request: SearchRequest[BackendProductProjection] = new SearchRequestImpl(a, new TypeReference[SearchResult[BackendProductProjection]]() {}, Locale.ENGLISH)
    val x = request.sort("variants.attributes.foo desc")
    x.asInstanceOf[SearchRequestImpl[BackendProductProjection]].getRequestHolder.getUrl must be ("base?sort=variants.attributes.foo+desc")
  }
}
