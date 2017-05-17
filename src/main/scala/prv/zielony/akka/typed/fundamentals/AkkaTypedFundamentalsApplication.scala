package prv.zielony.akka.typed.fundamentals

import akka.typed.scaladsl.Actor
import akka.typed.{ActorRef, ActorSystem}
import akka.typed.scaladsl.Actor.{Empty, Stateless}
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
  val squareBehavior = Stateless[Int]{ (_, i) =>
    println(s"$i squared is: ${i * i}")
  }

  // Initialize a typed ActorSystem with empty guardian behavior
  val system: ActorSystem[Nothing] = ActorSystem("SquareSystem", Empty)

  // Wrap behavior in an actor and get a typed ActorRef to it.
  val typedReference: Future[ActorRef[Int]] = system.systemActorOf(squareBehavior, "SquareActor")

  typedReference.map(ref =>
    // Send messages to your actor via the typed ActorRef.
    (0 to 10).foreach(i => ref ! i)
  )
}
