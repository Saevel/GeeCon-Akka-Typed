package prv.zielony.akka.typed.problems.types.problem

import akka.actor.Actor
import prv.zielony.akka.typed.problems.types.{Role, User}

/**
  * Created by kamil on 2017-05-17.
  */
class AuthorizationActor(roles: Seq[Role]) extends Actor {

  override def receive: Receive = {
    case user: User => sender ! verifyUser(user)
  }

  private def verifyUser(user: User) = roles.forall(role => user.roles.contains(role)) || (
    user.groups.exists(group => roles.forall(role => group.roles.contains(role)))
  )
}
