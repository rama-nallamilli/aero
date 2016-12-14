package org.rntech.jobs

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage, Message}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{Sink, Source, Flow}

class JobsController(jobService: JobService) {

  def inboundJobStream: Flow[Message, Message, Any] =
    Flow[Message].mapConcat {
      case tm: TextMessage =>
        //sends ACK or FAIL(Reason) back
        // TODO mat this stream to get the text?
        // tm.textStream

        //jobService.queueJob()
        TextMessage(Source.single("ACK")) :: Nil
      case bm: BinaryMessage =>
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }

  val inboundJobsRoute =
    path("jobs" / "inbound") {
      //UpgradeToWebSocket

      handleWebSocketMessages(inboundJobStream)
    }

  val jobStatus =
  path("jobs" / "status") {
    get {
      complete(HttpEntity(ContentTypes.`application/json`,
        """
          |
          |{"status": "ok" }
          |
          |""".stripMargin))
    }
  }
}

//TODO websocket that recieves jobs
//TODO registration in consul?
//TODO health endpoint that gives you the port and workers that are hosted here?
