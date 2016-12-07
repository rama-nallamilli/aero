package org.rntech.worker

import org.rntech.worker.serialization.{Deserailizer, Serializer}

import scala.util.{Failure, Success, Try}

case class ProcessorFailed(msg: String, cause: Throwable) extends RuntimeException(cause)

abstract class ProcessorRunner[In, Out](fn: In => Out) {

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

object Prototyping {
//  def pTransformers[T,D](fn: (T) => D)(value: T) = {
//    fn.apply(value)
//  }
//
//  val firstTransformer: (String) => Int = pTransformers[String,Int](str => str.toInt)
//  val secondTransformer: (Int) => String = pTransformers[Int,String](integer => integer.toString)
//  registerTransformers(firstTransformer, secondTransformer)
//
//  def registerTransformers[A,B](fn: ((A) => B)*) = {}


  type Job = (String,Array[Byte])

  case class DataTransformer[T,D](name: String, fn: (T) => D)

  val first = DataTransformer("transformToString", {
    s:String =>
      10
  })


  val second = DataTransformer("fromAIntToAString", {
    i: Int => "hello"
  })

  registerDataTransformers("super-data-flow", first, second)


  def registerDataTransformers(flowName: String, transformers: DataTransformer[_,_]*) = {
    transformers.foreach { transformer =>

    }
  }

  val (targetProcessor, data) = JobReciever.recieve()
  object JobReciever {
    def recieve(): Job = {
      val data = "somedata".getBytes
      ("transformToString", data)
    }
  }
}

class Worker {

  val processors = None
  val fetchers = None

  def startup() = {

  }

  def shutdown() = {

  }

}