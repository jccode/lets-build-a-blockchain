package services

import javax.inject.Singleton


trait GossipService {

  /**
    * Versioned favourite movie
    */
  type VersionMovie = (String, Int)
  type Port = Int
  type State = Map[Port, VersionMovie]

  def state: State

  def updateState(s: State): Unit

  def renderState(): Unit

  def removeState(port: Port): Unit
}

/**
  * GossipService
  *
  * @author 01372461
  */
@Singleton
class GossipServiceImpl extends GossipService {

  var _state: Map[Port, VersionMovie] = Map[Port, VersionMovie]()

  override def state: State = _state

  override def updateState(s: State): Unit = {
    val m: Seq[(Port, VersionMovie)] = state.toSeq ++ s.toSeq
    val g: Map[Port, Seq[(Port, VersionMovie)]] = m.groupBy(_._1)
    val newState = g.mapValues(_.sortBy(_._2._2).reverse.head._2)
    _state = newState
  }

  override def renderState(): Unit = {
    state.toSeq.sortBy(_._1).map {
      case (port, (movie, version)) => s"$port currently likes $movie ($version)"
    }.foreach(println)
  }

  override def removeState(port: Port): Unit = {
    val newState = state - port
    _state = newState
  }

}
