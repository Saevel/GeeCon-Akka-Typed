package prv.zielony.akka.typed.sender.typed

import akka.typed.Behavior
import akka.typed.scaladsl.Actor._
import prv.zielony.akka.typed.sender.Item

object Repository {

  def apply(items: Seq[Item]): Behavior[GetItems] = immutable[GetItems]{ (_, request) =>
    request.replyTo ! ItemsFound(items.filter(item => item.kind == request.kind))
    same
  }
}
