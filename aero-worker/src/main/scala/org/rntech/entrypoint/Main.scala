package org.rntech.entrypoint

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.rntech.jobs.{JobService, JobsController}

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Main extends App {

//  val localhost = InetAddress.getLocalHost
//  val localIpAddress = localhost.getHostAddress
//  println(localIpAddress)

  implicit val system = ActorSystem("job-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val jobService = new JobService
  val jobController = new JobsController(jobService)

  val bindingFuture = Http().bindAndHandle(jobController.routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
