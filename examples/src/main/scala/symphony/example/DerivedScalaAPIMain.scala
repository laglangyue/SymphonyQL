package symphony.example

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.*
import symphony.*
import symphony.parser.*
import symphony.schema.*
import symphony.example.schema.Users.*

import scala.concurrent.*
import scala.concurrent.duration.Duration

object DerivedScalaAPIMain {

  val graphql: SymphonyQL = SymphonyQL
    .newSymphonyQL()
    .query(
      Queries(args =>
        Source.single(
          Character("hello-" + args.origin.map(_.toString).getOrElse(""), args.origin.getOrElse(Origin.BELT))
        )
      )
    )
    .build()

  println(graphql.render)

  val characters =
    """{
      |  characters(origin: "MARS") {
      |    name
      |    origin
      |  }
      |}""".stripMargin

  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem                   = ActorSystem("symphonyActorSystem")
    val getRes: Future[SymphonyQLResponse[SymphonyQLError]] = graphql.runWith(SymphonyQLRequest(characters))
    println(Await.result(getRes, Duration.Inf).toOutputValue)
    actorSystem.terminate()
  }

}
