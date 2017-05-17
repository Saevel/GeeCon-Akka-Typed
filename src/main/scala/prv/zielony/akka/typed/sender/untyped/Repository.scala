package prv.zielony.akka.typed.sender.untyped

import akka.actor.Actor
import prv.zielony.akka.typed.sender.Item

import scala.util.Random

class Repository(items: Seq[Item]) extends Actor {

  private def randomSmallInt = Math.abs(Random.nextInt(1000))

  override def receive: Receive = {
    case GetItems(kind) => {
      Thread.sleep(randomSmallInt)
      sender ! items.filter(item => item.kind == kind)
    }
  }
}
