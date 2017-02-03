package com.lkuligin.rxScalaExamples

/**
  * Created by lkuligin on 27/01/2017.
  */
case class IntPair(a: Int, b: Int) {
  override def toString = s"Pair of $a and $b"
}

case class FunnyExample(mod2: Int, maxEl: Int, elements: Seq[Int]) {
  override def toString = s"""all elements that mod 2 = $mod2, max element in buffer: $maxEl, elements: ${elements.mkString(":")}"""
}