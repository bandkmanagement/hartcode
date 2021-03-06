package com.bandk.spray.server

import java.util.Date

trait Title {
  def imdbID: String
  def year: Int
  def title: String
  def posterUrl: String
  def isSeries: Boolean
  def isSimpleMovie: Boolean
}

case class Movie(imdbID: String, year: Int, title: String, posterUrl: String) extends Title {
  def isSeries: Boolean = false
  def isSimpleMovie: Boolean = true
}

case class SeriesTitle(imdbID: String, year: Int, title: String, posterUrl: String) extends Title {
  def isSeries: Boolean = true
  def isSimpleMovie: Boolean = false
}

case class Season(parentImdbID: String, seasonNumber: Int, episodes: Int)
case class Episode(imdbID: String, season: Int, number: Int, seriesImdbId: String)
case class SubEntry(number: Int, start: Date, stop: Date, text: String)
case class Subtitle(id: String, imdbId: String)
case class SubtitleWithContent(subtitle: Subtitle, content: String)
case class IMovie(otherId: Long, title: String, year: Int, rating: BigDecimal, votes: Long, genre: String, poster: String, imdbId: Option[String])

sealed trait TitleType {
  def discriminator: String
  def name: String
}

object TitleType {
  val types = Set(MovieType, Series)
  val typesByDiscriminator: Map[String, TitleType] = types.flatMap {
    t => Seq(t.discriminator -> t, t.name -> t)
  }.toMap
}

case object MovieType extends TitleType {
  override def discriminator: String = "m"
  override def name: String = "movie"
}
case object Series extends TitleType {
  override def discriminator: String = "s"
  override def name: String = "series"
}