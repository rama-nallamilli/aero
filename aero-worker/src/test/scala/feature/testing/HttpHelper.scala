package feature.testing

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, StatusCode, StatusCodes}
import akka.stream.ActorMaterializer
import org.scalatest.Matchers

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait HttpHelper extends Matchers {

  implicit val system = ActorSystem("test-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  def getResponseBodyFor(request: HttpRequest, expectedResponse: StatusCode = StatusCodes.OK, timeout: FiniteDuration = 1 second) = {
    val responseF =  Http().singleRequest(request)
    responseF.flatMap { response =>
      response.status shouldBe expectedResponse
      response.entity.toStrict(timeout).map(_.getData().utf8String)
    }
  }
}
