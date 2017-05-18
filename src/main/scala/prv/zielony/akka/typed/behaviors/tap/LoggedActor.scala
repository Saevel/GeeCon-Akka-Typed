package prv.zielony.akka.typed.behaviors.tap

import akka.typed.{Behavior, Signal}
import akka.typed.scaladsl.Actor._
import akka.typed.scaladsl.{Actor, ActorContext}

object LoggedActor {

  def apply[T](behavior: Behavior[T]) = Actor.tap[T](
    {(ctx: ActorContext[T], message: T) => println(s"Received message: $message")},
    {(ctx: ActorContext[T], signal: Signal) => {}},
    behavior
  )
}
