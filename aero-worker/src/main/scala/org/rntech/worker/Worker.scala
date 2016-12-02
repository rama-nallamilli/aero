package org.rntech.worker

//TODO should see if we can find away to extract from Byte array into default formater (this should also probably use some kind of compresion format e.g kyro)
case class Context(data: Array[Byte], headers: Map[String, Any])

abstract class Processor[In,Out] {
  def process(input: In, headers: Map[String, Any]): Out

  protected[this] def preProcess(data: Array[Byte], headers: Map[String, Any]) = {
    //deserailise
    //validate
    //process()
  }

  def validateIn: Class[In]
  def validateOut: Class[Out]
}

trait Fetcher[Out] {
  def fetch(): Out

  def validateOut: Class[Out]
}

class Worker {

  val processors = None
  val fetchers = None

  def startup() = {

  }

  def shutdown() = {

  }

}
