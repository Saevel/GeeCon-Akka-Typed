package prv.zielony.akka.typed.problems.sender.problem

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import prv.zielony.akka.typed.problems.sender.Item

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object SenderProblemApplication extends App {

  implicit val timeout: Timeout = 10 seconds

  val actorSystem = ActorSystem("SenderProblem")

  implicit val sheduler = actorSystem.scheduler

  // Create repositories with different items.
  val repositories = Seq(
    actorSystem.actorOf(Props(new RepositoryActor(Seq(
      Item("Computer", 7000.0, 0.99),
      Item("Pen", 20.0, 0.8)
    )))),
    actorSystem.actorOf(Props(new RepositoryActor(Seq(
      Item("Pen", 3.00, 0.5),
      Item("Computer", 2500.0, 0.23)
    ))))
  )

  // Create two Buyer actors for lowestPrice strategy and highest quallity strategy.
  val lowestPriceBuyer = actorSystem.actorOf(Props(new BuyerActor(chooseLowestPrice, repositories)))
  val highestQualityBuyer = actorSystem.actorOf(Props(new BuyerActor(chooseHighestQuality, repositories)))

  // Introduce some clutter and ask the actor for the result.
  (0 to 10).foreach(_ => lowestPriceBuyer ! GetItems("Laptop"))
  val promise = (highestQualityBuyer ? GetItems("Laptop")).mapTo[Item]
  (0 to 10).foreach(_ => highestQualityBuyer ! GetItems("Pen"))

  val result = Await.result(promise, 10 seconds)

  // Result will randomly be incorrect because of the race condition on sender.
  println(s"Supposedly highest quality laptop: $result")

  actorSystem.terminate

  def chooseHighestQuality(first: Item, second: Item): Item = if(first.quality > second.quality) first else second

  def chooseLowestPrice(first: Item, second: Item): Item = if(first.price < second.price) first else second
}
