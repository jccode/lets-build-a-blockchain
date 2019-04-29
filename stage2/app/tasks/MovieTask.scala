package tasks

import akka.actor.ActorSystem
import client.Client
import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.{Configuration, Logging, Play}
import services.GossipService

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random

/**
  * MovieTask
  *
  * @author 01372461
  */
class MovieTask @Inject()(actorSystem: ActorSystem,
                          gs: GossipService,
                          config: Configuration,
                          ws: WSClient)
                         (implicit executionContext: ExecutionContext) extends Logging {

  def every(duration: FiniteDuration, runnable: Runnable) =
    actorSystem.scheduler.schedule(0 second, duration, runnable)

  val port = config.getOptional[Int]("http.port").getOrElse(9000)
  val peerPort = config.getOptional[Int]("peerPort").getOrElse(9001)
  val client = Client(ws)

  /**
    * All movies
    */
  val movies = Source.fromResource("movies.txt").getLines().toList

  /**
    * current favourite movie
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

  /*
  Init value
   */
  gs.updateState(Map(port -> (null, 0), peerPort -> (null, 0)))


  every(8 seconds, () => {
    logger.info(s"Screw <$favoriteMovie>")
    versionNumber += 1
    favoriteMovie = randomMovie()
    logger.info(s"current port: $port")
    gs.updateState(Map(port -> (favoriteMovie, versionNumber)))
    logger.info(s"New favorite is <$favoriteMovie> ($versionNumber)")
  })

  every(3 seconds, () => {
    logger.info(s"Exchange with peer port: $peerPort")
    gs.state.keys.filter(_ != port).foreach { p =>
      val res: Future[Option[Map[Int, (String, Int)]]] = client.gossip(p, gs.state)
      try {
        Await.result(
          res.map {
            case Some(state) => gs.updateState(state)
            case None => gs.removeState(port)
          }
          , 5 seconds
        )
      } catch {
        case e: Exception =>
          logger.error(s"Cannot connect to $p")
      }
    }
  })

}
