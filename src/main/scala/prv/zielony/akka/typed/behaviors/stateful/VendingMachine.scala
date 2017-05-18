package prv.zielony.akka.typed.behaviors.stateful

import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, Behavior}
import akka.typed.scaladsl.Actor._
import prv.zielony.akka.typed.behaviors.stateful.VendingMachineInput._
import prv.zielony.akka.typed.behaviors.stateful.VendingMachineOutput._

object VendingMachine {

  def apply(client: ActorRef[VendingMachineOutput.Value]) = idleStageBehavior(client)

  private def idleStageBehavior(client: ActorRef[VendingMachineOutput.Value]): Behavior[VendingMachineInput.Value] = Actor.immutable[VendingMachineInput.Value]{ (_, command) =>
    command match {
      case Order => orderedStageBehavior(client)
      case Payment => paidStageBehavior(client)
      case Timeout => same[VendingMachineInput.Value]
    }
  }

  private def orderedStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = immutable[VendingMachineInput.Value]{ (_, command) =>
    command match {
      case Order => same[VendingMachineInput.Value]
      case Payment => {
        client ! Item
        idleStageBehavior(client)
      }
      case Timeout => idleStageBehavior(client)
    }
  }

  private def paidStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = immutable[VendingMachineInput.Value]{ (_, command) =>
    command match {
      case Order => {
        client ! Item
        idleStageBehavior(client)
      }
      case Payment => same[VendingMachineInput.Value]
      case Timeout => {
        client ! Return
        idleStageBehavior(client)
      }
    }
  }
}