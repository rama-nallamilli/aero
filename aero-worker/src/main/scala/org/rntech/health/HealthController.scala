package org.rntech.health

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}

object HealthController {
  private val applicationHealth =
    path("health") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, """{ "status": "ok" }""".stripMargin))
      }
    }

  def routes = applicationHealth
}
