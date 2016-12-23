package feature

import org.scalatest.{DoNotDiscover, FeatureSpec, GivenWhenThen}

@DoNotDiscover
class ConsulRegistrationFeature extends FeatureSpec with GivenWhenThen {

  feature("Worker registration against Consul cluster") {
    scenario("On startup the worker successfully registers with the Consul cluster") {
      Given("the application is running and consul is available")

      When("it starts up")

      Then("it should have registered itself with Consul")

    }

    scenario("On shutdown the worker de-registers itself from the Consul cluster") {}
  }

}
