/**
  * GlobalState
  *
  * @author 01372461
  */
class GlobalState {

  /**
    * versioned favourite movie
    */
  type VFM = (String, Int)
  type PORT = Int

  /**
    * Global states
    */
  var state: Map[PORT, VFM] = Map()


  def updateState(s: Map[PORT, VFM]): Unit = {
    val m: Seq[(PORT, VFM)] = state.toSeq ++ s.toSeq
    val g: Map[PORT, Seq[(PORT, (String, PORT))]] = m.groupBy(_._1)
    val newState = g.mapValues(_.sortBy(_._2._2).reverse.head._2)
    state = newState
  }

  private def _renderState(s: Map[PORT, VFM]) = {
    s.toSeq.sortBy(_._1).map {
      case (port, (movie, version)) => s"$port currently likes $movie ($version)"
    }.foreach(println)
  }

  def renderState() = _renderState(state)

  def removeState(port: Int) = {
    val newState = state - port
    state = newState
  }
}

object GlobalState {
  def apply(): GlobalState = new GlobalState()
}
