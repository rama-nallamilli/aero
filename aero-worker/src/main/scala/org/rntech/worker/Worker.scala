package org.rntech.worker

import org.rntech.worker.serialization.{Deserailizer, Serializer}

import scala.util.{Failure, Success, Try}

case class ProcessorFailed(msg: String, cause: Throwable) extends RuntimeException(cause)

object ProcessorRunner {

  //This class does running and ser/dez, maybe we should really have a sep of concern?, just running?
  def run[In,Out](fn: In => Out, data: Array[Byte])(implicit sez: Serializer, desez: Deserailizer): Either[ProcessorFailed,Array[Byte]] = {
    val pick = desez.desez(data)
    Try(fn(pick.asInstanceOf[In])) match {
      case Success(result) =>
        Right(sez.sez(result))
      case Failure(ex) =>
        Left(ProcessorFailed("Failure during processing, ${getClass.getSimpleName}", ex))
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

  import org.rntech.worker.serialization.Implicits.{dez, sez}


  ProcessorRunner.run(first.fn, data)
  //processor pools that can execute and return, just have one initially?

}

class Worker {

  val processors = None
  val fetchers = None

  def startup() = {

  }

  def shutdown() = {

  }

}