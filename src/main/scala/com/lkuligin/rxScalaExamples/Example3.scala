package com.lkuligin.rxScalaExamples

import java.util.concurrent.Executor

import com.typesafe.scalalogging.LazyLogging
import rx.Observable
import rx.lang.scala.{Observable, Subscription}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.control.NonFatal

/**
  * Example of multithreading subsribers
  * Created by lkuligin on 30/01/2017.
  */
class Example3(executor: Executor, id: Int) extends LazyLogging {
  private val ec = ExecutionContext.fromExecutor(executor)

  def consume(): Future[Subscription] =  {
    val emulator = new DataEmulator(10, 0, false)

    Future {
      DataObservable(emulator, s"${id}")
        .doOnEach(el => {
          logger.info(s"consumer${id} is consuming ${el} in ${Thread.currentThread().getName()}")
          Thread.sleep(100)}
        )
        //.onExceptionResumeNext(Observable.just(0))
        .subscribe()
    }(ec)
  }

}
