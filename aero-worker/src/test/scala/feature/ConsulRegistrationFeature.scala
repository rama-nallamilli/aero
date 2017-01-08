package feature

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import feature.testing.{HealthCheck, HttpHelper}
import org.json4s.JsonAST.{JField, JString}
import org.json4s.native.JsonMethods._
import org.rntech.entrypoint.Config
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.time.{Millis, Second, Span}
import org.scalatest.{DoNotDiscover, FeatureSpec, GivenWhenThen, Matchers}

@DoNotDiscover
class ConsulRegistrationFeature extends FeatureSpec with GivenWhenThen with Eventually with ScalaFutures
  with Matchers with HttpHelper with HealthCheck {

  implicit val defaultPatience = PatienceConfig(timeout = Span(1, Second), interval = Span(100, Millis))

  feature("Worker registration against Consul cluster") {
    scenario("On startup the worker successfully registers with the Consul cluster") {
      Given("the application is running and consul is available")
      eventually {
        applicationIsHealthy()
      }

      Then("it should have registered itself with Consul")
      eventually {
        val request = HttpRequest(uri = s"http://${Config.consulHost}:${Config.consulPort}/v1/agent/services").withMethod(HttpMethods.GET)
        val responseBody = getResponseBodyFor(request)
        val json = parse(responseBody.futureValue)

        val aeroService = json findField {
          case JField("Service", JString("aero-worker")) => true
          case _ => false
        }

        aeroService shouldBe defined
      }
    }

    scenario("On shutdown the worker de-registers itself from the Consul cluster") {
    }
  }

}
