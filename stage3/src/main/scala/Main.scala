import java.io.File

import play.api._
import play.api.mvc._
import play.api.routing.Router
import play.api.routing.sird._
import play.core.server.{AkkaHttpServer, ServerConfig}

object Main extends App {

  if (args.length != 2) {
    println("Usage Main <port> <seed_port>")
  }
  else {
    val Array(port, seed_port) = args
    println(s"port: $port; seed port: $seed_port")
    launchEmbeddingPlayServer(port)
  }


  /**
    * Launch embedding play server
    *
    * @param port
    */
  def launchEmbeddingPlayServer(port: String) = {
    val context = ApplicationLoader.Context.create(Environment.simple(path = new File("."), mode = Mode.Dev))
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    val components = new BuiltInComponentsFromContext(context) with NoHttpFiltersComponents {
      import Results._

      override def router: Router = Router.from {
        case GET(p"/gossip/") => Action {
          Ok("gossip")
        }
      }
    }
    val server = AkkaHttpServer.fromApplication(components.application, ServerConfig(
      address = "127.0.0.1",
      port = Some(port.toInt)
    ))
  }
}
