# rxScalaExamples
The best way to mock independent observable is to use interval:
```scala
val o = Observable.interval(200 millis).take(5)
o.subscribe(n => System.out.println(s"observing interval = ${n}"))
```
will print nothing (the application ends before something was emmited)
***
Use *toBlocking*:
```scala
val o = Observable.interval(200 millis).take(5)
o.toBlocking.subscribe(n => System.out.println(s"observing interval = ${n}"))
```
Output:
```
observing interval = 0
observing interval = 1
observing interval = 2
observing interval = 3
observing interval = 4
```
***
Creating Observable with *.create*:
```scala
private def observableInt: Observable[Int] = {
    Observable(
      { observer => {
        try {
          val a = true
          if (!observer.isUnsubscribed) {
            (1 to 10) foreach {
              i => {
                System.out.println(s"observable: emitting ${i} and waiting for next item")
                observer.onNext(i)
                Thread.sleep(1000)
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
```
***
Only *.groupBy* will create an `Observable[partition[T], Observable[T1]]`, so it makes sense to use it together with flatMap:
```scala
def groupByExample = {
  val o: Observable[IntPair] =  observableInt
    .groupBy(_ % 2)
    .flatMap({
      case (partition, lines) => lines.map({el => new IntPair(partition, el)})
     })
   o subscribe  {el => {System.out.println(s"example of consuming ${el}")}}
 }
```
Output: 
```
observable: emitting 1 and waiting for next item
consuming Pair of 1 and 1
observable: emitting 2 and waiting for next item
consuming Pair of 0 and 2
observable: emitting 3 and waiting for next item
consuming Pair of 1 and 3
observable: emitting 4 and waiting for next item
consuming Pair of 0 and 4
observable: emitting 5 and waiting for next item
consuming Pair of 1 and 5
observable: emitting 6 and waiting for next item
consuming Pair of 0 and 6
observable: emitting 7 and waiting for next item
consuming Pair of 1 and 7
observable: emitting 8 and waiting for next item
consuming Pair of 0 and 8
observable: emitting 9 and waiting for next item
consuming Pair of 1 and 9
observable: emitting 10 and waiting for next item
consuming Pair of 0 and 10
```
