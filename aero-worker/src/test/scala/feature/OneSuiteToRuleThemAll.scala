package feature

import org.scalatest.Suites

class OneSuiteToRuleThemAll extends Suites(//forged in the depths of Mordor
  new ConsulRegistrationFeature) with WiremockServer with RunningApplication

