package feature

import org.rntech.entrypoint.Application
import org.scalatest.{BeforeAndAfterAll, Suite}

trait RunningApplication extends BeforeAndAfterAll {
  this: Suite =>

  override def beforeAll(): Unit = {
    super.beforeAll()
    Application.start()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Application.stop()
  }

}
