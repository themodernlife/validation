package play.api.data.mapping.delimited

import org.specs2.mutable._
import play.api.data.mapping.{Path, From, Success}

class RulesSpec extends Specification {
  "Rules" should {
    val example = Array("foo", "1.2", "100")

//    "parse data types" in {
//      From[Delimited] { __ =>
//        (
//          (__ \ 0).read[String] ~
//          (__ \ 1).read[Double] ~
//          (__ \ 2).read[Int]
//        ).tupled
//      }.validate(example) mustEqual(Success(("foo", 1.2, 100)))
//    }

    val valid = Array("John Doe", "12345", "9393.12")
    val valid2 = Array("", "12345", "9393.12")

    val invalid = Array("Jane Doe", "9999x", "kjdsf")

    "extract data" in {
      (Path \ 0).read[Delimited, String].validate(valid) mustEqual(Success("John Doe"))
      (Path \ 1).read[Delimited, Long].validate(valid) mustEqual(Success(12345))
      (Path \ 2).read[Delimited, Double].validate(valid) mustEqual(Success(9393.12))
    }

    "validate options" in {
      (Path \ 0).read[Delimited, Option[String]].validate(valid2) mustEqual(Success(None))
      (Path \ 1).read[Delimited, Option[Long]].validate(valid2) mustEqual(Success(Some(12345)))
      (Path \ 2).read[Delimited, Option[Double]].validate(valid2) mustEqual(Success(Some(9393.12)))
    }

    "validate data" in {

      case class Contact(name: String, int: Int, double: Double)

      val w = From[Delimited] { __ =>
        ((__ \ 0).read[String] and
          (__ \ 1).read[Int] and
          (__ \ 2).read[Double])(Contact)
      }

      w.validate(valid) mustEqual(Success(Contact("John Doe", 12345, 9393.12)))
    }

    /*
    "return optional values" in {
      val input = Array("string", "1.9", "20123", "")
    }

    "optionally ignore extra columns" in {
      val input = Array("string", "1.9", "20123", "ignore")
    }
    */
  }
}
