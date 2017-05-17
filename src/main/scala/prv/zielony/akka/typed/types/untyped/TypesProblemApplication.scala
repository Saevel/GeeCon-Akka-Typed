package prv.zielony.akka.typed.types.untyped

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.typed.scaladsl.Actor
import akka.util.Timeout
import prv.zielony.akka.typed.types.{Group, Role}

import scala.concurrent.Await

object TypesProblemApplication extends App {

  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout: Timeout = 3 seconds

  val actorSystem = ActorSystem("TypesProblem")

  val managementRole = Role("ManagementUser")
  val userRole = Role("SystemUser")
  val portalAdminRole = Role("PortalAdministrator")
  val financeRole = Role("FinanceUser")
  val hrRole = Role("HRUser")

  val authorizationActor = actorSystem.actorOf(Props(new AuthorizationActor(
    Seq(managementRole, userRole, portalAdminRole)
  )))

  val authorization = (authorizationActor ? Group("PortalAdministrators", Seq(managementRole, userRole, portalAdminRole), Seq.empty))
    .mapTo[Boolean]

  val result = Await.result(authorization, 3 seconds)

  println(if(result) "AccessGranted" else "AccessDenied")

  actorSystem.terminate
}