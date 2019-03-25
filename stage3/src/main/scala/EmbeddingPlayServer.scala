import java.io.File

import play.api._
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._
import play.core.server.{AkkaHttpServer, ServerConfig}

/**
  * EmbeddingPlayServer
  *
  * @author 01372461
  */
class EmbeddingPlayServer(port: Int = 9000) {

  val context: ApplicationLoader.Context = ApplicationLoader.Context.create(Environment.simple(path = new File("."), mode = Mode.Dev))

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  val components: BuiltInComponentsFromContext with NoHttpFiltersComponents = new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
    import play.api.mvc.Results._

    override def router: Router = Router.from {
      case GET(p"/gossip/") => mvc.Action {
        Ok("gossip")
      }
    }
  }

  val server: AkkaHttpServer = AkkaHttpServer.fromApplication(components.application, ServerConfig(
    address = "127.0.0.1",
    port = Some(port.toInt)
  ))

}
