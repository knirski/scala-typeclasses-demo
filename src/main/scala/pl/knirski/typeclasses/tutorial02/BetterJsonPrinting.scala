package pl.knirski.typeclasses.tutorial02

object BetterJsonPrinting extends App {

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
      case JsonObject(fields) => "{" + fields.map { case (key, value) => '"' + key + '"' + ":" + formatCompactJson(value) }.mkString(",") + "}"
      case JsonArray(elements) => "[" + elements.map(formatCompactJson).mkString(",") + "]"
    }

    def formatIndentedJson(js: JsonValue): String = ???

    trait Jsonable {
      def toJson: JsonValue
    }

  }

  import JustJsonStuff._

  case class Guitar(name: String, yearOfIntroduction: Int, vibrato: Boolean, popularisedBy: Seq[Person]) extends Jsonable {
    override def toJson: JsonValue = {
      JsonObject(Map(
        "name" -> JsonString(name),
        "yearOfIntroduction" -> JsonNumber(yearOfIntroduction),
        "vibrato" -> JsonBoolean(vibrato),
        "popularisedBy" -> JsonArray(popularisedBy.map(_.toJson))
      )
      )
    }
  }

  case class Person(firstName: String, lastName: String) extends Jsonable {
    override def toJson: JsonValue = {
      JsonObject(Map(
        "firstName" -> JsonString(firstName),
        "lastName" -> JsonString(lastName)
      ))
    }
  }

  val lesPaul = Guitar("Gibson Les Paul", 1952, false, Seq(Person("Jimmy", "Page")))
  val stratocaster = Guitar("Fender Stratocaster", 1954, true, Seq(Person("Jimi", "Hendrix"), Person("Eric", "Clapton")))

  println(formatCompactJson(lesPaul.toJson))
  println(formatCompactJson(stratocaster.toJson))

}
