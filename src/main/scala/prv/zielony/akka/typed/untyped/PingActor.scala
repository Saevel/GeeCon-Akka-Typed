package prv.zielony.akka.typed.untyped

import akka.actor.{Actor, ActorRef}

/**
  * Extremely simple actor
  */
class PingActor(pongActor: ActorRef) extends Actor {

  override def receive: Receive = {
    case "ping" => {
      println("Ping!")
      pongActor ! "pong"
    }

    case "stop" => {
      pongActor ! "stop"
      context.stop(self)
    }
  }
}
