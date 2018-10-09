package pl.knirski.typeclasses.tutorial06

import java.time.Instant

object TypeclassForExternalType extends App {

  object Domain {

    case class Guitar(name: String, dateOfIntroduction: Instant, vibrato: Boolean, popularisedBy: Seq[Person])
    case class Person(firstName: String, lastName: String)

  }

  object ExternalJson {

    import pl.knirski.typeclasses.tutorial05.ImplicitTypeclass.JsonStuff._

    implicit val instantJsonable = new Jsonable[Instant] {
      override def toJson(a: Instant): JsonValue = JsonString(a.toString)
    }

    implicit def seqJsonable[A: Jsonable] = new Jsonable[Seq[A]] {
      override def toJson(a: Seq[A]): JsonValue = JsonArray(a.map(implicitly[Jsonable[A]].toJson))
    }

  }

  object DomainJson {

    import Domain._
    import ExternalJson._
    import pl.knirski.typeclasses.tutorial05.ImplicitTypeclass.JsonStuff._

    implicit val personJsonable = new Jsonable[Person] {
      override def toJson(p: Person): JsonValue = {
        JsonObject(Map(
          "firstName" -> JsonString(p.firstName),
          "lastName" -> JsonString(p.lastName)
        ))
      }
    }

    implicit val guitarJsonable = new Jsonable[Guitar] {
      override def toJson(g: Guitar): JsonValue = {
        JsonObject(Map(
          "name" -> JsonString(g.name),
          "dateOfIntroduction" -> implicitly[Jsonable[Instant]].toJson(g.dateOfIntroduction),
          "vibrato" -> JsonBoolean(g.vibrato),
          "popularisedBy" -> implicitly[Jsonable[Seq[Person]]].toJson(g.popularisedBy)
        ))
      }
    }

  }

  import Domain._

  val lesPaul = Guitar("Gibson Les Paul", Instant.MAX, false, Seq(Person("Jimmy", "Page")))
  val stratocaster = Guitar("Fender Stratocaster", Instant.MIN, true, Seq(Person("Jimi", "Hendrix"), Person("Eric", "Clapton")))

  import DomainJson._
  import pl.knirski.typeclasses.tutorial05.ImplicitTypeclass.JsonStuff._

  println(formatCompactJson(lesPaul))
  println(formatCompactJson(stratocaster))

}
