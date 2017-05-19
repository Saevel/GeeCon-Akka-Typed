package prv.zielony.akka.typed.behaviors.full

import akka.typed.Behavior
import akka.typed.scaladsl.Actor
import prv.zielony.akka.typed.behaviors.full.UserGuardian.CheckContact

object ContactProvider {

  def apply(users: Seq[User]): Behavior[CheckContact] = Actor.immutable { (_, command) =>
    command.replyTo ! users.find(user => user.id == command.id).flatMap(_.contactData.email)
    Actor.same
  }
}
