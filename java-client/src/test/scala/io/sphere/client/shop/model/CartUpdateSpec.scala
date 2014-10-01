package io.sphere.client.shop.model

import java.util.Arrays.asList

import com.neovisionaries.i18n.CountryCode
import io.sphere.internal.command.CartCommands
import org.scalatest._

class CartUpdateSpec extends FunSuite with MustMatchers {

  test("actions in cartupdate are available"){
    val update = new CartUpdate().addLineItem(3, "product-id-a").setCountry(CountryCode.DE)
    update.getActions must be (asList(
      new CartCommands.AddLineItemFromMasterVariant("product-id-a", 3, null),
      new CartCommands.SetCountry(CountryCode.DE)))
  }

}
