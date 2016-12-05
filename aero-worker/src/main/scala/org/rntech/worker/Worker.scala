package org.rntech.worker

import scala.pickling.Defaults._
import scala.pickling.binary._
import scala.util.{Failure, Success, Try}

case class ProcessorFailed(msg: String, cause: Throwable) extends RuntimeException(cause)

trait Deserailizer {
  def desez(data: Array[Byte]): AnyRef
}

trait Serializer {
  def sez[T](a: T): Array[Byte]
}

object PicklingDeserailizer extends Deserailizer {
  def desez(data: Array[Byte]): AnyRef = BinaryPickleArray(data).unpickle[AnyRef]
}

object PicklingSerializer extends Serializer {
  def sez[T](a: T): Array[Byte] = a.pickle.value
}

abstract class Processor[In, Out] {

  def processors[A, B](fn: A => B)



  def process(input: In, headers: Map[String, Any]): Out

  protected[this] def run(data: Array[Byte], headers: Map[String, Any])(implicit sez: Serializer, desez: Deserailizer) = {
    //TODO switch out so can choose any parsing implementation?
    val pick = desez.desez(data)
    Try(process(pick.asInstanceOf[In], Map.empty[String, Any])) match {
      case Success(result) =>
        sendOutputData(sez.sez(result))
      case Failure(ex) =>
        reportFailure(s"Failure during processing, ${getClass.getSimpleName}", ex)
    }
  }

  def sendOutputData(data: Array[Byte]) = {

  }

  def reportFailure(msg: String, ex: Throwable) = {

  }

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