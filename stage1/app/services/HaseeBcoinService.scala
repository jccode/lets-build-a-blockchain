package services

import javax.inject.Singleton


trait HaseeBcoinService {

  type User = String
  type Amount = Double

  def createUser(user: User): Unit
  def getBalance(user: User): Option[Amount]
  def transfer(from: User, to: User, amount: Amount): Unit
  def allUserState(): Map[User, Amount]
}

/**
  * HaseeBcoinService
  *
  * @author 01372461
  */
@Singleton
class HaseeBcoinServiceImpl extends HaseeBcoinService {

  private var balances = scala.collection.mutable.Map[User, Amount]("michael" -> 100000)

  override def createUser(user: User): Unit = if (!balances.contains(user)) balances += (user -> 0)

  override def getBalance(user: User): Option[Amount] = balances.get(user)

  override def transfer(from: User, to: User, amount: Amount): Unit = {
    balances.get(from).foreach {fa =>
      balances.get(to).foreach {fb =>
        balances(from) = fa - amount
        balances(to) = fb + amount
      }
    }
  }

  override def allUserState(): Map[User, Amount] = balances.toMap
}
