package feature

import org.scalatest.{DoNotDiscover, FeatureSpec}

@DoNotDiscover
class JobProcessingFeature extends FeatureSpec {

  feature("Processes inbound jobs") {

    // before this can happen the flow must have been submitted and loaded so we know what the current code to run is for the job
    // and the next one to send it too
//    1. recieve job
//    2. process job
//    3. send next job to next availble worker (use consul to get the address of the worker that the job needs to go to)
//    4. the hash key is used to target the node
    scenario("Successfully processes inbound jobs") {

    }
  }
}
