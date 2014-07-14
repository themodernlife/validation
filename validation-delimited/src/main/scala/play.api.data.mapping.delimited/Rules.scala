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

  implicit def ooo[O](p: Path)(implicit pick: Path => RuleLike[Delimited, String], coerce: RuleLike[String, O]): Rule[Delimited, Option[O]] =
    myOpt(coerce, equalTo(""))(pick)(p)

  protected def myOpt[O](r: => RuleLike[String, O], noneValues: RuleLike[String, String]*)(implicit pick: Path => RuleLike[Delimited, String]) = (path: Path) =>
    Rule[Delimited, Option[O]] { delimited =>
      val isNone = not(noneValues.foldLeft(Rule.zero[String])(_ compose not(_))).fmap(_ => None)
      val v = (pick(path).validate(delimited).map(Some.apply) orElse Success(None))
      v.viaEither {
        _.right.flatMap {
          case None => Right(None)
          case Some(i) => isNone.orElse(Rule.toRule(r).fmap[Option[O]](Some.apply)).validate(i).asEither
        }
      }
    }

  /*
  implicit def ooo[O](p: Path)(implicit pick: Path => RuleLike[Delimited, String], coerce: RuleLike[String, O]): Rule[Delimited, Option[O]] =
    Rule[Delimited, O] { delimited =>
      val sval = (pick(p).validate(delimited).map {
        case s @ "" => Success(None)
        case s      => Success(Some(s))
      }
    }


  protected def opt[J, O](r: => RuleLike[J, O], noneValues: RuleLike[I, I]*)(implicit pick: Path => RuleLike[I, I], coerce: RuleLike[I, J]) = (path: Path) =>
    Rule[I, Option[O]] {
      (d: I) =>
        val isNone = not(noneValues.foldLeft(Rule.zero[I])(_ compose not(_))).fmap(_ => None)
        val v = (pick(path).validate(d).map(Some.apply) orElse Success(None))
        v.viaEither {
          _.right.flatMap {
            case None => Right(None)
            case Some(i) => isNone.orElse(Rule.toRule(coerce).compose(r).fmap[Option[O]](Some.apply)).validate(i).asEither
          }
        }
    }
    */

  //implicit def ooo[O](p: Path)(implicit pick: Path => RuleLike[JsValue, JsValue], coerce: RuleLike[JsValue, O]): Rule[JsValue, Option[O]] =
  //  optionR(Rule.zero[O])(pick, coerce)(p)

  /*
  def optionR[J, O](r: => RuleLike[J, O], noneValues: RuleLike[JsValue, JsValue]*)(implicit pick: Path => RuleLike[JsValue, JsValue], coerce: RuleLike[JsValue, J]): Path => Rule[JsValue, Option[O]] =
    super.opt[J, O](r, (jsNullR.fmap(n => n: JsValue) +: noneValues): _*)
  */

  //(pick(p).map(.validate(d).map(Some.apply) orElse Success(None))

  /*
  private val isEmpty = validateWith[PM]("validation.empty") { pm =>
    pm.filter { case (_, vs) => !vs.isEmpty }.isEmpty
  }
  implicit def optionR[O](implicit pick: Path => RuleLike[PM, PM], coerce: RuleLike[PM, O]): Path => Rule[PM, Option[O]] =
    opt(coerce, isEmpty)

  def optionR[J, O](r: => RuleLike[J, O], noneValues: RuleLike[PM, PM]*)(implicit pick: Path => RuleLike[PM, PM], coerce: RuleLike[PM, J]): Path => Rule[UrlFormEncoded, Option[O]] =
    path => {
      val nones = isEmpty +: noneValues
      val o = opt[J, O](r, nones: _*)(pick, coerce)(path)
      Rule.zero[UrlFormEncoded].fmap(toPM).compose(o)
    }
    */

}