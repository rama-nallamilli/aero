package org.rntech.jobs

import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.stream.scaladsl.Flow.fromFunction
import akka.stream.scaladsl.{Flow, Sink, Source}

class JobsController(jobService: JobService)(implicit materializer: Materializer) {

  private def inboundJobStream: Flow[Message, Message, Any] = {
    val addJob = fromFunction[String, Either[JobOperationFailed, JobAdded]](s => jobService.queueJob(s))
    val resultToString = fromFunction[Either[JobOperationFailed, JobAdded], String](s => s.right.toString)

    Flow[Message].mapConcat {
      case tm: TextMessage if tm.isStrict =>
        val msg = tm.getStrictText
        TextMessage(Source.single(msg) via addJob via resultToString) :: Nil
      case tm: TextMessage =>
        TextMessage(tm.textStream via addJob via resultToString) :: Nil
      case bm: BinaryMessage => //TODO
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }
  }

  private val inboundJobsRoute =
    path("jobs" / "inbound") {
      handleWebSocketMessages(inboundJobStream)
    }

  def routes = inboundJobsRoute
}