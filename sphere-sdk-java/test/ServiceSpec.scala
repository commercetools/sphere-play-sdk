package sphere

import java.util.{Currency, UUID}

import sphere.testobjects.{TestOrder, TestCart, TestCustomer}
import de.commercetools.sphere.client.shop.model.Address
import de.commercetools.sphere.client.shop.{Orders, Carts, CustomerService, LoginResult}
import de.commercetools.sphere.client.{QueryRequest, CommandRequest, MockListenableFuture}
import de.commercetools.internal.ListenableFutureAdapter

import org.scalatest.{BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers
import org.scalamock.scalatest.MockFactory
import org.scalamock.ProxyMockFactory
import play.mvc.Http
import de.commercetools.sphere.client.model.QueryResult

abstract class ServiceSpec extends WordSpec
with MustMatchers
with BeforeAndAfterEach
with MockFactory
with ProxyMockFactory {

  val testId = UUID.randomUUID().toString
  val testCustomerId = UUID.randomUUID().toString
  val testCartId = UUID.randomUUID().toString
  lazy val initialCustomer = TestCustomer(testCustomerId, 1)
  lazy val resultCustomer = TestCustomer(testCustomerId, 2)
  val testAddress = new Address("Alexanderplatz")
  lazy val testCart = TestCart(testCartId, 1)
  val initialTestCart = TestCart(testCartId, 1)
  val resultTestCart = TestCart(testCartId, 2)
  lazy val loginResultNoCart = new LoginResult(resultCustomer, null)
  lazy val loginResultWithCart = new LoginResult(resultCustomer, resultTestCart)

  lazy val emptyMap = new java.util.HashMap[java.lang.String,java.lang.String]()
  lazy val EUR = Currency.getInstance("EUR")

  def customerServiceExpecting[A: Manifest](expectedMethodCall: Symbol, methodArgs: List[Any], methodResult: A = resultCustomer): CustomerService = {
    val mockedFuture = MockListenableFuture.completed(methodResult)
    val future = new ListenableFutureAdapter(mockedFuture)
    val commandRequest = mock[CommandRequest[A]]
    commandRequest expects 'executeAsync returning future
    val customerService = mock[CustomerService]
    customerService expects expectedMethodCall withArgs (methodArgs:_*) returning commandRequest
    customerService
  }

  def customerServiceQueryExpecting[A](expectedMethodCall: Symbol, methodArgs: List[Any], methodResult: A = resultCustomer): CustomerService = {
    val mockedFuture = MockListenableFuture.completed(methodResult)
    val future = new ListenableFutureAdapter(mockedFuture)
    val queryRequest = mock[QueryRequest[A]]
    queryRequest expects 'fetchAsync returning future
    val customerService = mock[CustomerService]
    customerService expects expectedMethodCall withArgs (methodArgs:_*) returning queryRequest
    customerService
  }

  def cartServiceExpecting[A](expectedMethodCall: Symbol, methodArgs: List[Any], methodResult: A = resultTestCart): Carts = {
    val mockedFuture = MockListenableFuture.completed(methodResult)
    val future = new ListenableFutureAdapter(mockedFuture)
    val commandRequest = mock[CommandRequest[A]]
    commandRequest expects 'executeAsync returning future
    val cartService = mock[Carts]
    cartService expects expectedMethodCall withArgs (methodArgs:_*) returning commandRequest
    cartService
  }

  def orderServiceQueryExpecting[A](expectedMethodCall: Symbol, methodArgs: List[Any], methodResult: A = TestOrder): Orders = {
    import scala.collection.JavaConversions._
    val mockedFuture = MockListenableFuture.completed(new QueryResult(0, 1, 1, List(methodResult)))
    val future = new ListenableFutureAdapter(mockedFuture)
    val queryRequest = mock[QueryRequest[A]]
    queryRequest expects 'fetchAsync returning future
    val orderService = mock[Orders]
    orderService expects expectedMethodCall withArgs (methodArgs:_*) returning queryRequest
    orderService
  }

  //TODO refactor the serviceExpecting methods

  override def beforeEach()  {
    Http.Context.current.set(new Http.Context(null, emptyMap, emptyMap))
  }
}