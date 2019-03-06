import com.google.inject.AbstractModule
import services.{HaseeBcoinService, HaseeBcoinServiceImpl}

/**
  * Module
  *
  * @author 01372461
  */
class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[HaseeBcoinService]).to(classOf[HaseeBcoinServiceImpl])
  }

}
