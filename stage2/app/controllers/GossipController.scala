package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.GossipService

@Singleton
class GossipController @Inject()(cc: ControllerComponents, gs: GossipService) extends AbstractController(cc) with Logging {

  // Codes in constructor will run on startup.
  logger.info("===== STARTUP =====")


  def index = Action {
    Ok("Hello world")
  }

  def gossip(state: String) = Action {
    logger.info(s"Their state: $state")

    // merge their state
    val theirState: Option[Map[Int, (String, Int)]] =
      Json.fromJson[Map[String, (String, Int)]](Json.parse(state)).asOpt
        .map(_.map(m => (m._1.toInt, m._2)))
    theirState.foreach(gs.updateState)
    gs.renderState()

    // return new state
    val newState: Map[String, (String, Int)] = gs.state.map(m => (m._1.toString, m._2))
    Ok(Json.stringify(Json.toJson(newState)))
  }

}
