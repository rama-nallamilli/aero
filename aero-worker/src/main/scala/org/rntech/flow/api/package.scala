package org.rntech.flow


package object api {
  sealed case class DataTransformer[T, D](name: String, fn: (T) => D, concurrency: Int = 1)

  sealed case class Flow(flowName: String, transformers: DataTransformer[_, _]*)

  object StubbedFlow {
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
  }
}
