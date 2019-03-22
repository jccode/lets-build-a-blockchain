/**
  * Client
  *
  * @author 01372461
  */
object Client {

  val URL = "http://localhost"
  val PORT = 9000


  def create_user(name: String): Unit = {
    println(s"create user $name")
  }

  def get_balance(user: String): Unit = {
    println(s"get balance $user")
  }

  def transfer(from: String, to: String, amount: Double): Unit = {
    println(s"transfer from $from to $to $amount")
  }
}
