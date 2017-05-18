package prv.zielony.akka.typed.types.typed

import akka.typed.scaladsl.Actor._
import prv.zielony.akka.typed.types.{Member, Role, User}


object AuthorizationActor {

  def apply(roles: Seq[Role]) = immutable[Member]{ (_, command) => command match {
    case user: User => {
      if(verifyUser(user, roles)) println("User Authorized") else println("User not authorized")
    }
  }
    same
  }

  private def verifyUser(user: User, roles: Seq[Role]) = roles.forall(role => user.roles.contains(role)) ||
    (user.groups.exists(group => roles.forall(role => group.roles.contains(role))))
}
