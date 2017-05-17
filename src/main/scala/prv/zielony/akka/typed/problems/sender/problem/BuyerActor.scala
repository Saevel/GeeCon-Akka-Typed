package prv.zielony.akka.typed.problems.sender.problem

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import prv.zielony.akka.typed.problems.sender.Item

import scala.concurrent.{ExecutionContext, Future}

// POMYSŁ: MAMY ITEMY, KTÓRE MAJĄ CENY, I JAKOŚĆ PLUS AKTORZY Z REPOZYTORIAMI, KTÓRZY ZWRACA
// CAJĄ Z NICH DANE. NA TYCH STOJĄ BUY'ERZY, KTÓRZY KUPUJĄ JE ALBO WG CENY, ALBO WG JAKOŚCI ETC.

// JEŚLI WYWOŁAMY KILKU RÓNYCH BUYERÓW JEDEN PO DRUGI, W TYM NP ASYNCHRONICZNIE, TO RACZEJ
// DOSTANIEMY BŁĘDNE WYNIKI PRZEZ RACE CONDITION NA SENDERZE

/**
  * Created by kamil on 2017-05-16.
  */
class BuyerActor(strategy: (Item, Item) => Item, repositories: Seq[ActorRef])(implicit timeout: Timeout, context: ExecutionContext) extends Actor {

  override def receive: Receive = {

    case GetItems(kind) => Future.sequence(
      // Ask each repository for items
      repositories.map(reference => (reference ? GetItems(kind))
        .mapTo[Seq[Item]]
      )
    ).map(items => items.flatten)
      // Choose the best item using strategy.
      .map(items => items.reduce(strategy))
      // When ready, send back the chosen item.
      .foreach(chosenItem => sender() ! chosenItem)
  }
}