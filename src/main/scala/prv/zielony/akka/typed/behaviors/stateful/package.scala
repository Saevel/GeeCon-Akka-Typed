package prv.zielony.akka.typed.behaviors

package object stateful {

  object VendingMachineOutput extends Enumeration {
    val Return, Item = Value
  }

  object VendingMachineInput extends Enumeration {
    val Order, Payment, Timeout = Value
  }
}
