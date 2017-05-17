package prv.zielony.akka.typed.behaviors.stateful

import akka.actor.{Actor, ActorRef}

import prv.zielony.akka.typed.behaviors.stateful.VendingMachineInput._
import prv.zielony.akka.typed.behaviors.stateful.VendingMachineOutput._

class UntypedVendingMachine(client: ActorRef) extends Actor {

  import VendingMachineState._

  var state: VendingMachineState.Value = Idle

  override def receive: Receive = {
    case Timeout => state match {
      case Idle => {}
      case Paid => {
        client ! Return
        state = Idle
      }
      case Ordered => {
        state = Idle
      }
    }
    case Payment => state match {
      case Idle => {
        state = Paid
      }
      case Paid => {}
      case Ordered => {
        client ! Item
        state = Idle
      }
    }
    case Order => state match {
      case Idle => {
        state = Ordered
      }
      case Paid => {
        client ! Item
        state = Idle
      }
      case Ordered => {}
    }
  }
}

object VendingMachineState extends Enumeration {
  val Idle, Paid, Ordered = Value
}