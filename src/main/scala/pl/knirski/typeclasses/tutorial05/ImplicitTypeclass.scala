package pl.knirski.typeclasses.tutorial05

object ImplicitTypeclass extends App {

  object JsonStuff {


    sealed trait JsonValue
    case class JsonBoolean(value: Boolean) extends JsonValue
    case class JsonNumber(value: Long) extends JsonValue
    case class JsonString(value: String) extends JsonValue
    case class JsonObject(fields: Map[String, JsonValue]) extends JsonValue
    case class JsonArray(elements: Seq[JsonValue]) extends JsonValue

    trait Jsonable[A] {
      def toJson(a: A): JsonValue
    }

    def formatCompactJson(js: JsonValue): String = js match {
      case JsonBoolean(value) => java.lang.Boolean.toString(value)
      case JsonNumber(value) => java.lang.Long.toString(value)
      case JsonString(value) => '"' + value + '"'
      case JsonObject(fields) => "{" + fields.map { case (key, value) => '"' + key + '"' + ": " + formatCompactJson(value) }.mkString(", ") + "}"
      case JsonArray(elements) => "[" + elements.map(formatCompactJson).mkString(", ") + "]"
    }

    // those two are equivalent
//    def formatCompactJson[A](a: A)(implicit jsonable: Jsonable[A]): String = formatCompactJson(jsonable.toJson(a))
    def formatCompactJson[A: Jsonable](a: A): String = formatCompactJson(implicitly[Jsonable[A]].toJson(a))

  }

  object DomainJson {

    import pl.knirski.typeclasses.tutorial03.DomainJsonSeparation.Domain._
    import ImplicitTypeclass.JsonStuff._

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
          "yearOfIntroduction" -> JsonNumber(g.yearOfIntroduction),
          "vibrato" -> JsonBoolean(g.vibrato),
          "popularisedBy" -> JsonArray(g.popularisedBy.map(implicitly[Jsonable[Person]].toJson))
        ))
      }
    }
  }

  import pl.knirski.typeclasses.tutorial03.DomainJsonSeparation.Domain._

  val lesPaul = Guitar("Gibson Les Paul", 1952, false, Seq(Person("Jimmy", "Page")))
  val stratocaster = Guitar("Fender Stratocaster", 1954, true, Seq(Person("Jimi", "Hendrix"), Person("Eric", "Clapton")))

  import JsonStuff._
  import DomainJson._

  println(formatCompactJson(lesPaul))
  println(formatCompactJson(stratocaster))

}
