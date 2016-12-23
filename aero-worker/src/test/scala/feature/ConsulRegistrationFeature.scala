package feature

import org.scalatest.{DoNotDiscover, FeatureSpec}

@DoNotDiscover
class ConsulRegistrationFeature extends FeatureSpec {

  feature("Worker registration against Consul cluster") {
    scenario("On startup the worker successfully registers with the Consul cluster") {

    }

    scenario("On shutdown the worker de-registers itself from the Consul cluster") {}
  }

}
