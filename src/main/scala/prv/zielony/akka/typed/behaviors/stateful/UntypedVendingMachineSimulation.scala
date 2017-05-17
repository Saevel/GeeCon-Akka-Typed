package prv.zielony.akka.typed.behaviors.stateful

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import prv.zielony.akka.typed.behaviors.stateful.VendingMachineInput._

object UntypedVendingMachineSimulation extends App {

  import scala.concurrent.duration._

  implicit val timeout: Timeout = 3 seconds

  val system = ActorSystem("VendingMachineSimulation")

  val receiver = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case message => println(s"Message from Vending Machine: $message")
    }
  }))

  val vendingMachine = system.actorOf(Props(new UntypedVendingMachine(receiver)))

  vendingMachine ! Payment
  vendingMachine ! VendingMachineInput.Timeout
  vendingMachine ! Order
  vendingMachine ! VendingMachineInput.Timeout
  vendingMachine ! Order
  vendingMachine ! Payment
  vendingMachine ! VendingMachineInput.Timeout

  Thread.sleep(300)

  system.terminate
}