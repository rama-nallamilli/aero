package feature.testing

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse
import org.rntech.entrypoint.Config
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures

trait HealthCheck extends Matchers with ScalaFutures {
  this: HttpHelper =>

  private implicit val formats = DefaultFormats

  def applicationIsHealthy() = {
    val request = HttpRequest(uri = s"http://localhost:${Config.servicePort}/health")
      .withMethod(HttpMethods.GET)

    val responseBody = getResponseBodyFor(request)

    val json = parse(responseBody.futureValue)
    (json \ "status").extract[String] shouldBe "ok"
  }
}
