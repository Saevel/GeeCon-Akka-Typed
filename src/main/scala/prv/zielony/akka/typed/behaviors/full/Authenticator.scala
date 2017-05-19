package prv.zielony.akka.typed.behaviors.full

import akka.typed.Behavior
import akka.typed.scaladsl.Actor
import prv.zielony.akka.typed.behaviors.full.UserGuardian.AuthenticateUser

object Authenticator {

  def apply(users: Seq[User]): Behavior[AuthenticateUser] = Actor.immutable{ (_, command) =>
    command.replyTo ! users.find(user => user.credentials.name == command.credentials.name &&
      user.credentials.password == command.credentials.password).map(_.id)
    Actor.same
  }
}
