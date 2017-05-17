package prv.zielony.akka.typed.problems.sender.solution

import akka.actor.Scheduler
import akka.typed.ActorRef
import akka.typed.scaladsl.Actor.Stateless
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout
import prv.zielony.akka.typed.problems.sender.Item

import scala.concurrent.{ExecutionContext, Future}

object BuyerActor {

  def apply(strategy: (Item, Item) => Item)(repositories: Seq[ActorRef[GetItems]])
           (implicit context: ExecutionContext, timeout: Timeout, scheduler: Scheduler) = Stateless[GetOptimalItem]{ (_, command) =>
    Future.sequence(
      repositories.map(repository =>
        repository ? {replyTo: ActorRef[ItemsFound] => GetItems(command.kind, replyTo)}
      )
    ).map(commands => commands.flatMap(command => command.item))
      .map(items => items.reduce(strategy))
      .foreach(item => command.replyTo ! item)
  }
}