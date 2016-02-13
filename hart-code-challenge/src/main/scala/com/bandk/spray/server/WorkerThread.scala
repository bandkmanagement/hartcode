package com.bandk.spray.server

import akka.actor.Actor
import akka.actor.ActorLogging

class WorkerThread extends Actor 
with ActorLogging {

  // 
  // Add Actor messaging
  //
  def receive = {
    case FutureMessage =>

  }
  
}