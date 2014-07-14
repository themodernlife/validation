package play.api.data.mapping.delimited

import org.specs2.mutable._
import play.api.data.mapping.{From, Success}

class RulesSpec extends Specification {
  "Rules" should {
    val example = Array("foo", "1.2", "100")

    "parse data types" in {
      From[Delimited] { __ =>
        (
          (__ \ 0).read[String] ~
          (__ \ 1).read[Double] ~
          (__ \ 2).read[Int]
        ).tupled
      }.validate(example) mustEqual(Success(("foo", 1.2, 100)))
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
