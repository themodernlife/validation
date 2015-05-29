package play.api.data.mapping.config

import com.typesafe.config._
import play.api.data.mapping.GenericRules

object Rules extends GenericRules {
  import scala.language.implicitConversions
  import play.api.libs.functional._
  import play.api.libs.functional.syntax._

  import play.api.data.mapping._

  private def configAs[T](f: PartialFunction[ConfigValue, Validation[ValidationError, T]])(msg: String, args: Any*) =
    Rule.fromMapping[ConfigValue, T](
      f.orElse {
        case j => Failure(Seq(ValidationError(msg, args: _*)))
      })
//
//  implicit def stringR = jsonAs[String] {
//    case JString(v) => Success(v)
//  }("error.invalid", "String")
//
  implicit def booleanR = configAs[Boolean] {
    case cv if cv.unwrapped().isInstanceOf[Boolean] => Success(cv.unwrapped().asInstanceOf[Boolean])
  }("error.invalid", "Boolean")
//
//  // Note: Mappings of JsNumber to Number are validating that the JsNumber is indeed valid
//  // in the target type. i.e: JsNumber(4.5) is not considered parseable as an Int.
//  implicit def intR = configAs[Int] {
//    case cv if cv.unwrapped().isInstanceOf[Number] => Success(cv.unwrapped().asInstanceOf[Number].intValue())
//  }("error.number", "Int")
//
//  implicit def shortR = jsonAs[Short] {
//    case JInt(v) if v.isValidShort => Success(v.toShort)
//  }("error.number", "Short")
//
//  implicit def longR = jsonAs[Long] {
//    case JInt(v) if v.isValidLong => Success(v.toLong)
//  }("error.number", "Long")
//
//  implicit def jsNumberR[N <: JsonAST.JNumber] = jsonAs[N] {
//    case v: N => Success(v)
//  }("error.number", "Number")
//
//  implicit def jsBooleanR = jsonAs[JBool] {
//    case v @ JBool(_) => Success(v)
//  }("error.invalid", "Boolean")
//
//  implicit def jsStringR = jsonAs[JString] {
//    case v @ JString(_) => Success(v)
//  }("error.invalid", "String")
//
//  implicit def jsObjectR = jsonAs[JObject] {
//    case v @ JObject(_) => Success(v)
//  }("error.invalid", "Object")
//
//  implicit def jsArrayR = jsonAs[JArray] {
//    case v @ JArray(_) => Success(v)
//  }("error.invalid", "Array")
//
//  // BigDecimal.isValidFloat is buggy, see [SI-6699]
//  import java.{ lang => jl }
//  private def isValidFloat(bd: BigDecimal) = {
//    val d = bd.toFloat
//    !d.isInfinity && bd.bigDecimal.compareTo(new java.math.BigDecimal(jl.Float.toString(d), bd.mc)) == 0
//  }
//  implicit def floatR = jsonAs[Float] {
//    case JDecimal(v) if isValidFloat(v) => Success(v.toFloat)
//    case JDouble(v) if isValidFloat(v) => Success(v.toFloat)
//    case JInt(v) => Success(v.toFloat)
//  }("error.number", "Float")
//
//  // BigDecimal.isValidDouble is buggy, see [SI-6699]
//  private def isValidDouble(bd: BigDecimal) = {
//    val d = bd.toDouble
//    !d.isInfinity && bd.bigDecimal.compareTo(new java.math.BigDecimal(jl.Double.toString(d), bd.mc)) == 0
//  }
//  implicit def doubleR = jsonAs[Double] {
//    case JDecimal(v) if isValidDouble(v) => Success(v.toDouble)
//    case JDouble(v) => Success(v)
//    case JInt(v) => Success(v.toDouble)
//  }("error.number", "Double")
//
//  implicit def bigDecimal = jsonAs[BigDecimal] {
//    case JDecimal(v) => Success(v)
//    case JDouble(v) => Success(v)
//    case JInt(v) => Success(BigDecimal(v))
//  }("error.number", "BigDecimal")
//
//  import java.{ math => jm }
//  implicit def javaBigDecimal = jsonAs[jm.BigDecimal] {
//    case JDecimal(v) => Success(v.bigDecimal)
//    case JDouble(v) => Success(BigDecimal(v).bigDecimal)
//    case JInt(v) => Success(BigDecimal(v).bigDecimal)
//  }("error.number", "BigDecimal")
//
//  implicit val jsNullR = jsonAs[JNull.type] {
//    case JNull => Success(JNull)
//  }("error.invalid", "null")
//
//  implicit def ooo[O](p: Path)(implicit pick: Path => RuleLike[JValue, JValue], coerce: RuleLike[JValue, O]): Rule[JValue, Option[O]] =
//    optionR(Rule.zero[O])(pick, coerce)(p)
//
//  def optionR[J, O](r: => RuleLike[J, O], noneValues: RuleLike[JValue, JValue]*)(implicit pick: Path => RuleLike[JValue, JValue], coerce: RuleLike[JValue, J]): Path => Rule[JValue, Option[O]] =
//    super.opt[J, O](r, (jsNullR.fmap(n => n: JValue) +: noneValues): _*)
//
//  implicit def mapR[O](implicit r: RuleLike[JValue, O]): Rule[JValue, Map[String, O]] =
//    super.mapR[JValue, O](r, jsObjectR.fmap { case JObject(fs) => fs })
//
//  implicit def JsValue[O](implicit r: RuleLike[JObject, O]): Rule[JValue, O] =
//    jsObjectR.compose(r)

  implicit def pickInJson[II <: ConfigValue, O](p: Path)(implicit r: RuleLike[ConfigValue, O]): Rule[II, O] = {

    def search(path: Path, config: ConfigValue): Option[ConfigValue] = path.path match {
      case KeyPathNode(k) :: t =>
        config match {
          case co: ConfigObject if co.containsKey(k) => search(Path(t), co.get(k))
          case _ => None
        }
      case IdxPathNode(i) :: t =>
        config match {
          case cl: ConfigList if i < cl.size() => search(Path(t), cl.get(i))
          case _ => None
        }
      case Nil => Some(config)
    }

    Rule[II, ConfigValue] { config =>
      search(p, config) match {
        case None => Failure(Seq(Path -> Seq(ValidationError("error.required"))))
        case Some(js) => Success(js)
      }
    }.compose(r)
  }

  // // XXX: a bit of boilerplate
//  private def pickInS[T](implicit r: RuleLike[Seq[JValue], T]): Rule[JValue, T] =
//    jsArrayR.fmap { case JArray(fs) => Seq(fs:_*) }.compose(r)
//  implicit def pickSeq[O](implicit r: RuleLike[JValue, O]) = pickInS(seqR[JValue, O])
//  implicit def pickSet[O](implicit r: RuleLike[JValue, O]) = pickInS(setR[JValue, O])
//  implicit def pickList[O](implicit r: RuleLike[JValue, O]) = pickInS(listR[JValue, O])
//  implicit def pickArray[O: scala.reflect.ClassTag](implicit r: RuleLike[JValue, O]) = pickInS(arrayR[JValue, O])
//  implicit def pickTraversable[O](implicit r: RuleLike[JValue, O]) = pickInS(traversableR[JValue, O])

}