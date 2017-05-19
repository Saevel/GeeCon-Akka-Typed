package prv.zielony.akka.typed.fundamentals

import akka.typed.scaladsl.Actor._
import akka.typed.{ActorRef, ActorSystem}
import akka.util.Timeout

import scala.concurrent.Future

/**
  * Check the easiest basics of Akka Typed Here
  */
object AkkaTypedFundamentalsApplication extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  implicit val timeout: Timeout = 3 millis

  // Create a simple, static behavior for the actor.
  val squareBehavior = immutable[Int]{ (_, i) =>
    println(s"$i squared is: ${i * i}")
    // Use the same behavior the next time
    same
  }

  // Initialize a typed ActorSystem with empty guardian behavior
  val system: ActorSystem[Int] = ActorSystem("SquareSystem", squareBehavior)

  (0 to 10).foreach(i => system ! i)
}