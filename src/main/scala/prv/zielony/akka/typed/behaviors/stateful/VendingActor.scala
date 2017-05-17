package prv.zielony.akka.typed.behaviors.stateful

import akka.typed.ActorRef
import akka.typed.scaladsl.Actor._

/**
  * Created by kamil on 2017-05-17.
  */
object VendingActor {

  object VendingMachineOutput extends Enumeration {
    val Return, Item = Value
  }

  object VendingMachineInput extends Enumeration {
    val Order, Payment, Timeout = Value
  }

  import VendingMachineInput._
  import VendingMachineOutput._

  def apply(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineInput.Value]{ (_, command) =>
    idleStageBehavior(client)
  }

  private def idleStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineInput.Value]{ command =>
    command match {
      case Order => orderedStageBehavior(client)
      case Payment => paidStageBehavior(client)
      case Timeout => Same[VendingMachineInput.Value]
    }
  }

  private def orderedStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineInput.Value]{ (_, command) =>
    command match {
      case Order => Same[VendingMachineInput.Value]
      case Payment => {
        client ! Item
        idleStageBehavior(client)
      }
      case Timeout => idleStageBehavior(client)
    }
  }

  private def paidStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineInput.Value]{ (_, command) =>
    command match {
      case Order => {
        client ! Item
        idleStageBehavior(client)
      }
      case Payment => Same[VendingMachineInput.Value]
      case Timeout => {
        client ! Return
        idleStageBehavior(client)
      }
    }
  }
}