package com.lkuligin.rxScalaExamples

import java.util.concurrent.Executor

import com.typesafe.scalalogging.LazyLogging
import rx.Observable.OnSubscribe
import rx.lang.scala.{Observable, Subscriber}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

/**
  * Created by lkuligin on 27/01/2017.
  */
class Example2 extends LazyLogging {

  def step1 = {
    var i = 0
    def createObservable: Observable[Int] = Observable {
      subscriber: Subscriber[Int] => {
        while (i < 5 && !subscriber.isUnsubscribed) {
          subscriber.onNext(i)
          i += 1;
        }
        if (!subscriber.isUnsubscribed) {
          subscriber.onCompleted()
        }

      }
    }
    val o = createObservable
    o
      .map(el => 3/(el-3))
      .retry
      .onErrorResumeNext(_ => {
        System.out.print("error")
        Observable.empty
    })
       .retry
     .subscribe(println(_))
  }

  def step2 = {
    val emulator = new DataEmulator()
    val o: Observable[Int] = DataObservable(emulator)
    o.subscribe(println(_))
  }

  def OwnObservableAlone = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val emulator = new DataEmulator()
    val o: Observable[Int] = DataObservable(emulator)

    val f1 = Future {
      o subscribe {el => {
        logger.info(s"consumer1 is consuming ${el}")
        Thread.sleep(100)
      }}
    }

    println("before onComplete")
    f1.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer1")
      case Failure(e) => logger.info(s"Error", e)
    }

    logger.info("doing A ...")
    Thread.sleep(100)
    logger.info("doing B ...")
    Thread.sleep(100)
    logger.info("doing C ...")
    Thread.sleep(100)
    Await.result(f1, Integer.MAX_VALUE millis)
  }

  def OwnObservableConcurrent = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val emulator = new DataEmulator()
    val o: Observable[Int] = DataObservable(emulator)

    val f1 = Future {
      o subscribe {el => {
        logger.info(s"consumer1 is consuming ${el}")
        Thread.sleep(100)
      }}
    }

    val f2 = Future {
      o subscribe {el => {
        logger.info(s"consumer2 is consuming ${el}")
        Thread.sleep(150)
      }}
    }

    println("before onComplete")
    f1.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer1")
      case Failure(e) => logger.info(s"Error", e)
    }

    f2.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer2")
      case Failure(e) => logger.info(s"Error", e)
    }

    logger.info("doing A ...")
    Thread.sleep(100)
    logger.info("doing B ...")
    Thread.sleep(100)
    logger.info("doing C ...")
    Thread.sleep(100)
    Await.result(f1, Integer.MAX_VALUE millis)
    Await.result(f2, Integer.MAX_VALUE millis)

  }

  def OwnObservableConcurrentAlt = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val emulator = new DataEmulator()

    val f1 = Future {
      DataObservable(emulator, "1") subscribe {el => {
        logger.info(s"consumer1 is consuming ${el}")
        Thread.sleep(100)
      }}
    }

    val f2 = Future {
      DataObservable(emulator, "2") subscribe {el => {
        logger.info(s"consumer2 is consuming ${el}")
        Thread.sleep(150)
      }}
    }

    println("before onComplete")
    f1.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer1")
      case Failure(e) => logger.info(s"Error", e)
    }

    f2.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer2")
      case Failure(e) => logger.info(s"Error", e)
    }

    logger.info("doing A ...")
    Thread.sleep(100)
    logger.info("doing B ...")
    Thread.sleep(100)
    logger.info("doing C ...")
    Thread.sleep(100)
    Await.result(f1, Integer.MAX_VALUE millis)
    Await.result(f2, Integer.MAX_VALUE millis)

  }

  def OwnObservableConcurrentAltError = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val emulator = new DataEmulator(10, 0, true)


    val f1 = Future {
      DataObservable(emulator, "1")
        .doOnEach(el => {
          logger.info(s"consumer1 is consuming ${el}")
          Thread.sleep(100)}
        )
        .onExceptionResumeNext(Observable.just(0))
        .subscribe()
    }

    val f2 = Future {
      DataObservable(emulator, "2").subscribe(el => {
        logger.info(s"consumer2 is consuming ${el}")
        Thread.sleep(150)
      },
        e => logger.error("error", e)
      )
    }

    println("before onComplete")
    f1.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer1")
      case Failure(e) => logger.info(s"Error", e)
    }

    f2.onComplete {
      case Success(_) => logger.info(s"Got the callback from consumer2")
      case Failure(e) => logger.info(s"Error", e)
    }

    logger.info("doing A ...")
    Thread.sleep(100)
    logger.info("doing B ...")
    Thread.sleep(100)
    logger.info("doing C ...")
    Thread.sleep(100)
    Await.result(f1, Integer.MAX_VALUE millis)
    Await.result(f2, Integer.MAX_VALUE millis)

  }

  def ConcurrentWithContext (executor: Executor) = {
    (1 to 2).map(id => new Example3(executor, id)).map(el => el.consume()).map(Await.result(_, Integer.MAX_VALUE millis))
  }

  def ConcurrentWithScheduler (executor: Executor) = {
    val a = (1 to 2).map(id => new Example4(executor, id))//.map(el => el.consume()).map(Await.result(_, Integer.MAX_VALUE millis))
    Thread.sleep(500)
    //System.out.println(s"Sleeping in scheduler in thread ${Thread.currentThread().getName()}")
    /*sys.addShutdownHook {
      logger.debug(s"Shutting down all consumer in thread ${Thread.currentThread().getName()}")
      try {
        a.map(el => el.stop()).map(Await.result(_, 30 seconds))
      } catch {
        case NonFatal(e) => logger.error("Unexpected exception when shutting down listing consumer", e)
      }
    }*/
    //a.map(el => el.stop())
  }


}
