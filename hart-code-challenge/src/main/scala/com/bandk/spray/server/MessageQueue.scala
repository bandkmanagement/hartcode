package com.bandk.spray.server

import akka.actor.{ActorLogging, Actor}

class MessageQueue extends Actor 
with ActorLogging {

  val FILE_PATH="c:\\dev\\"
  
  def receive = {
    case PersistMovieData(movieData) =>
      Utils.writeStringIntoFile(movieData, FILE_PATH++java.util.UUID.randomUUID.toString+".dat")

  }
}