package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.mvc.{AbstractController, ControllerComponents}
import services.HaseeBcoinService

/**
  * HaseeBcoinController
  *
  * @author 01372461
  */
@Singleton
class HaseeBcoinController @Inject()(cc: ControllerComponents,
                                     bcoinService: HaseeBcoinService) extends AbstractController(cc) with Logging {

  def index = Action {
    Ok("Hello world")
  }

  /**
    * Create user
    *
    * @param name  user name
    * @return
    */
  def users(name: String) = Action {
    bcoinService.createUser(name)
    printState()
    Ok(s"create user $name")
  }

  /**
    * Get balance
    *
    * @param name user name
    * @return
    */
  def balance(name: String) = Action {
    bcoinService.getBalance(name)
    printState()
    Ok(s"Get $name balance")
  }


  /**
    * transfer money
    *
    * @param from
    * @param to
    * @param amount
    * @return
    */
  def transfer(from: String, to: String, amount: Double) = Action {
    bcoinService.transfer(from, to, amount)
    printState()
    Ok(s"transfer $amount from $from to $to")
  }


  private def printState(): Unit = {
    import play.utils.Colors
    val allState = bcoinService.allUserState().toString()
    logger.info(Colors.green(allState))
  }
}
