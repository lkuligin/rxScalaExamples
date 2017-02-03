package com.lkuligin.rxScalaExamples

import java.util.concurrent.Executor

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala.{Subscriber, Subscription}
import rx.lang.scala.schedulers.ExecutionContextScheduler

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.concurrent.duration._

/**
  * Created by lkuligin on 03/02/2017.
  */
class Example4(executor: Executor, id: Int) extends LazyLogging {
  private val ec = ExecutionContext.fromExecutor(executor)
  private val scheduler = ExecutionContextScheduler(ec)
  private val mainThread = Thread.currentThread()


  /*sys.ShutdownHookThread {
    logger.debug(s"Shutting down the rxListingConsumer in thread ${Thread.currentThread().getName()}")
    //subscriber.unsubscribe()
    val f = stop()
    try {
      Await.result(f, 1 seconds)
    } catch {
      case NonFatal(e) => logger.error("Unexpected exception when shutting down listing consumer", e)
    }
    mainThread.join()
  }*/

  val emulator = new DataEmulator(10, 500, false)

  /*val s = DataObservable(emulator, s"${id}")
    .doOnEach(el => {
      logger.info(s"consumer${id} is consuming ${el} in ${Thread.currentThread().getName()}")
      Thread.sleep(100)}
    )*/

  val subscriber = DataObservable(emulator, s"${id}")
        .doOnEach(el => {
          logger.info(s"consumer${id} is consuming ${el} in ${Thread.currentThread().getName()}")
          Thread.sleep(100)}
        )
        //.onExceptionResumeNext(Observable.just(0))
        .subscribeOn(scheduler)
        .subscribe()

  /*def consume() =  {
    val emulator = new DataEmulator(10, 0, false)

    Future {
      s.subscribe()
        //.onExceptionResumeNext(Observable.just(0))
        //.subscribe()
    }(ec)
  }*/

  def stop() = {
    logger.debug(s"subscriber.unsubscribe() in thread ${Thread.currentThread().getName()}")
    subscriber.unsubscribe()
    logger.debug(s"subscriber has been unsubscribed in thread ${Thread.currentThread().getName()}")
  }

}
