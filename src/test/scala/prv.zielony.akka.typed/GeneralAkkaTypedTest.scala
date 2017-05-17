package prv.zielony.akka.typed

import akka.typed.{ActorRef, ActorSystem}
import akka.typed.ScalaDSL.Total
import akka.typed.scaladsl.AskPattern._
import akka.util.Timeout
import org.scalacheck.Gen
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class GeneralAkkaTypedTest extends WordSpec with Matchers with PropertyChecks with ScalaFutures {

  val integerIntervals = Gen.choose(4, 20).map(i => 3 to i)

  "Prime numbers" should {

    "not be divisible by any number other than itself and 1" in forAll(integerIntervals){ interval =>

      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.concurrent.duration._

      implicit val timeout: Timeout = 3 seconds

      val system = ActorSystem[PrimeCountCommand]("MainSystem", Total(checkPrimeBehavior(Seq(2))))

      val primesActor = system.systemActorOf(Total(checkPrimeBehavior(Seq(2))), "PrimesActor")

      implicit val scheduler = system.scheduler

      val results = primesActor
        .flatMap{ref =>
          interval.foreach(i => ref ! CheckNumber(i))
          ref ? {reference: ActorRef[Seq[Int]] => ReturnAll(reference)}
        }.futureValue

      // interval.foreach(i => primesActor ! CheckNumber(i))

      // val results = (primesActor ? {reference: ActorRef[Seq[Int]] => ReturnAll(reference)}).futureValue

      results.foreach{ i =>
        interval.forall(x => (i % x == 0) && (i != x)) should be(false)
      }
    }
  }
}
