package pl.knirski.typeclasses.tutorial01

// First approach - classic object-oriented design
object OOP extends App {

  object JustJsonStuff {

    sealed trait JsonValue

    case class JsonBoolean(value: Boolean) extends JsonValue {
      override def toString: String = java.lang.Boolean.toString(value)
    }
    case class JsonNumber(value: Long) extends JsonValue {
      override def toString: String = java.lang.Long.toString(value)
    }
    case class JsonString(value: String) extends JsonValue {
      override def toString: String = '"' + value + '"'
    }
    case class JsonObject(fields: Map[String, JsonValue]) extends JsonValue {
      override def toString: String = {
        "{" + fields.map { case (key, value) => '"' + key + '"' + ":" + value }.mkString(",") + "}"
      }
    }
    case class JsonArray(elements: Seq[JsonValue]) extends JsonValue {
      override def toString: String = {
        "[" + elements.mkString(",") + "]"
      }
    }

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

  println(lesPaul.toJson)
  println(stratocaster.toJson)

}


