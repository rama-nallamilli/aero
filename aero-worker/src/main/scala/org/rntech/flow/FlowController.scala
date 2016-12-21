package org.rntech.flow

import akka.http.scaladsl.server.Directives._

//Can receive requests to run and/or stop processors
//maybe the source is already loaded onto the machines as jar files in lib directory,
// aero-master distributes sources to each node
class FlowController {

  def routes =
    path("flow" / "transformers" / RemainingPath) { transformerName =>
      //Returns the status of the data transformer on this node
      get {
        ???
      }

      //Adds one of these transformers to the current worker ready to process jobs
      put {
        ???
      }
    }
}