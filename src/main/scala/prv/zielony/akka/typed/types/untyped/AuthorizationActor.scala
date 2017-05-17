package prv.zielony.akka.typed.types.untyped

import akka.actor.Actor
import prv.zielony.akka.typed.types.{Role, User}

class AuthorizationActor(roles: Seq[Role]) extends Actor {

  override def receive: Receive = {
    case user: User => sender ! verifyUser(user)
  }

  private def verifyUser(user: User) = roles.forall(role => user.roles.contains(role)) || (
    user.groups.exists(group => roles.forall(role => group.roles.contains(role)))
  )
}
