package com.bandk.spray.server

import org.json4s._
import org.json4s.jackson.JsonMethods._

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.response.Response
import java.net.URL

trait MovieSearcher {
  def searchMovieJson(title: String): String
  def searchMovie(title: String): Option[String]
}

class OmdbApi extends MovieSearcher  {
//  implicit val formats = DefaultFormats + new TitleTypeSerializer
  implicit val formats = DefaultFormats
  val WEBSERVICE_URL = "http://www.omdbapi.com/"

  def searchMovieJson(title: String): String = {
    
    val sanitizedTitle = title.replaceAll(" ", "+")
    val urlToCall = s"$WEBSERVICE_URL?t=$sanitizedTitle"
    val httpClient = new HttpClient
    val response: Response = httpClient.get(new URL(urlToCall))
    val jsonString = response.body.asString
    jsonString
  }

  private def extractYear(s: String): BigInt = {
    BigInt(s.substring(0, 4))
  }

  def searchMovie(title: String): Option[String] = {
    Option(searchMovieJson(title))
  }

  def parseMovie(jsonString: String): Option[Movie] = {
    
    val json = parse(jsonString,false)
    val modified = json transformField {
      case ("Title", x) => ("title", x)
      case ("Year", x) => ("year", JInt(extractYear(x.extract[String])))
      case ("Poster", x) => ("posterUrl", x)
      case ("Type", x) => ("movieType", x)
    }
    try {
      Some(modified.extract[Movie])
    } catch {
      case e: Throwable =>
        //logger.error("Unable to parse movie json: " + jsonString, e)
        None
    }
  }
}