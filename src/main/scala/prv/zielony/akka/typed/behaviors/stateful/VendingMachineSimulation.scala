package prv.zielony.akka.typed.behaviors.stateful

import scala.concurrent.ExecutionContext.Implicits.global
import akka.typed.ActorSystem
import akka.typed.scaladsl.Actor._
import akka.util.Timeout
import prv.zielony.akka.typed.behaviors.tap.LoggedActor
import prv.zielony.akka.typed.behaviors.stateful.VendingMachineInput._

import scala.concurrent.Await

object VendingMachineSimulation extends App {

  import scala.concurrent.duration._

  implicit val timeout: Timeout = 300 millis

  val client = ActorSystem("Client", immutable[VendingMachineOutput.Value]{ (_, output) =>
    println(s"Vending machine output: $output")
    same
  })

  val vendingMachineFuture = client.systemActorOf(LoggedActor(VendingMachine(client)), "VendingMachine")

  val vendingMachine = Await.result(vendingMachineFuture, 3 seconds)

  vendingMachine ! Payment
  vendingMachine ! VendingMachineInput.Timeout
  vendingMachine ! Order
  vendingMachine ! VendingMachineInput.Timeout
  vendingMachine ! Order
  vendingMachine ! Payment
  vendingMachine ! VendingMachineInput.Timeout
}