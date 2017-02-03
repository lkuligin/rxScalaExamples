package com.lkuligin.rxScalaExamples

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable

/**
  * Created by lkuligin on 27/01/2017.
  */
class DataEmulator(amount: Int = 10, emissionDelay: Long = 0, withErrorEmulation: Boolean = false) extends LazyLogging {

  val data: mutable.Queue[Int] = mutable.Queue()
  data.enqueue((1 to amount):_*)

  var count = 0

  def getElement(id: String = "1") = {
    logger.info(s"emulator is emitting value for consumer ${id} in ${Thread.currentThread().getName()}")
    Thread.sleep(emissionDelay)
    count += 1
    if (withErrorEmulation && count % 3 == 0) {
      System.out.println("throwing error")
      data.dequeue()
      //throw new IllegalArgumentException("test exception")
      throw new InterruptedException()
    }
    else data.dequeue()
  }
}
