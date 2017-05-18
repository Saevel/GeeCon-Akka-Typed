package prv.zielony.akka.typed.sender.typed

import akka.actor.Scheduler
import akka.typed.ActorRef
import akka.typed.scaladsl.Actor._
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout
import prv.zielony.akka.typed.sender.Item

import scala.concurrent.{ExecutionContext, Future}

object Buyer {

  def apply(strategy: (Item, Item) => Item)(repositories: Seq[ActorRef[GetItems]])
           (implicit context: ExecutionContext, timeout: Timeout, scheduler: Scheduler) = immutable[GetOptimalItem]{ (_, command) =>
    Future.sequence(
      repositories.map(repository =>
        repository ? {replyTo: ActorRef[ItemsFound] => GetItems(command.kind, replyTo)}
      )
    ).map(commands => commands.flatMap(command => command.item))
      .map(items => items.reduce(strategy))
      .foreach(item => command.replyTo ! item)

    same
  }
}