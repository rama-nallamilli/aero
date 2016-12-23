package feature

import org.scalatest.{BeforeAndAfterAll, Suite}
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

trait WiremockServer extends BeforeAndAfterAll {
  this: Suite =>

  private val wiremockPort = 8190 //TODO read from config
  private lazy val wireMockServer = new WireMockServer(options().port(wiremockPort))

  override def beforeAll(): Unit = {
    super.beforeAll()
    if(!wireMockServer.isRunning)
      wireMockServer.start()

    wireMockServer.resetAll()
    configureFor(wiremockPort)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    wireMockServer.stop()
  }

}
