package pl.knirski.typeclasses.tutorial03

object DomainJsonSeparation extends App {

  object JustJsonStuff {

    sealed trait JsonValue
    case class JsonBoolean(value: Boolean) extends JsonValue
    case class JsonNumber(value: Long) extends JsonValue
    case class JsonString(value: String) extends JsonValue
    case class JsonObject(fields: Map[String, JsonValue]) extends JsonValue
    case class JsonArray(elements: Seq[JsonValue]) extends JsonValue

    def formatCompactJson(js: JsonValue): String = js match {
      case JsonBoolean(value) => java.lang.Boolean.toString(value)
      case JsonNumber(value) => java.lang.Long.toString(value)
      case JsonString(value) => '"' + value + '"'
      case JsonObject(fields) => "{" + fields.map { case (key, value) => '"' + key + '"' + ": " + formatCompactJson(value) }.mkString(", ") + "}"
      case JsonArray(elements) => "[" + elements.map(formatCompactJson).mkString(", ") + "]"
    }

  }

  object Domain {

    case class Guitar(name: String, yearOfIntroduction: Int, vibrato: Boolean, popularisedBy: Seq[Person])
    case class Person(firstName: String, lastName: String)

  }

  object DomainJson {

    import Domain._
    import JustJsonStuff._

    def toJson(g: Guitar): JsonValue = {
      JsonObject(Map(
        "name" -> JsonString(g.name),
        "yearOfIntroduction" -> JsonNumber(g.yearOfIntroduction),
        "vibrato" -> JsonBoolean(g.vibrato),
        "popularisedBy" -> JsonArray(g.popularisedBy.map(toJson))
      )
      )

    }
    def toJson(p: Person): JsonValue = {
      JsonObject(Map(
        "firstName" -> JsonString(p.firstName),
        "lastName" -> JsonString(p.lastName)
      ))
    }

  }

  import Domain._

  val lesPaul = Guitar("Gibson Les Paul", 1952, false, Seq(Person("Jimmy", "Page")))
  val stratocaster = Guitar("Fender Stratocaster", 1954, true, Seq(Person("Jimi", "Hendrix"), Person("Eric", "Clapton")))

  import JustJsonStuff._
  import DomainJson._

  println(formatCompactJson(toJson(lesPaul)))
  println(formatCompactJson(toJson(stratocaster)))

}
