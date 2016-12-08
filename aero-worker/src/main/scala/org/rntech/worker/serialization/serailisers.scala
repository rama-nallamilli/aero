package org.rntech.worker.serialization

import scala.pickling.Defaults._
import scala.pickling.binary.{BinaryPickleArray, _}

trait Deserailizer {
  def desez(data: Array[Byte]): AnyRef
}

trait Serializer {
  def sez[T](a: T): Array[Byte]
}


object PicklingDeserailizer extends Deserailizer {
  def desez(data: Array[Byte]): AnyRef = ??? //BinaryPickleArray(data).unpickle[AnyRef]
}

object PicklingSerializer extends Serializer {
  def sez[T](a: T): Array[Byte] = ??? //a.pickle.value
}

object Implicits {
  implicit val sez = PicklingSerializer
  implicit val dez = PicklingDeserailizer
}