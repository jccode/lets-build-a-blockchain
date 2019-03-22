import javax.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable}

/**
  * Client.
  *
  * Usage:
  *
  * 1. Launch server
  *
  *        sbt stage1/run
  *
  *
  * 2. Launch client console
  *
  *        sbt stage1/console
  *        :load stage1/app/Client.scala
  *        val c = Client()
  *        c.get_balance("michael")
  *        c.create_user("sara")
  *        c.transfer("michael", "sara", 5000)
  *
  *
  *
  * @author 01372461
  */
class Client @Inject() (ws: WSClient) {

  val URL = "http://localhost"
  val PORT = 9000

  private def exec[T](awaitable: Awaitable[T]): Unit = println(Await.result(awaitable, 5 second))

  def index(): Unit = {
    exec(ws.url(s"$URL:$PORT").get.map(_.body))
  }

  def create_user(name: String): Unit = {
    exec(ws.url(s"$URL:$PORT/users?name=$name").post("").map(_.body))
  }

  def get_balance(user: String): Unit = {
    exec(ws.url(s"$URL:$PORT/balance/$user").get.map(_.body))
  }

  def transfer(from: String, to: String, amount: Double): Unit = {
    /*
    import play.api.http.HeaderNames._
    import play.api.http.MimeTypes._
    val data = Map[String, Seq[String]]("from" -> Seq(from), "to" -> Seq(to), "amount" -> Seq(amount.toString))
    val data = Map[String, String]("from" -> from, "to" -> to, "amount" -> amount.toString)
    exec(ws.url(s"$URL:$PORT/transfers").withHttpHeaders(CONTENT_TYPE -> FORM).withRequestFilter(AhcCurlRequestLogger()).post(data).map(_.body))
    */

    exec(ws.url(s"$URL:$PORT/transfers")
      .withQueryStringParameters("from" -> from, "to" -> to, "amount" -> amount.toString)
      .post("")
      .map(_.body))
  }

}

object Client {

  // Launch the interactive console
  // Ref: https://www.playframework.com/documentation/2.7.x/PlayConsole#Launch-the-interactive-console
  import play.api._
  val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
  val context = ApplicationLoader.Context.create(env)
  val loader = ApplicationLoader(context)
  val app = loader.load(context)
  Play.start(app)

  def apply(): Client = app.injector.instanceOf(classOf[Client])

}
