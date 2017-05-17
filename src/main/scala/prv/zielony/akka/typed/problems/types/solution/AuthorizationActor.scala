package prv.zielony.akka.typed.problems.types.solution

import akka.typed.ScalaDSL._
import prv.zielony.akka.typed.problems.types.{Member, Role, User}


object AuthorizationActor {

  def apply(roles: Seq[Role]) = Static[Member]{
    case user: User => {
      if(verifyUser(user, roles)) println("User Authorized") else println("User not authorized")
    }
  }

  private def verifyUser(user: User, roles: Seq[Role]) = roles.forall(role => user.roles.contains(role)) || (
    user.groups.exists(group => roles.forall(role => group.roles.contains(role)))
    )
}
