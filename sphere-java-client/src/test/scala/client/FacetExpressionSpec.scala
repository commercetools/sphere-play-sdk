package de.commercetools.sphere.client

import de.commercetools.sphere.client.facets.expressions.FacetExpressions._
import de.commercetools.sphere.client.facets.expressions._
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import TestUtil._
import scala.collection.JavaConverters._
import java.util

class FacetExpressionSpec extends WordSpec with MustMatchers {
  /** Converts QueryParams to a tuples for easier asserts. */
  def params(facet: FacetExpression): List[(String, String)] = {
    facet.createQueryParams().asScala.toList.map(param => (param.getName, param.getValue))
  }
  /** Converts a single QueryParams to a tuple for easier asserts. */
  def param(facet: FacetExpression): (String, String) = {
    val ps = params(facet)
    if (ps.length != 1) throw new AssertionError("Facet creates more than one query parameter.")
    ps.head
  }

  private def emptyList = new util.ArrayList[String]()

  "Terms facet" in {
    param(new StringAttribute.Terms("a")) must be ("facet", "a")
    param(new NumberAttribute.Terms("a")) must be ("facet", "a")
    param(new MoneyAttribute.Terms("a")) must be ("facet", "a")
    param(new DateAttribute.Terms("a")) must be ("facet", "a")
    param(new TimeAttribute.Terms("a")) must be ("facet", "a")
    param(new DateTimeAttribute.Terms("a")) must be ("facet", "a")
    param(new Price.Terms()) must be ("facet", "variants.price")
    param(new Categories.Terms()) must be ("facet", "categories.id")
  }

  "StringAttribute facets" should {
    "StringAttribute.TermsMultiSelect" in {
      params(new StringAttribute.TermsMultiSelect("a", "sel1", "sel2")) must be (
        List(("facet","a"), ("filter","a:\"sel1\",\"sel2\""), ("filter.facets","a:\"sel1\",\"sel2\"")))
    }
    "StringAttribute.Values" in {
      params(new StringAttribute.Values("a", emptyList)) must be (List())
      params(new StringAttribute.Values("a", "v1", "v2")) must be (List(("facet","a:\"v1\" as a-:-v1"), ("facet","a:\"v2\" as a-:-v2")))
    }
    "StringAttribute.ValuesMultiSelect" in {
      params(new StringAttribute.ValuesMultiSelect("a", emptyList, emptyList)) must be (List())
      params(new StringAttribute.ValuesMultiSelect("a", emptyList, "v1")) must be (List(("facet","a:\"v1\" as a-:-v1")))
      // selected value that's not included in values
      params(new StringAttribute.ValuesMultiSelect("a", lst("sel1"), emptyList)) must be (List(("filter","a:\"sel1\""), ("filter.facets","a:\"sel1\"")))
    }
  }
}