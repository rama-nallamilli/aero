package org.rntech.worker

import org.rntech.worker.serialization.{Deserailizer, Serializer}

import scala.util.{Failure, Success, Try}

case class ProcessorFailed(msg: String, cause: Throwable) extends RuntimeException(cause)

trait Transformer[I,O] {
  def transform(in: I): O
}

abstract class Processor[In, Out] {

  def fn(in: In): Out

  def process(input: In, headers: Map[String, Any]): Out

  protected[this] def run(data: Array[Byte], headers: Map[String, Any])(implicit sez: Serializer, desez: Deserailizer) = {
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

object E {

  def pTransformers[T,D](fn: (T) => D)(value: T) = {
    fn.apply(value)
  }


  val firstTransformer: (String) => Int = pTransformers[String,Int](str => str.toInt)
  val secondTransformer: (Int) => String = pTransformers[Int,String](integer => integer.toString)



  registerTransformers(firstTransformer, secondTransformer)
//  registerTransformer(firstTransformer)
  def registerTransformers[A,B](fn: ((A) => B)*) = {

  }

}

object Processor {
  def processors[A, B](fn: A => B) = {

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