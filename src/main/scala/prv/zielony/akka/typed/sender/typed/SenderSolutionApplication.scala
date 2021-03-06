package prv.zielony.akka.typed.sender.typed

import akka.typed.scaladsl.Actor._
import akka.typed.{ActorRef, ActorSystem}
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout
import prv.zielony.akka.typed.sender.Item

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object SenderSolutionApplication extends App {

  implicit val timeout: Timeout = 10 seconds

  val actorSystem = ActorSystem("SenderSolution", empty)

  implicit val scheduler = actorSystem.scheduler

  val futureRepositories = Future.sequence(Seq(
    actorSystem.systemActorOf(
      Repository(Seq(
        Item("Computer", 7000.0, 0.99),
        Item("Pen", 20.0, 0.8)
      )), "Repository1"),
    actorSystem.systemActorOf(
      Repository(Seq(
        Item("Pen", 3.00, 0.5),
        Item("Computer", 2500.0, 0.23)
      )), "Repository2")
  ))

  val futureLowestPriceBuyer = futureRepositories.flatMap(references =>
    actorSystem.systemActorOf(Buyer(chooseLowestPrice)(references), "LowestPriceBuyer")
  )

  val futureHighestQualityBuyer = futureRepositories.flatMap(references =>
    actorSystem.systemActorOf(Buyer(chooseHighestQuality)(references), "HighestQualityBuyer")
  )

  futureLowestPriceBuyer.foreach(reference =>
    (0 to 10).foreach{_ => reference ! GetOptimalItem("Pen", actorSystem.deadLetters)}
  )

  val promise = futureHighestQualityBuyer.flatMap(buyer => buyer ? {ref: ActorRef[Item] => GetOptimalItem("Computer", ref)})

  futureHighestQualityBuyer.foreach(reference =>
    (0 to 10).foreach{_ => reference ! GetOptimalItem("Computer", actorSystem.deadLetters)}
  )

  val result = Await.result(promise, 10 seconds)

  println(s"Highest quality computer is: $result")

  actorSystem.terminate

  def chooseHighestQuality(first: Item, second: Item): Item = if(first.quality > second.quality) first else second

  def chooseLowestPrice(first: Item, second: Item): Item = if(first.price < second.price) first else second
}