package sphere

import com.google.common.base.Optional
import io.sphere.client.shop.model._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import Fixtures._
import org.scalatest._
import io.sphere.client.shop.{SignUpBuilder, SphereClient}
import io.sphere.client.exceptions.EmailAlreadyInUseException
import org.joda.time.LocalDate;


class CustomerServiceIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()

  val FirstName = "Max"
  val LastName = "Mustermann"
  val Title = "Mr."
  val MiddleName = "M."
  val Password = "Asecret-123"
  val CustomerName = new CustomerName(Title, FirstName, MiddleName, LastName)

  "sphere client" must {
     "add a customer address" in {
       val customer = newCustomer
       customer.getAddresses.asScala must be(Nil)
       val updatedCustomer = client.customers().update(customer.getIdAndVersion, new CustomerUpdate().addAddress(GermanAddress)).execute()
       updatedCustomer.getAddresses.asScala.toList(0).getCountry must be(GermanAddress.getCountry)
     }

     "change one customer address of many" in {
       val customer = client.customers().update(newCustomer.getIdAndVersion, new CustomerUpdate().addAddress(GermanAddress).addAddress(FrenchAddress)).execute()
       customer.getAddresses.map(_.getCountry).toSet must be(Set(GermanAddress.getCountry, FrenchAddress.getCountry))
       val addressInFrance = customer.getAddresses.asScala.filter(_.getCountry == FrenchAddress.getCountry).head
       val updatedCustomer = client.customers().update(customer.getIdAndVersion, new CustomerUpdate().changeAddress(addressInFrance.getId, BelgianAddress)).execute()
       updatedCustomer.getAddresses.map(_.getCountry).toSet must be(Set(GermanAddress.getCountry, BelgianAddress.getCountry))
     }

    def testSignup(builder: SignUpBuilder) = {
      val signInResult = client.customers.signUp(builder).execute()
      signInResult.getCustomer.getEmail must be (builder.getEmail)
      signInResult.getCustomer.getName must be (CustomerName)
      signInResult.getCustomer.isEmailVerified must be (false)
      signInResult
    }

    "sign up a customer with customer number" in {
      val email = Fixtures.randomEmail()
      val customerNumber = email.toUpperCase
      val builder = new SignUpBuilder(email, Password, CustomerName).setCustomerNumber(customerNumber)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getCustomerNumber must be (customerNumber)
    }

    "sign up a customer with external id" in {
      val email = Fixtures.randomEmail()
      val externalId = email.toUpperCase
      val builder = new SignUpBuilder(email, Password, CustomerName).setExternalId(externalId)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getExternalId must be (externalId)
    }

    "sign up a customer" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      testSignup(builder)
    }

    "sign up a customer with an anonymous cart" in {
      val email = Fixtures.randomEmail()
      val cart = Fixtures.newCartWithProduct
      cart.getCustomerId must be ("")//cart is not bound to a customer
      cart.getId must not be (null)
      cart.getId must not be ("")
      val builder = new SignUpBuilder(email, Password, CustomerName).setAnonymousCartId(cart.getId)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getName must be (CustomerName)
      signInResult.getCart.getId must be (cart.getId)
      signInResult.getCart.getLineItems()(0).getId must be(cart.getLineItems()(0).getId)
      signInResult.getCart.getLineItems()(0).getQuantity must be(cart.getLineItems()(0).getQuantity)
    }

    "handle error when customer email already exists in signup" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      testSignup(builder)
      intercept[EmailAlreadyInUseException](testSignup(builder))
    }

    "update the externalId of a customer" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getExternalId must be ("")
      val externalId = Fixtures.randomString()
      client.customers.update(signInResult.getCustomer.getIdAndVersion, new CustomerUpdate().setExternalId(externalId)).execute()
      client.customers.byId(signInResult.getCustomer.getId).fetch.get.getExternalId must be (externalId)
    }

    "update the companyName of a customer" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getCompanyName must be ("")
      val companyName = Fixtures.randomString()
      client.customers.update(signInResult.getCustomer.getIdAndVersion, new CustomerUpdate().setCompanyName(companyName)).execute()
      client.customers.byId(signInResult.getCustomer.getId).fetch.get.getCompanyName must be (companyName)
    }

    "update the vatlId of a customer" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getVatId must be ("")
      val vatId = Fixtures.randomString()
      client.customers.update(signInResult.getCustomer.getIdAndVersion, new CustomerUpdate().setVatId(vatId)).execute()
      client.customers.byId(signInResult.getCustomer.getId).fetch.get.getVatId must be (vatId)
    }

    "update the dateOfBirth of a customer" in {
      val builder = new SignUpBuilder(Fixtures.randomEmail(), Password, CustomerName)
      val signInResult = testSignup(builder)
      signInResult.getCustomer.getDateOfBirth must be (Optional.absent())
      val dateOfBirth = new LocalDate()
      client.customers.update(signInResult.getCustomer.getIdAndVersion, new CustomerUpdate().setDateOfBirth(dateOfBirth)).execute()
      client.customers.byId(signInResult.getCustomer.getId).fetch.get.getDateOfBirth.get must be (dateOfBirth)
    }

    "return Optional.absent() if a customer cannot be found by id" in {
      client.customers().byId("not-present").fetch() must be (Optional.absent())
    }
  }
}