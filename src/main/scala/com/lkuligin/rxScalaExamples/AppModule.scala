package com.lkuligin.rxScalaExamples

import java.util.concurrent.{ExecutorService, Executors, ThreadPoolExecutor, TimeUnit}

import com.google.common.util.concurrent.MoreExecutors
import com.google.inject.{AbstractModule, Provides, Singleton}
import net.codingwell.scalaguice.ScalaModule

/**
  * Created by lkuligin on 26/01/2017.
  */
class AppModule  extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
  }

  @Provides @Singleton
  def configuration: Configuration = new Configuration()

  @Provides @Singleton
  def example1: Example1 = new Example1()

  @Provides @Singleton
  def example2: Example2 = new Example2()

  @Provides @Singleton
  def httpServer(config: Configuration) = new HttpServer(1900)

  @Provides @Singleton
  def executorService: ExecutorService = MoreExecutors.getExitingExecutorService(
    Executors.newCachedThreadPool().asInstanceOf[ThreadPoolExecutor], 20, TimeUnit.SECONDS
  )
}
