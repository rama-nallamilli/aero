package org.rntech.entrypoint

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.rntech.consul.Registrater
import org.rntech.jobs.{JobService, JobsController}

import scala.concurrent.{Await, ExecutionContext, Future, blocking}
import scala.io.StdIn
import scala.concurrent.duration._

object Main extends App {
  import Application._
  start()
  awaitInput()
  stop()
}

object Config {
  val config = ConfigFactory.load()
  val serviceHost = config.getString("aero.worker.service.host")
  val servicePort = config.getInt("aero.worker.service.port")
  val consulHttpUrl = config.getString("aero.worker.consul.host")
  val consulPort = config.getInt("aero.worker.consul.port")
}

object Application {
  implicit val system = ActorSystem("job-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  val jobService = new JobService
  val jobController = new JobsController(jobService)
  val registrater = new Registrater(Config.consulHttpUrl, Config.consulPort)
  val serviceId = UUID.randomUUID().toString
  var bindingFuture: Future[ServerBinding] = null

  def start() = {
    bindingFuture = Http().bindAndHandle(jobController.routes, "0.0.0.0", Config.servicePort)

    blocking {
      Await.result(registrater.registerSelf(serviceId, Config.serviceHost, Config.servicePort), 2 seconds) match {
        case Left(a) => println(s"Registration failed $a")
        case Right(b) => println(s"Registered $b")
      }
    }
  }

  def awaitInput() = {
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
  }

  def stop() = {
    blocking {
      Await.result(registrater.deregister(serviceId), 2 seconds) match {
        case Left(a) => println(s"Registration failed $a")
        case Right(b) => println(s"Deregistered $b")
      }
    }

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
