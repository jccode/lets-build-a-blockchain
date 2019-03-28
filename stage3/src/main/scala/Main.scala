

object Main extends App {

  if (args.length != 2) {
    println("Usage Main <port> <peer_port>")

  } else {
    val Array(port, peerPort) = args
    println(s"port: $port; peer port: $peerPort")
    val globalState = GlobalState()
    val client = Client()
    val playServer = new EmbeddingPlayServer(globalState, port.toInt)
    val g = new Gossip(port.toInt, peerPort.toInt, globalState, playServer, client)

  }

}
