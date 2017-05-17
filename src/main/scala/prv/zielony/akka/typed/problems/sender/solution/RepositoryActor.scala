package prv.zielony.akka.typed.problems.sender.solution

import akka.typed.ScalaDSL.Static
import prv.zielony.akka.typed.problems.sender.Item

object RepositoryActor {

  def apply(items: Seq[Item]) = Static[GetItems]{ request =>
    request.replyTo ! ItemsFound(items.filter(item => item.kind == request.kind))
  }
}
