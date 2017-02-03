package com.lkuligin.rxScalaExamples

/**
  * Created by lkuligin on 03/02/2017.
  */
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler

import scala.util.control.NonFatal

class HttpServer(port: Int) extends LazyLogging {
  val server = new Server(port)

  def start(): Unit = {
    val context = new ContextHandler()
    context.setContextPath("/diagnostics/heartbeat")
    context.setHandler(new DiagnosticHandler())

    server.setHandler(context)
    try {
      server.start()
    } catch {
      case NonFatal(ex) => logger.error(s"Error starting http server, details: ${ex.getMessage}")
    }
  }

  def stop(): Unit = {
    server.stop()
  }
}