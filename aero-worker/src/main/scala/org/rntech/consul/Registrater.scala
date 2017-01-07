package org.rntech.consul

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.stream.ActorMaterializer
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

case class RegisterSuccess(uuid: String, name: String)
case class ConsulRequestFailed(statusCode: Int, msg: String)

class Registrater(consulHttpUrl: String, consulPort: Int) {

  implicit val formats = DefaultFormats
  val baseUrl = s"http://$consulHttpUrl:$consulPort/v1/agent/service"

  /*
    Registers with Consul service discovery
      @see https://www.consul.io/docs/agent/http/agent.html#agent_service_register
   */
  def registerSelf(serviceId: String, serviceHost: String, servicePort: Int, tags: Set[String] = Set.empty)(implicit system: ActorSystem, materializer: ActorMaterializer): Future[Either[ConsulRequestFailed, RegisterSuccess]] = {
    val name = "aero-worker"

    val healthEndpoint = s"http://$serviceHost:$servicePort/health"

    val json = ("ID" -> serviceId) ~
      ("Name" -> name) ~
      ("Tags" -> tags) ~
      ("Address" -> serviceHost) ~
      ("Port" -> servicePort) ~
      ("Check" -> (
        ("HTTP" -> healthEndpoint) ~
          ("DeregisterCriticalServiceAfter" -> "10m") ~
          ("Interval" -> "10s")))

    //TODO find out how to set request timeouts?
    val jsonBody = compact(render(json))
    println(s"$baseUrl/register")
    println(jsonBody)
    val httpRequest = HttpRequest(uri = s"$baseUrl/register")
      .withMethod(HttpMethods.PUT)
      .withEntity(jsonBody)

    Http().singleRequest(httpRequest).flatMap { response =>
        response.status match {
          case StatusCodes.OK =>
            Future.successful(Right(RegisterSuccess(serviceId, name)))
          case _ =>
            val responseBody = response.entity.toStrict(1 seconds).map(_.data.toString)
            responseBody.map { body =>
              Left(ConsulRequestFailed(response.status.intValue(), body))
            }
        }
      }
  }

  /*
  Deregisters with Consul service discovery
    @see https://www.consul.io/docs/agent/http/agent.html#agent_service_deregister
 */
  def deregister(serviceId: String)(implicit system: ActorSystem, materializer: ActorMaterializer) = {
    val httpRequest = HttpRequest(uri = s"$baseUrl/deregister/$serviceId")
      .withMethod(HttpMethods.PUT)

    Http().singleRequest(httpRequest).flatMap { response =>
      response.status match {
        case StatusCodes.OK => Future.successful(Right(Success))
        case _ =>
          val responseBody = response.entity.toStrict(1 seconds).map(_.data.toString)
          responseBody.map { body =>
            Left(ConsulRequestFailed(response.status.intValue(), body))
          }
      }
    }
  }
}
