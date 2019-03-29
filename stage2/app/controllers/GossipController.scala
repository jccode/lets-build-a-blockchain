package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class GossipController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with Logging {

  // Codes in constructor will run on startup.
  logger.info("===== STARTUP =====")


  def index = Action {
    Ok("Hello world")
  }

  def gossip(state: String) = Action {
    println(s"their state: $state")
    Ok("gossip")
  }

}
