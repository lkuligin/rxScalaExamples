package com.lkuligin.rxScalaExamples

import com.typesafe.scalalogging.LazyLogging
import rx.lang.scala._

import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
  * Created by lkuligin on 26/01/2017.
  */
class Example1 extends LazyLogging {
  //val o = Observable.interval(200 millis).take(5)
  //o.subscribe(n => println("n = " + n))
  //Observable.just(1, 2, 3, 4).reduce(_ + _)

  private def observableIntIndependent: Observable[Int] = {
    Observable.interval(100 millis).take(10).map(i => {
      System.out.println(s"emitting ${i}")
      i.toInt
    })
  }

  def observableInt(amountMax: Int = 10, printEmitMsg: Boolean = true, emissionDelay: Long = 1000): Observable[Int] = {
    Observable(
      { observer => {
        try {
          val a = true
          if (!observer.isUnsubscribed) {
            (1 to amountMax) foreach {
              i => {
                if (printEmitMsg) System.out.println(s"observable: emitting ${i} and waiting for next item")
                observer.onNext(i)
                Thread.sleep(emissionDelay)
              }
            }
            observer.onCompleted()
          }
        } catch {
          case e: Exception => observer.onError(e)
        }
        System.out.println("disposed")
      }
      })
  }

  private def createFunnyExample(partition: Int, el: Seq[Int]) = {
    FunnyExample(partition, el.max[Int], el)
  }


  def step1(): Unit = {
    val o = Observable.interval(200 millis).take(5)
    o.subscribe(n => System.out.println(s"observing interval = ${n}"))
  }

  def step2(): Unit = {
    val o = Observable.interval(200 millis).take(5)
    o.toBlocking.subscribe(n => System.out.println(s"observing interval = ${n}"))
  }

  def step3: Observable[Boolean] = {
    val first = Observable.just(10, 11, 12, 14)
    val second = Observable.just(10, 11, 13)
    for ((n1, n2) <- (first zip second)) yield (n1 == n2)
  }

  def step5(): Observable[Long] = {
    Observable.interval(200 millis).take(100)
  }

  def groupByExample = {
    val o: Observable[IntPair] =  observableInt()
      .groupBy(_ % 2)
      .flatMap({
        case (partition, lines) => lines.map({el => new IntPair(partition, el)})
      })
    o subscribe  {el => {System.out.println(s"consuming ${el}")}}
  }

  def groupByExample1 = {
    val o: Observable[FunnyExample] =  observableInt(20, false, 0)
      .groupBy(_ % 2)
      .flatMap({
        case (partition, lines) => lines
          .tumblingBuffer(5)
          .map(createFunnyExample(partition, _))
      }
      )
    o subscribe  {el => {System.out.println(s"consuming ${el}")}}
  }

  def errorHandling = {
    val o1 = List(1, 2, 3, 0, 4) toObservable
    val o2 = List(1, 2, 2, 4, 5) toObservable

    val o3 = o1 zip o2 map {case (i1, i2) => i2 / i1 }

    val o4: Observable[Try[Int]] = o3 lift {
      subscriber: Subscriber[Try[Int]] => new Subscriber[Int](subscriber) {
        override def onNext(v: Int) {
          if (!subscriber.isUnsubscribed)
            subscriber.onNext(Success(v))
        }

        override def onError(e: Throwable) {
          if (!subscriber.isUnsubscribed)
            System.out.println("error")
            //subscriber.onNext(Failure(e))
        }

        override def onCompleted() {
          if (!subscriber.isUnsubscribed)
            subscriber.onCompleted()
        }
      }
    }

    o4 subscribe(println(_))
  }

  def errorHandling2 = {
    def reportError(error: Throwable): Observable[Int] = {
      logger.error("error", error)
      Observable.empty
    }
    val o1 = List(1, 2, 3, 0, 4) toObservable
    val o2: Observable[Int] = o1.map(i => 12/i).onErrorResumeNext(reportError)
    o2 subscribe(println(_))
  }

}
