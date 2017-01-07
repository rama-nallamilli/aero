package feature

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import org.json4s.JsonAST.{JField, JString}
import org.json4s.native.JsonMethods._
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{DoNotDiscover, FeatureSpec, GivenWhenThen, Matchers}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@DoNotDiscover
class ConsulRegistrationFeature extends FeatureSpec with GivenWhenThen with Eventually with ScalaFutures with Matchers {

  implicit val system = ActorSystem("test-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  feature("Worker registration against Consul cluster") {
    scenario("On startup the worker successfully registers with the Consul cluster") {

      Given("the application is running and consul is available")

      When("it starts up")

      Then("it should have registered itself with Consul")
      eventually {
        val request = HttpRequest(uri = "http://192.168.99.100:8500/v1/agent/services")
          .withMethod(HttpMethods.GET)

        val responseBody = Http().singleRequest(request)
          .flatMap(_.entity.toStrict(1 seconds)
          .map(_.getData().utf8String))

        val json = parse(responseBody.futureValue)
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
