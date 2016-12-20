package org.rntech.consul

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpHeader, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer

case class RegistratorConfig(consulHttpUrl: String)

class Registrator(consulHttpUrl: String) {

  def registerSelf(implicit system: ActorSystem, materializer: ActorMaterializer) = {
    val registrationEndpoint = s"$consulHttpUrl/v1/agent/service/register"

    //https://www.consul.io/docs/agent/http/agent.html#agent_service_register
    val responseFuture = Http().singleRequest(HttpRequest(uri = "http://akka.io")
      .withMethod(HttpMethods.PUT)
     //   .withEntity()
    )


  }
}
