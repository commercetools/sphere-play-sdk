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
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}

class AttributeIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()
  val locale = Locale.ENGLISH
  def lEnum(key: String, label: String) = new LocalizableEnum(key, label.toEnLoc)

  "sphere client" must {
    "read attributes which are localized strings" in {
      val product = client.products.bySlug(locale, "product-with-localized-enum-1401098281104").fetch.get
      val attribute = product.getAttribute("locen")
      attribute must not be(null)
      attribute.getLocalizableEnum must be(new LocalizableEnum("third", new LocalizedString(locale, "third key")))
      attribute.getLocalizableEnum.getLabel.get(locale) must be("third key")
    }

    "read attributes which are sets" must {
      implicit val product = client.products.bySlug(locale, "product-with-set-attribute-localized-enum-1401275197495").fetch.get

      "read LocalizableEnum" in {
        val expected = ImmutableSet.of(lEnum("first", "first label en"), lEnum("second", "second label en"))
        attributeMustBe(expected, "setattributelocenum", classOf[LocalizableEnum])
      }

      "read LocalizedString" in {
        val expected = ImmutableSet.of("foo bar localized string", "second loc string", "third loc string").toEnLocSet
        attributeMustBe(expected, "setattributelocstring", classOf[LocalizedString])
      }

      "read text" in {
        val expected = ImmutableSet.of("text content 1", "text content 2")
        attributeMustBe(expected, "SETATTRIBUTETEXT", classOf[String])
      }

      "read enum" in {
        val expected = ImmutableSet.of(new Attribute.Enum("f1", "f1"), new Attribute.Enum("f2", "f2"))
        attributeMustBe(expected, "setattributeenum", classOf[Attribute.Enum])
      }

      "read Integer" in {
        val expected = ImmutableSet.of(new Integer(21), new Integer(42))
        attributeMustBe(expected, "SETATTRIBUTENUMBER", classOf[Integer])
      }

      "read Double" in {
        val expected = ImmutableSet.of(new JDouble(5), new JDouble(11.2))
        attributeMustBe(expected, "SETATTRIBUTENUMBERDOUBLE", classOf[JDouble])
      }

      "read Money" in {
        val expected = ImmutableSet.of("500.55" EUR, "2.23" EUR)
        attributeMustBe(expected, "SETATTRIBUTEMONEY", classOf[Money])
      }

      "read DateTime from date" in {
        val expected = ImmutableSet.of("2014-06-02T00:00:00.000+02:00" toDateTime, "2014-06-03T00:00:00.000+02:00" toDateTime)
        attributeMustBe(expected, "setattributedate", classOf[DateTime])
      }

      "read DateTime from time" in {
        def formatted(dateTime: DateTime): String = {
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")
            dateTimeFormatter.print(dateTime)
        }
        val expected = ImmutableSet.of("15:47:16", "15:47:33")
        val attribute: Attribute = product.getAttribute("setattributetime")
        val actual = attribute.getSet(classOf[DateTime]).map(formatted).asJava
        actual must be(expected)
      }

      "read DateTime from date time" in {
        val expected = ImmutableSet.of("2014-06-02T15:47:16.000+02:00" toDateTime, "2014-06-03T15:47:24.000+02:00" toDateTime)
        attributeMustBe(expected, "setattributedatetim", classOf[DateTime])
      }
    }
  }

  def attributeMustBe[T](expected: java.util.Set[T], attributeName: String, clazz: Class[T])(implicit product: Product) {
    val attribute: Attribute = product.getAttribute(attributeName.toLowerCase)
    val actual: ImmutableSet[T] = attribute.getSet(clazz)
    actual must be(expected)
  }
}
