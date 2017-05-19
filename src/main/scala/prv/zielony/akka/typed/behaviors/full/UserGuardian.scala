package prv.zielony.akka.typed.behaviors.full

import akka.typed.{ActorContext, ActorRef, Behavior, ExtensibleBehavior, PreRestart, Signal}
import akka.typed.scaladsl.Actor


object UserGuardian {

  def apply(users: Seq[User]): Behavior[UserCommand] = Actor.deferred{context =>
    val authenticator = context.spawn(Authenticator(users), "Authenticator")
    val contactProvder = context.spawn(ContactProvider(users), "ContactProvder")
    forwardMessages(authenticator, contactProvder)
  }

  private def forwardMessages(authenticator: ActorRef[AuthenticateUser], contactProvider: ActorRef[CheckContact]): Behavior[UserCommand] =
    Actor.immutable{ (_, command) =>
      command match {
        case authenticate: AuthenticateUser => authenticator ! authenticate
        case checkContact: CheckContact => contactProvider ! checkContact
      }
      Actor.same
  }

  sealed trait UserCommand {
    val replyTo: ActorRef[Option[String]]
  }

  case class AuthenticateUser(credentials: Credentials, replyTo: ActorRef[Option[String]]) extends UserCommand

  case class CheckContact(id: String, replyTo: ActorRef[Option[String]]) extends UserCommand
}
