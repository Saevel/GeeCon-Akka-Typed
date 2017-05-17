package prv.zielony.akka.typed.problems.types.problem

import akka.actor.{ActorSystem, Props}
import prv.zielony.akka.typed.problems.types.{Group, Role, User}
import akka.pattern.ask
import akka.typed.scaladsl.Actor
import akka.util.Timeout

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