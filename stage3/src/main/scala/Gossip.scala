
import akka.actor.Cancellable

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random


/**
  * Gossip
  *
  * @author 01372461
  */
class Gossip(val port: Int, val peerPort: Int, val globalState: GlobalState, val playServer: EmbeddingPlayServer, val client: Client) {

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
    * Return a random movie
    * @return
    */
  def randomMovie(): String = movies(Random.nextInt(movies.length))


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



  /*
  Init value
   */
  globalState.updateState(Map(port -> (null, 0), peerPort -> (null, 0)))


  every(8 seconds, () => {
    println(s"Screw <$favoriteMovie>")
    versionNumber += 1
    favoriteMovie = randomMovie()
    globalState.updateState(Map(port -> (favoriteMovie, versionNumber)))
    println(s"New favorite is <$favoriteMovie> ($versionNumber)")
  })

  every(3 seconds, () => {
    println("Exchange with peer port")
    globalState.state.keys.filter(_ != port).foreach { p =>
      val res: Future[Option[Map[Int, (String, Int)]]] = client.gossip(p, globalState.state)
      try {
        Await.result(
          res.map {
            case Some(state) => globalState.updateState(state)
            case None => globalState.removeState(port)
          }
          , 5 seconds)
      } catch {
        case e: Exception => {
          println(s"Cannot connect to $p")
          //globalState.removeState(p)
        }
      }
    }
  })
}
