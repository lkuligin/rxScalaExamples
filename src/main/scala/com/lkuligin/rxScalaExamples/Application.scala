package com.lkuligin.rxScalaExamples

import java.util.concurrent.ExecutorService

import ch.qos.logback.classic.LoggerContext
import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging
import net.codingwell.scalaguice.InjectorExtensions._
import org.slf4j.LoggerFactory
import rx.lang.scala.Observable

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

/**
  * Created by lkuligin on 26/01/2017.
  */
object Application extends App with LazyLogging {
  logger.debug("starting app...")
  val injector = Guice.createInjector(
    new AppModule()
  )

  val example1 = injector.instance[Example1]
  val example2 = injector.instance[Example2]
  val executor = injector.instance[ExecutorService]

  //example1.errorHandling2
  //example1.errorHandling
  example2.OwnObservableConcurrent
  logger.info("done")


  /*val httpServer = injector.instance[HttpServer]
  httpServer.start()

  private val mainThread = Thread.currentThread()
  @volatile private var keepRunning = true

  try {
    //example2.ConcurrentWithScheduler(executor)
    val a = (1 to 2).map(id => new Example4(executor, id))
    sys.addShutdownHook {
      logger.debug("shutdown app")
      keepRunning = false
      import scala.concurrent.ExecutionContext.Implicits.global
      a.map(el => Future{el.stop()}).map(el => Await.result(el, 5 seconds))
      //Thread.sleep(100)
      //mainThread.join()
    }
    while (keepRunning) {
      System.out.println(s"Sleeping in thread ${Thread.currentThread().getName()}")
      Thread.sleep(1000)
    }
  } catch {
    case NonFatal(e) => logger.error("Unexpected exception thrown by consumer", e)
  } finally {
    logger.debug("Cleaning up....")
    httpServer.stop()
    LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext].stop()
  }*/

  //example1.errorHandling
  //example1.errorHandling2
  //example3.errorHandling
  //example1.groupByExample
  //example1.groupByExample1
  //example2.OwnObservableAlone
  //example2.OwnObservableConcurrent
  //example2.OwnObservableConcurrentAlt
  //example2.OwnObservableConcurrentAltError


  //import scala.concurrent.ExecutionContext.Implicits.global

  //Example of interval observer
  /*
  example1.step1()
  //without waiting for result = nothing will be consumed
  Await.result(Future { Thread.sleep(1500) }, 2 seconds)
  System.out.println("stop waiting")
  */

  //example of blocking consumer fixing the issue above
  /*
  example1.step2()
  System.out.println("do something = no parallel computations available!")


  System.out.println("step3")
  example1.step3 subscribe  {el => {System.out.println(s"example of consuming from zipped observable ${el}")}}
  */

  //No multiple subscription - observables are consumed step by step
  /*System.out.println("step4")
  val o = example1.observableInt
  o subscribe {
    el => {
      System.out.println(s"consumer1: consuming from observable ${el} and doing some job")
      Thread.sleep(200)
    }
  }

  o subscribe {
    el => {
      System.out.println(s"consumer2: consuming from observable ${el} and doing some job")
      Thread.sleep(200)
    }
  }
  */

  /*System.out.println("map example")
  val o = example1.observableIntIndependent
  o
    .map(i => {
      System.out.println(s"very slowly consuming ${i}")
      Thread.sleep(1000)
      i*2
    })
    .toBlocking
    .subscribe()*/

  /*val o: Observable[Int] = example1.observableInt.flatMap({el => Observable.just(el, el+1)})
  o subscribe  {el => {System.out.println(s"example of consuming from zipped observable ${el}")}}*/



  /*val o: Observable[Int] = IntObservable()*/
  /*o subscribe {el => {
    System.out.println(s"consumer1 is consuming ${el}")
    Thread.sleep(100)
  }}
  o subscribe {el => {
    System.out.println(s"consumer2 is consuming ${el}")
    Thread.sleep(100)
  }}*/

  /*val f1 = Future {
    o subscribe {el => {
      System.out.println(s"consumer1 is consuming ${el}")
      Thread.sleep(100)
    }}
  }
  println("before onComplete")
  f1.onComplete {
    case Success(_) => System.out.println(s"Got the callback from consumer1")
    case Failure(e) => System.out.println(s"Error", e)
  }

  val f2 = Future {
    o subscribe {el => {
      System.out.println(s"consumer2 is consuming ${el}")
      Thread.sleep(100)
    }}
  }
  println("before onComplete")
  f2.onComplete {
    case Success(_) => System.out.println(s"Got the callback from consumer1")
    case Failure(e) => System.out.println(s"Error", e)
  }

  logger.info("doing A ...")
  Thread.sleep(100)
  logger.info("doing B ...")
  Thread.sleep(100)
  logger.info("doing C ...")
  Thread.sleep(100)
  Await.result(f1, Integer.MAX_VALUE millis)
  Await.result(f2, Integer.MAX_VALUE millis)*/

  /*val o: Observable[Long] = example1.step5()
  val o1: Observable[(Long, Observable[Long])] = o.groupBy(_ % 2)
  val o2 = o1.flatMap(
    {
      case (partition, element) => Observable.just(element)
  })*/

}
