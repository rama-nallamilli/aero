package feature

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import org.json4s.JsonAST.{JField, JString}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{DoNotDiscover, FeatureSpec, GivenWhenThen, Matchers}
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@DoNotDiscover
class ConsulRegistrationFeature extends FeatureSpec with GivenWhenThen with Eventually with ScalaFutures with Matchers {

  feature("Worker registration against Consul cluster") {
    scenario("On startup the worker successfully registers with the Consul cluster") {
      implicit val system = ActorSystem("test-actor-system")
      implicit val materializer = ActorMaterializer()
      implicit val executionContext: ExecutionContext = system.dispatcher
      Given("the application is running and consul is available")

      Thread.sleep(2000)
      When("it starts up")

      Then("it should have registered itself with Consul")
      eventually {
        val data = HttpRequest(uri = "http://192.168.99.100:8500/v1/agent/services")
          .withMethod(HttpMethods.GET)
          .entity.toStrict(2 seconds)
          .map(_.getData().utf8String) // TODO This is not returning the full body, investigate

        val jsonBody = data.futureValue
        val json = parse(jsonBody)
        val aeroService = json findField {
          case JField("Service", JString("aero-worker")) => true
          case _ => false
        }
        aeroService shouldBe defined
      }



    }

    scenario("On shutdown the worker de-registers itself from the Consul cluster") {}
  }

}
