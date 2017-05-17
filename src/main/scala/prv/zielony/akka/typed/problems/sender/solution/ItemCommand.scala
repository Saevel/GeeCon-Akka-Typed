package prv.zielony.akka.typed.problems.sender.solution

import akka.typed.ActorRef
import prv.zielony.akka.typed.problems.sender.Item

sealed trait ItemCommand

case class GetOptimalItem(kind: String, replyTo: ActorRef[Item])

case class GetItems(kind: String, replyTo: ActorRef[ItemsFound])

case class ItemsFound(item: Seq[Item])