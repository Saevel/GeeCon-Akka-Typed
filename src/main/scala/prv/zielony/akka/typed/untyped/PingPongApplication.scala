package prv.zielony.akka.typed.untyped

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by kamil on 2017-05-16.
  */
object PingPongApplication extends App {
  // Create an ActorSystem
  val actorSystem = ActorSystem("PingPong")

  // Register actor instances in the ActorSystem and get references to them.
  lazy val pingActor: ActorRef = actorSystem.actorOf(Props(new PingActor(pongActor)))

  lazy val pongActor: ActorRef = actorSystem.actorOf(Props(new PongActor(pingActor)))

  // Start the game
  pingActor ! "ping"

  // Sleep 3 seconds and observe the game
  Thread.sleep(3 * 1000)

  // Stop the game.
  pingActor ! "stop"
  actorSystem.terminate
}