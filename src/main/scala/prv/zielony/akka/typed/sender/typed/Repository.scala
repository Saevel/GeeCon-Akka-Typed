package prv.zielony.akka.typed.sender.typed

import akka.typed.ScalaDSL.Static
import prv.zielony.akka.typed.sender.Item

object Repository {

  def apply(items: Seq[Item]) = Static[GetItems]{ request =>
    request.replyTo ! ItemsFound(items.filter(item => item.kind == request.kind))
  }
}
