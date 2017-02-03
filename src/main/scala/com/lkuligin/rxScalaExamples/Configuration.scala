package com.lkuligin.rxScalaExamples

import com.typesafe.config.ConfigFactory

/**
  * Created by lkuligin on 26/01/2017.
  */
class Configuration {
  lazy val config = ConfigFactory.load()
}
