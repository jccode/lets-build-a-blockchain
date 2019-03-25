
import akka.actor.Cancellable

import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random


/**
  * Gossip
  *
  * @author 01372461
  */
class Gossip(val port: Int, val peerPort: Int) {

  //val logger = Logger(getClass)

  /**
    * All movies
    */
  val movies = Source.fromResource("movies.txt").getLines().toList

  /**
    * current favorite movie
    */
  var favoriteMovie: String = randomMovie()

  /**
    * current version number
    */
  var versionNumber: Int = 0




  /**
    * versioned favourite movie
    */
  type VFM = (String, Int)
  type PORT = Int


  /**
    * Global states
    */
  var state: Map[PORT, VFM] = Map()

  /**
    * Return a random movie
    * @return
    */
  def randomMovie(): String = movies(Random.nextInt(movies.length))


  /**
    * start play server
    */
  val playServer = new EmbeddingPlayServer(port)

  import playServer.components.actorSystem.dispatcher


  /**
    * Helper functions
    * @param duration
    * @param runnable
    * @return
    */
  def every(duration: FiniteDuration, runnable: Runnable): Cancellable = {
    playServer.components.actorSystem.scheduler.schedule(0 second, duration, runnable)
  }


  def updateState(s: Map[PORT, VFM]): Map[PORT, VFM] = {
    val m: Seq[(PORT, VFM)] = state.toSeq ++ s.toSeq
    val g: Map[PORT, Seq[(PORT, (String, PORT))]] = m.groupBy(_._1)
    g.mapValues(_.sortBy(_._2._2).reverse.head._2)
  }

  def renderState(s: Map[PORT, VFM]) = {
    s.toSeq.sortBy(_._1).map {
      case (port, (movie, version)) => s"$port currently likes $movie ($version)"
    }.foreach(println)
  }


  /*
  Constructors
 */
  every(8 seconds, () => {
    println(s"Screw <$favoriteMovie>")
    versionNumber += 1
    favoriteMovie = randomMovie()
    state = updateState(Map(port -> (favoriteMovie, versionNumber)))
    println(s"New favorite is <$favoriteMovie> ($versionNumber)")
  })


}
