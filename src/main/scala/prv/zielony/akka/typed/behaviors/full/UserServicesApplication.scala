package prv.zielony.akka.typed.behaviors.full

import akka.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.typed.scaladsl.AskPattern._
import prv.zielony.akka.typed.behaviors.full.UserGuardian.{AuthenticateUser, CheckContact}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object UserServicesApplication extends App {

  implicit val timeout: Timeout = 3 seconds

  val guardian = ActorSystem("Guardian", UserGuardian(Seq(
   User("1", Credentials("username1", "password1"), ContactData(None, Some("kamil@dim.pl"), None), PersonalData("Kamil", "Owczarek", 27)),
   User("2", Credentials("username2", "password2"), ContactData(None, Some("person@domain.pl"), None), PersonalData("Random", "User", 33))
  )))

  implicit val scheduler = guardian.scheduler

  val contacts = guardian ? {ref: ActorRef[Option[String]] => CheckContact("2", ref)}
  val ids = guardian ? {ref: ActorRef[Option[String]] => AuthenticateUser(Credentials("username2", "password2"), ref)}

  val contactData = Await.result(contacts, 3 seconds)
  val id = Await.result(ids, 3 seconds)

  println(s"Id: $id. Contact: ${contactData}")

  guardian.terminate
}
