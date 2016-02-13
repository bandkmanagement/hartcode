package com.bandk.spray.server

import akka.actor.ActorSystem
import spray.routing._
import spray.http.MediaTypes
import akka.actor.Props
import akka.actor.ActorLogging
import akka.actor.Actor

	// ====================
	// ===== Messages =====
	// ====================
	sealed trait PersistMovieMessage
  case class PersistMovieData(movieData: String) extends PersistMovieMessage
  case class FutureMessage() extends PersistMovieMessage


object MovieDetailServer extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem("MovieDetailServer")
  
  val messagequeue = actorSystem.actorOf(Props(new MessageQueue), "messagequeue")
  
  //
  // This could be an actor pool
  //
  val consumer1 = actorSystem.actorOf(Props(new WorkerThread), "consumer1")

  def getJson(route: Route) = get {
    respondWithMediaType(MediaTypes.`application/json`) { route }
  }
  
  // email to john@hart.com
  startServer(interface = "localhost", port = 8080) {
    getJson {
      path("movie" / Segment / "details") { (movieToFind:String) =>
        complete {
          val movieSearcher:OmdbApi = new OmdbApi()
          val movieInfo = movieSearcher.searchMovie(movieToFind)
          
          if (movieInfo.isDefined) {
            val movieInfoString = movieInfo.get.toString()
            // Send movie data to work queue from consumer 1
             messagequeue.tell(PersistMovieData(movieInfoString), consumer1)
            //
            movieInfoString
          } else
          {
            "Ok".toString
          }
        }
      }
    }
  }
  
}
