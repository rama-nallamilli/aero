package org.rntech.entrypoint

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import org.rntech.jobs.{JobService, JobsController}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object Main extends App {
  import Application._
  start()
  awaitInput()
  stop()
}

object Application {
  implicit val system = ActorSystem("job-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  val jobService = new JobService
  val jobController = new JobsController(jobService)
  var bindingFuture: Future[ServerBinding] = null

  def start() = {
    bindingFuture = Http().bindAndHandle(jobController.routes, "localhost", 8080)
  }

  def awaitInput() = {
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
  }

  def stop() = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
