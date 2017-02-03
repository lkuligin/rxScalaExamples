package com.lkuligin.rxScalaExamples

import java.util.NoSuchElementException

import com.lkuligin.rxScalaExamples.DataObservable.DataObservableState
import com.typesafe.scalalogging.LazyLogging
import rx.Observable.OnSubscribe
import rx.exceptions.Exceptions
import rx.lang.scala.Observable
import rx.{Observer, Subscriber}
import rx.observables.SyncOnSubscribe

import scala.util.control.NonFatal

/**
  * Created by lkuligin on 27/01/2017.
  */
class DataObservable(source: DataEmulator, id: String = "1") extends SyncOnSubscribe[DataObservableState, Int] with LazyLogging  {
  //private var id: Int = 0

  override def generateState(): DataObservableState = {
    //id = id + 1
    //System.out.println(s"generating state for ${id}")
    System.out.println("generating state")
    new DataObservableState(source, id)
  }

  override def next(state: DataObservableState, observer: Observer[_ >: Int]): DataObservableState = {
    try {
       observer.onNext(state.getRecord())
    } catch {
      case _: InterruptedException =>
        logger.warn("interrupted while waiting for the messages")
        Observable.empty
        //Thread.currentThread().interrupt()
      case e: NoSuchElementException => observer.onCompleted()
      case e: Throwable => logger.info("get error") //logger.error("error1", e)
    }
    state
  }

  override def onUnsubscribe(state: DataObservableState): Unit = {
    logger.info(s"Subscriber has been unsubscribed in thread ${Thread.currentThread().getName()}, current state ${state}")
  }
}

object DataObservable {
  class DataObservableState(source: DataEmulator, id: String) extends LazyLogging {
    def getRecord(): Int = {
      try {
        logger.info(s"Emitting value from observable, id=${id}")
        source.getElement(id)
      } catch {
        case t: Throwable => throw Exceptions.propagate(t)
      }
    }
  }

  def apply(source: DataEmulator, id: String = "1") = {
    System.out.println("Running apply")
    rx.lang.scala.JavaConversions.toScalaObservable(rx.Observable.create(new DataObservable(source, id)))
  }
}