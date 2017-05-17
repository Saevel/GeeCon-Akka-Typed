package prv.zielony.akka.typed.untyped

import akka.actor.{Actor, ActorRef}


class PongActor(pingActor: ActorRef) extends Actor {

  override def receive: Receive = {
    case "pong" => {
      println("Pong!")
      pingActor ! "ping"
    }

    case "stop" => {
      pingActor ! "stop"
      context.stop(self)
    }
  }
}
