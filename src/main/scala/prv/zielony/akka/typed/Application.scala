package prv.zielony.akka.typed

import akka.typed._
import akka.typed.scaladsl.Actor._
import akka.typed.{ActorRef, ActorSystem}
import akka.typed.ScalaDSL.{And, Or, Partial, Static, Total}
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends App {

  import scala.concurrent.duration._

  implicit val timeout: Timeout = 3 seconds

  sealed trait PrimeCountCommand

  case class CheckNumber(number: Int) extends PrimeCountCommand

  case class ReturnAll(replyTo: ActorRef[Seq[Int]]) extends PrimeCountCommand

  // TODO: Experiment with pure functions and lifting!

  def checkPrimeBehavior(formerPrimes: Seq[Int])(command: PrimeCountCommand): Behavior[PrimeCountCommand] = command match {
    case CheckNumber(i) => if(formerPrimes.exists(x => i % x == 0)) {
      Total[PrimeCountCommand](checkPrimeBehavior(formerPrimes))
    } else {
      Total[PrimeCountCommand](checkPrimeBehavior(formerPrimes :+ i))
    }
    case ReturnAll(sender) => {
      sender ! formerPrimes
      Same[PrimeCountCommand]
    }
  }

  def handleCheckNumber(formerPrimes: Seq[Int]): PartialFunction[PrimeCountCommand, Behavior[PrimeCountCommand]] = {
    case CheckNumber(i) => if(formerPrimes.exists(x => i % x == 0)) {
      Partial(handleCheckNumber(formerPrimes))
    } else {
      Partial(handleCheckNumber(formerPrimes :+ i))
    }
  }



  def handleReturnAll(primes: Seq[Int]): PartialFunction[PrimeCountCommand, Behavior[PrimeCountCommand]] = {
    case ReturnAll(ref) => {
      ref ! primes
      Partial(handleReturnAll(primes))
    }
  }

  val composedBehavior = Or(Partial(handleCheckNumber(Seq(2))), Partial(handleReturnAll(Seq(2))))

  //

  val partialBehavior = Partial(handleCheckNumber(Seq(2)))

  val system = ActorSystem[PrimeCountCommand]("MainSystem", Total(checkPrimeBehavior(Seq(2))))

  implicit val scheduler = system.scheduler

  (3 to 20).foreach{i => system ! CheckNumber(i)}

  val futureResult = system ? {actorRef: ActorRef[Seq[Int]] => ReturnAll(actorRef)}

  futureResult.foreach{ints =>
    println(s"Primes: $ints")
    system.terminate
  }

}
