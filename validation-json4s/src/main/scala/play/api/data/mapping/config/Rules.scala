package play.api.data.mapping.config

import com.typesafe.config._
import play.api.data.mapping.GenericRules

import scala.util.Try
import java.lang.{Number => JNumber}
import java.math.{BigDecimal => JBigDecimal, BigInteger => JBigInteger}

object Rules extends GenericRules {
  import scala.language.implicitConversions
  import play.api.libs.functional._
  import play.api.libs.functional.syntax._

  import play.api.data.mapping._

//  private def configAs[T](f: PartialFunction[Config, Validation[ValidationError, T]])(msg: String, args: Any*) =
//    Rule.fromMapping[Config, T](
//      f.orElse {
//        case j => Failure(Seq(ValidationError(msg, args: _*)))
//      })
//
//  implicit def stringR = jsonAs[String] {
//    case JString(v) => Success(v)
//  }("error.invalid", "String")
//
//  implicit def booleanR = configAs[Boolean] {
//    case cv if cv.unwrapped().isInstanceOf[Boolean] => Success(cv.unwrapped().asInstanceOf[Boolean])
//  }("error.invalid", "Boolean")
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

//  implicit def pickInJson[CV , O](p: Path)(implicit r: RuleLike[CV, O]): Rule[Config, O] = {
//
//    def search(path: Path, config: Config): Option[Config] = path.path match {
//      case KeyPathNode(k) :: t =>
//        config match {
//          case config if config.hasPath(k) => search(Path(t), config.getConfig(k))
//          case _ => None
//        }
////      case IdxPathNode(i) :: t =>
////        config match {
////          case cl: ConfigList if i < cl.size() => search(Path(t), cl.get(i))
////          case _ => None
////        }
//      case Nil => Some(config)
//    }
//
//    Rule[Config, Config] { config =>
//      search(p, config) match {
//        case Some(config) => Success(config)
//        case None         => Failure(Seq(Path -> Seq(ValidationError("error.required"))))
//      }
//    }.compose(r)
//  }

//

//
//
//  implicit def parseString[O](implicit r: RuleLike[String, O]): Rule[PM, O] = {
//    val find = Rule[Option[String], String] {
//      _.map(Success(_)).getOrElse(Failure(Seq(Path -> Seq(ValidationError("error.required")))))
//    }
//    Rule.zero[PM]
//      .fmap(_.get(Path))
//      .compose(find)
//      .compose(r)
//  }

//  implicit def booleanR = Rule.fromMapping[String, Boolean] {
//    pattern("""(?iu)true|false""".r).validate(_: String)
//      .map(java.lang.Boolean.parseBoolean)
//      .fail.map(_ => Seq(ValidationError("error.invalid", "Boolean")))
//  }

  private implicit def path2String(p: Path): String = p.path.mkString(".")

  implicit def pickInConfig[O](p: Path)(implicit r: RuleExtractor[O]): Rule[Config, O] =
    Rule[Config, Config] {
      case config if config.hasPath(p) => Success(config)
      case _ => Failure(Seq(Path -> Seq(ValidationError("error.required"))))
    }.compose(r(p))

  private def rule[T](p: Path)(message: String, args: Any*)(f: (Config, String) => T) = Rule[Config, T] { c =>
    try {
      Success(f(c, p))
    } catch {
      case e: ConfigException => Failure(Seq(Path -> Seq(ValidationError(message, args: _*))))
    }
  }

  type RuleExtractor[O] = Path => RuleLike[Config, O]

  implicit val longR: RuleExtractor[Long]       = (p: Path) => rule(p)("error.number", "Long")((c, p) => c.getLong(p))
  implicit val intR: RuleExtractor[Int]         = (p: Path) => rule(p)("error.number", "Int")((c, p) => c.getInt(p))

  implicit val doubleR: RuleExtractor[Double]         = (p: Path) => rule(p)("error.number", "Double")((c, p) => c.getDouble(p))

  implicit val booleanR: RuleExtractor[Boolean] = (p: Path) => rule(p)("error.invalid", "Boolean")((c, p) => c.getBoolean(p))
  implicit val stringR: RuleExtractor[String]   = (p: Path) => rule(p)("error.invalid", "String")((c, p) => c.getString(p))

  implicit val jnumberR: RuleExtractor[JNumber] = (p: Path) => rule(p)("error.number", "JNumber")((c, p) => c.getNumber(p))

  // These types are not represented in the core
  implicit val shortR: RuleExtractor[Short] = (p: Path) => rule(p)("error.number", "Short")((c, p) => c.getNumber(p).shortValue())
  implicit val foatR: RuleExtractor[Float]         = (p: Path) => rule(p)("error.number", "Float")((c, p) => c.getNumber(p).floatValue())


  //
//  def safely[T](p: String): Rule[Config, Try[T]] = {
//
//    try {
//
//    } catch {
//      case e: com.typesafe.config.ConfigException => Seq(ValidationError("error.invalid", "Boolean"))
//    }
//
//    Rule.zero[T]
//  }
//
//  def booleanPick(p: String): Rule[Config, Boolean] = {
//    val find = Rule[Config, Boolean] {
//      case c if c.hasPath(p) => Success(c.getBoolean(p))
//      case _ => Failure(Seq(p -> Seq(ValidationError("error.required"))))
//    }
//
//    Rule.zero[Config].compose(find)
//  }

  // // XXX: a bit of boilerplate
//  private def pickInS[T](implicit r: RuleLike[Seq[JValue], T]): Rule[JValue, T] =
//    jsArrayR.fmap { case JArray(fs) => Seq(fs:_*) }.compose(r)
//  implicit def pickSeq[O](implicit r: RuleLike[JValue, O]) = pickInS(seqR[JValue, O])
//  implicit def pickSet[O](implicit r: RuleLike[JValue, O]) = pickInS(setR[JValue, O])
//  implicit def pickList[O](implicit r: RuleLike[JValue, O]) = pickInS(listR[JValue, O])
//  implicit def pickArray[O: scala.reflect.ClassTag](implicit r: RuleLike[JValue, O]) = pickInS(arrayR[JValue, O])
//  implicit def pickTraversable[O](implicit r: RuleLike[JValue, O]) = pickInS(traversableR[JValue, O])

}