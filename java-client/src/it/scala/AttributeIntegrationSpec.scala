package sphere

import org.scalatest._
import io.sphere.client.shop.model._
import io.sphere.client.shop._
import java.util.Locale
import io.sphere.client.model.{Money, LocalizedString}
import com.google.common.collect.{ImmutableMap, Sets, Maps, ImmutableSet}
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
  def lEnum(key: String, label: LocalizedString) = new LocalizableEnum(key, label)

  "sphere client" must {
    implicit val product = client.products.bySlug(locale, "attribute-integration-spec-1407335245768").fetch.get

    "read attributes which are localized enums" in {
      val attribute = product.getAttribute("loc-enum-attribute")
      attribute must not be null
      val localizedString = new LocalizedString(Locale.ENGLISH, "two-label-en", Locale.GERMAN, "two-label-de")
      attribute.getLocalizableEnum must be(new LocalizableEnum("two-key", localizedString))
      attribute.getLocalizableEnum.getLabel.get(locale) must be("two-label-en")
    }

    "read attributes which are localized strings" in {
      val attribute = product.getAttribute("loc-string-attribute")
      attribute must not be null
      val localizedString = new LocalizedString(Locale.ENGLISH, "val-loc-string-en", Locale.GERMAN, "val-loc-string-de")
      attribute.getLocalizedString must be(localizedString)
      attribute.getLocalizedString.get(locale) must be("val-loc-string-en")
    }

    "read attributes which are booleans" in {
      val attribute = product.getAttribute("boolean-attribute")
      attribute must not be null
      attribute.getBoolean must be(Optional.of(true))
    }

    "read attributes which are numbers" in {
      val attribute = product.getAttribute("number-attribute")
      attribute must not be null
      attribute.getDouble must be(new JDouble(2.5))
    }

    "read attributes which are sets" must {

      "read LocalizableEnum" in {
        val localizedString1 = new LocalizedString(Locale.ENGLISH, "two-label-en", Locale.GERMAN, "two-label-de")
        val localizedString2 = new LocalizedString(Locale.ENGLISH, "three-label-en", Locale.GERMAN, "three-label-de")
        val expected = ImmutableSet.of(lEnum("two-key", localizedString1), lEnum("three-key", localizedString2))
        attributeMustBe(expected, "set-loc-enum-attribute")
      }

      "read LocalizedString" in {
        val localizedString1 = new LocalizedString(Locale.ENGLISH, "two-set-string-en", Locale.GERMAN, "two-set-string-de")
        val localizedString2 = new LocalizedString(Locale.ENGLISH, "three-set-string-en", Locale.GERMAN, "three-set-string-de")
        val expected = ImmutableSet.of(localizedString1, localizedString2)
        attributeMustBe(expected, "set-loc-string-attribute")
      }

      "read text" in {
        val expected = ImmutableSet.of("two-set-string", "three-set-string")
        attributeMustBe(expected, "set-string-attribute")
      }

      "read boolean" in {
        val expected = ImmutableSet.of(true, false)
        val actual = product.getAttribute("set-boolean-attribute").getSet(classOf[java.lang.Boolean])
        actual must be(expected)
      }

      "read enum" in {
        val expected = ImmutableSet.of(new Attribute.Enum("one-key", "one-label"), new Attribute.Enum("two-key", "two-label"))
        attributeMustBe(expected, "set-enum-attribute")
      }

      "read Number" in {
        val expected = ImmutableSet.of(new JDouble(2.5), new JDouble(3.75), new JDouble(4))
        attributeMustBe(expected, "set-number-attribute")
      }

      "read Money" in {
        val expected = ImmutableSet.of("2.50" EUR, "3.75" EUR, "4.00" EUR)
        attributeMustBe(expected, "set-money-attribute")
      }

      "read LocalDate from date" in {
        val expected = ImmutableSet.of("2014-08-06", "2014-08-07").map(_.toDate)
        attributeMustBe(expected, "set-date-attribute")
      }

      "read LocalTime from time" in {
        val expected = ImmutableSet.of("18:20:49", "17:24:31").map(_.toTime)
        attributeMustBe(expected, "set-time-attribute")
      }

      "read DateTime from date time" in {
        val expected = ImmutableSet.of("2014-08-06T16:27:05", "2014-08-06T16:38:59").map(_.toDateTime)
        attributeMustBe(expected, "set-date-time-attribute")
      }
    }
  }

  def attributeMustBe[T](expected: java.util.Set[T], attributeName: String)(implicit product: Product, tpe: ClassTag[T]) {
    val attribute: Attribute = product.getAttribute(attributeName.toLowerCase)
    val actual = attribute.getSet(tpe.runtimeClass)
    actual must be(expected)
  }
}
