import com.google.inject.AbstractModule
import services.{GossipService, GossipServiceImpl}
import tasks.MovieTask

/**
  * Module
  *
  * @author 01372461
  */
class Module extends AbstractModule {

  override def configure(): Unit = {
    // services
    bind(classOf[GossipService]).to(classOf[GossipServiceImpl])

    // tasks
    bind(classOf[MovieTask]).asEagerSingleton()
  }
}
