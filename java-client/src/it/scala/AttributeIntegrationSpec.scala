package sphere

import org.scalatest._
import io.sphere.client.shop.model._
import io.sphere.client.shop._
import java.util.Locale
import io.sphere.client.model.{Money, LocalizedString}
import com.google.common.collect.{Sets, Maps, ImmutableSet}
import sphere.IntegrationTest.Implicits._
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import java.lang.{Double => JDouble}
import java.math.{BigDecimal => JBigDecimal, BigInteger}
import org.joda.time.{LocalTime, DateTime}
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import scala.reflect.ClassTag
import com.google.common.base.Optional

class AttributeIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()
  val locale = Locale.ENGLISH
  def lEnum(key: String, label: String) = new LocalizableEnum(key, label.toEnLoc)

  "sphere client" must {
    "read attributes which are localized strings" in {
      val product = client.products.bySlug(locale, "product-with-localized-string-1401098281104").fetch.get
      val attribute = product.getAttribute("locstring")
      attribute must not be(null)
      attribute.getLocalizedString must be(new LocalizedString(locale, "string"))
      attribute.getLocalizedString.get(locale) must be("string")
    }

    "read attributes which are localized enums" in {
      val product = client.products.bySlug(locale, "product-with-localized-enum-1401098281104").fetch.get
      val attribute = product.getAttribute("locen")
      attribute must not be(null)
      attribute.getLocalizableEnum must be(new LocalizableEnum("third", new LocalizedString(locale, "third key")))
      attribute.getLocalizableEnum.getLabel.get(locale) must be("third key")
    }

    "read attributes which are booleans" in {
      val product = client.products.bySlug(locale, "product-with-boolean-2").fetch.get
      val attribute = product.getMasterVariant.getAttribute("boolean-attribute")
      attribute must not be(null)
      attribute.getBoolean must be(Optional.of(true))
    }

    "read attributes which are sets" must {
      implicit val product = client.products.bySlug(locale, "product-with-set-attribute-localized-enum-1401275197495").fetch.get

      "read LocalizableEnum" in {
        val expected = ImmutableSet.of(lEnum("first", "first label en"), lEnum("second", "second label en"))
        attributeMustBe(expected, "setattributelocenum")
      }

      "read LocalizedString" in {
        val expected = ImmutableSet.of("foo bar localized string", "second loc string", "third loc string").toEnLocSet
        attributeMustBe(expected, "setattributelocstring")
      }

      "read text" in {
        val expected = ImmutableSet.of("text content 1", "text content 2")
        attributeMustBe(expected, "SETATTRIBUTETEXT")
      }

      "read boolean" in {
        val product = client.products.bySlug(locale, "product-with-boolean-set").fetch.get
        val expected = ImmutableSet.of(true, false)
        val attribute: Attribute = product.getAttribute("setattributebool")
        val actual = attribute.getSet(classOf[java.lang.Boolean])
        actual must be(expected)
      }

      "read enum" in {
        val expected = ImmutableSet.of(new Attribute.Enum("f1", "f1"), new Attribute.Enum("f2", "f2"))
        attributeMustBe(expected, "setattributeenum")
      }

      "read Integer" in {
        val expected = ImmutableSet.of(new Integer(21), new Integer(42))
        attributeMustBe(expected, "SETATTRIBUTENUMBER")
      }

      "read Double" in {
        val expected = ImmutableSet.of(new JDouble(5), new JDouble(11.2))
        attributeMustBe(expected, "SETATTRIBUTENUMBERDOUBLE")
      }

      "read Money" in {
        val expected = ImmutableSet.of("500.55" EUR, "2.23" EUR)
        attributeMustBe(expected, "SETATTRIBUTEMONEY")
      }

      "read LocalDate from date" in {
        val expected = ImmutableSet.of("2014-06-02", "2014-06-03").map(_.toDate)
        attributeMustBe(expected, "setattributedate")
      }

      "read LocalTime from time" in {
        val expected = ImmutableSet.of("15:47:16", "15:47:33").map(_.toTime)
        attributeMustBe(expected, "setattributetime")
      }

      "read DateTime from date time" in {
        val expected = ImmutableSet.of("2014-06-02T15:47:16", "2014-06-03T15:47:24").map(_.toDateTime)
        attributeMustBe(expected, "setattributedatetim")
      }
    }
  }

  def attributeMustBe[T](expected: java.util.Set[T], attributeName: String)(implicit product: Product, tpe: ClassTag[T]) {
    val attribute: Attribute = product.getAttribute(attributeName.toLowerCase)
    val actual = attribute.getSet(tpe.runtimeClass)
    actual must be(expected)
  }
}
