import java.io.File

import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._
import play.core.server.{AkkaHttpServer, ServerConfig}
import play.api.mvc.Results._

/**
  * EmbeddingPlayServer
  *
  * @author 01372461
  */
class EmbeddingPlayServer(val globalState: GlobalState, port: Int = 9000) {

  val context: ApplicationLoader.Context = ApplicationLoader.Context.create(Environment.simple(path = new File("."), mode = Mode.Dev))

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  val components: BuiltInComponentsFromContext with NoHttpFiltersComponents = new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
    override def router: Router = Router.from(routes())
  }

  val server: AkkaHttpServer = AkkaHttpServer.fromApplication(components.application, ServerConfig(
    address = "localhost",
    port = Some(port.toInt)
  ))

  def routes(): Router.Routes = {

    case POST(p"/gossip" ? q"state=$state") => mvc.Action {
      println("their state: " + state)

      // merge their state
      val theirState: Option[Map[Int, (String, Int)]] =
        Json.fromJson[Map[String, (String, Int)]](Json.parse(state)).asOpt
            .map(_.map(m => (m._1.toInt, m._2)))
      theirState.foreach( globalState.updateState )
      globalState.renderState()

      // return new state
      val newState: Map[String, (String, Int)] = globalState.state.map(m => (m._1.toString, m._2))
      Ok(Json.stringify(Json.toJson(newState)))
    }

  }
}
