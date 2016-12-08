package org.rntech.worker

import org.rntech.worker.serialization.{Deserailizer, Serializer}

import scala.util.{Failure, Success, Try}
import cats.syntax.either._

case class ProcessingFailed(msg: String, cause: Throwable) extends RuntimeException(cause)

object Prototyping {

  case class DataTransformer[T, D](name: String, fn: (T) => D, concurrency: Int = 1)

  case class Flow(flowName: String, transformers: DataTransformer[_, _]*)

  type Job = (String, Array[Byte])

  val transformA = DataTransformer(name = "transformA",
    fn = {
      s: String =>
        s.toInt
    })

  val transformB = DataTransformer(name = "transformB",
    fn = {
      i: Int =>
        s"hello $i"
    })

  val flow = Flow("super-data-flow", transformA, transformB)

  //This flow is sent to the cluster and deployed, each node cluster has a JobReciever and a JobSender
  //The JobReceiver will accept jobs over HTTP, it will then route it an appropriate processor
  //The JobSender will take the processor output result and send it to the next processor
  //Each node knows about every flow and where each processor is deployed

  val (targetProcessor, data) = JobReciever.receive()

  object JobReciever {
    def receive(): Job = ("transformToString", "someData".getBytes)
  }
  case class ProcessingResult(name: String, outputData: Array[Byte])

  object Orchastrator {
    import serialization.Implicits._
    def processJob(job: Job): Either[ProcessingFailed, ProcessingResult] = {
      val (processorName, data) = job
      val transformer = flow.transformers.find(_.name equals processorName) //throw error if not found
      run(transformer.get.fn, data).map { outData =>
        ProcessingResult(processorName, outData)
      }
    }

    private def run[In, Out](fn: In => Out, data: Array[Byte])(implicit serializer: Serializer, deserailizer: Deserailizer): Either[ProcessingFailed, Array[Byte]] = {

      val pick = deserailizer.desez(data)
      Try(fn(pick.asInstanceOf[In])) match {
        case Success(result) =>
          Right(serializer.sez(result))
        case Failure(ex) =>
          Left(ProcessingFailed("Failure during processing, ${getClass.getSimpleName}", ex))
      }
    }
  }

  object JobSender {
    def send(name: String, processingResult: ProcessingResult): Either[ProcessingFailed, String] = {
      Right("Done")
    }
  }

  val job = JobReciever.receive()
  val result = Orchastrator.processJob(job)
  def getNextStageName: String = "" //todo flow needs to be some kind of tree?
  val sendResult = result.flatMap { data =>
    JobSender.send(getNextStageName, data)
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