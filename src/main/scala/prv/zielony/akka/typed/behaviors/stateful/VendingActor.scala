package prv.zielony.akka.typed.behaviors.stateful

import java.util

import akka.typed.ActorRef
import akka.typed.scaladsl.Actor._

/**
  * Created by kamil on 2017-05-17.
  */
object VendingActor {

  object VendingMachineOutput extends Enumeration {
    val Return, Item = Value
  }

  object VendingMachineProtocol extends Enumeration {
    val Order, Payment, Timeout = Value
  }

  import VendingMachineProtocol._
  import VendingMachineOutput._

  def apply(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineProtocol.Value]{ (_, command) =>
   command match {
     case Order => orderedStageBehavior(client)
     case Payment => paidStageBehavior(client)
     case Timeout => Same[VendingMachineProtocol.Value]
   }
  }

  private def orderedStageBehavior(clientActor: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineProtocol.Value]{ (_, command) =>
    command match {
      case Order => Same[VendingMachineProtocol.Value]
      case Payment => ???
    }
  }

  private def paidStageBehavior(client: ActorRef[VendingMachineOutput.Value]) = Stateful[VendingMachineProtocol.Value]{ (_, command) =>
    ???
  }
}