package org.rntech.consul

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer

case class RegistratorConfig(consulHttpUrl: String)

class Registrator(consulHttpUrl: String) {

  def registerSelf(registrationId: String, name: String, hostname: String, port: Int, tags: Set[String] = Set.empty)(implicit system: ActorSystem, materializer: ActorMaterializer) = {
    val registrationEndpoint = s"$consulHttpUrl/v1/agent/service/register"

    //https://www.consul.io/docs/agent/http/agent.html#agent_service_register
    import org.json4s.JsonDSL._

    val json = ("ID" -> registrationId) ~
      ("Name" -> name) ~
      ("Tags" -> tags) ~
      ("Address" -> hostname) ~
      ("Port" -> port) ~
      ("Check" -> (
        ("HTTP" -> s"http://$hostname:$port/health") ~
          ("DeregisterCriticalServiceAfter" -> "90m") ~
          ("Interval" -> "10s")))

    val responseFuture = Http().singleRequest(HttpRequest(uri = "http://akka.io")
      .withMethod(HttpMethods.PUT)
      //   .withEntity()
    )

    //    {
    //      "ID": "redis1",
    //      "Name": "redis",
    //      "Tags": [
    //      "primary",
    //      "v1"
    //      ],
    //      "Address": "127.0.0.1",
    //      "Port": 8000,
    //      "EnableTagOverride": false,
    //      "Check": {
    //        "DeregisterCriticalServiceAfter": "90m",
    //        "Script": "/usr/local/bin/check_redis.py",
    //        "HTTP": "http://localhost:5000/health",
    //        "Interval": "10s",
    //        "TTL": "15s"
    //      }
    //    }


  }
}
