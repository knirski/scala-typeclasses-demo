package pl.knirski.typeclasses.tutorial07

import java.time.Instant

import io.circe.generic.auto._
import io.circe.syntax._

object GenericDerivation extends App {

  import pl.knirski.typeclasses.tutorial03.DomainJsonSeparation.Domain._

  val lesPaul = Guitar("Gibson Les Paul", 1952, false, Seq(Person("Jimmy", "Page")))
  val stratocaster = Guitar("Fender Stratocaster", 1954, true, Seq(Person("Jimi", "Hendrix"), Person("Eric", "Clapton")))

  println(lesPaul.asJson.noSpaces)
  println(stratocaster.asJson.noSpaces)

  case class Yolo(when: Instant)

  val json = Yolo(Instant.now()).asJson
  println(json)

}
