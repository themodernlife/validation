package play.api.data.mapping.delimited

import play.api.data.mapping._

object Rules extends DefaultRules[Delimited] with ParsingRules {
  import scala.language.implicitConversions
  import scala.language.higherKinds

  implicit def pick[O](p: Path)(implicit r: RuleLike[String, O]): Rule[Delimited, O] =
    Rule[Delimited, String] { delimited =>
      p.path match {
        case IdxPathNode(i) :: t if i < delimited.length => Success(delimited(i))
        case _ => Failure(Seq(Path -> Seq(ValidationError("error.required"))))
      }
    }.compose(r)
}