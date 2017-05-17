package prv.zielony.akka.typed.behaviors.tap

import akka.typed.{Behavior, Signal}
import akka.typed.scaladsl.Actor.Tap
import akka.typed.scaladsl.ActorContext

/**
  * Created by kamil on 2017-05-17.
  */
object LoggedActor {

  def apply[T](behavior: Behavior[T]) = Tap[T](
    {(ctx: ActorContext[T], signal: Signal) => {}},
    { (ctx: ActorContext[T], message: T) => println(s"Received message: $message")},
    behavior)
}
