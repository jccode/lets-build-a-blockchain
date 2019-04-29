package client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Client
  *
  * @author 01372461
  */
class Client(val ws: WSClient) {

  val URL = "http://localhost"

  def gossip(port: Int, state: Map[Int, (String, Int)]): Future[Option[Map[Int, (String, Int)]]] = {
    val strState = Json.stringify(Json.toJson(state.map(m => (m._1.toString, m._2))))
    val future = ws.url(s"$URL:$port/gossip").withQueryStringParameters("state" -> strState).post("")
      .map { r =>
        Json.fromJson[Map[String, (String, Int)]](Json.parse(r.body))
          .map(_.map(m => (m._1.toInt, m._2)))
          .asOpt
      }
    future
  }

}

object Client {

  def apply(): Client = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val ws = AhcWSClient()
    new Client(ws)
  }

  def apply(ws: WSClient) = new Client(ws)
}
