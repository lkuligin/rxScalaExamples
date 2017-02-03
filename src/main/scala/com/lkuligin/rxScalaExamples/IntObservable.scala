package com.lkuligin.rxScalaExamples

import com.typesafe.scalalogging.LazyLogging
import rx.Observer
import rx.lang.scala.Observable
import rx.observables.SyncOnSubscribe

import scala.util.control.NonFatal

/**
  * Created by lkuligin on 27/01/2017.
  */
class IntObservable() extends SyncOnSubscribe[Int, Int] with LazyLogging  {
  //private var id: Int = 0

  override def generateState(): Int = {
    //id = id + 1
    //System.out.println(s"generating state for ${id}")
    0
  }

  override def next(state: Int, observer: Observer[_ >: Int]): Int = {
    try {
      System.out.println(s"Emitting value ${state}")
      if (state<10) observer.onNext(state)
      else observer.onCompleted()
    } catch {
      /*case _: InterruptedException =>
        logger.warn("interrupted while waiting for the messages")
        Thread.currentThread().interrupt()*/
      case NonFatal(e) => observer.onError(e)
    }
    state + 1
  }

  override def onUnsubscribe(state: Int): Unit = {
    logger.debug(s"Subscriber has been unsubscribed, current state ${state}")
  }
}

object IntObservable {
  def apply() =
    rx.lang.scala.JavaConversions.toScalaObservable(rx.Observable.create(new IntObservable()))
}