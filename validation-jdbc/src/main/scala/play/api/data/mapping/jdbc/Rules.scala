/**
 * Copyright (C) 2014 MediaMath <http://www.mediamath.com>
 *
 * @author ihummel
 */
package play.api.data.mapping.jdbc

import java.sql._

import scala.util.Try

import play.api.data.mapping._
import play.api.data.mapping.IdxPathNode
import play.api.data.mapping.KeyPathNode

object Rules extends DefaultRules[ResultSet] {
  implicit def stringFromIndex(rs: ResultSet, i: Int): Try[String] = Try(rs.getString(i))
  implicit def stringFromColumn(rs: ResultSet, k: String): Try[String] = Try(rs.getString(k))

  implicit def bigDecimalFromIndex(rs: ResultSet, i: Int): Try[BigDecimal] = Try(rs.getBigDecimal(i))
  implicit def bigDecimalFromColumn(rs: ResultSet, k: String): Try[BigDecimal] = Try(rs.getBigDecimal(k))

  implicit def booleanFromIndex(rs: ResultSet, i: Int): Try[Boolean] = Try(rs.getBoolean(i))
  implicit def booleanFromColumn(rs: ResultSet, k: String): Try[Boolean] = Try(rs.getBoolean(k))

  implicit def longFromIndex(rs: ResultSet, i: Int): Try[Long] = Try(rs.getLong(i))
  implicit def longFromColumn(rs: ResultSet, k: String): Try[Long] = Try(rs.getLong(k))

  implicit def shortFromIndex(rs: ResultSet, i: Int): Try[Short] = Try(rs.getShort(i))
  implicit def shortFromColumn(rs: ResultSet, k: String): Try[Short] = Try(rs.getShort(k))

  implicit def doubleFromIndex(rs: ResultSet, i: Int): Try[Double] = Try(rs.getDouble(i))
  implicit def doubleFromColumn(rs: ResultSet, k: String): Try[Double] = Try(rs.getDouble(k))

  implicit def floatFromIndex(rs: ResultSet, i: Int): Try[Float] = Try(rs.getFloat(i))
  implicit def floatFromColumn(rs: ResultSet, k: String): Try[Float] = Try(rs.getFloat(k))

  implicit def byteFromIndex(rs: ResultSet, i: Int): Try[Byte] = Try(rs.getByte(i))
  implicit def byteFromColumn(rs: ResultSet, k: String): Try[Byte] = Try(rs.getByte(k))

  implicit def dateFromIndex(rs: ResultSet, i: Int): Try[Date] = Try(rs.getDate(i))
  implicit def dateFromColumn(rs: ResultSet, k: String): Try[Date] = Try(rs.getDate(k))

  implicit def intFromIndex(rs: ResultSet, i: Int): Try[Int] = Try(rs.getInt(i))
  implicit def intFromColumn(rs: ResultSet, k: String): Try[Int] = Try(rs.getInt(k))

  implicit def timeFromIndex(rs: ResultSet, i: Int): Try[Time] = Try(rs.getTime(i))
  implicit def timeFromColumn(rs: ResultSet, k: String): Try[Time] = Try(rs.getTime(k))

  implicit def timestampFromIndex(rs: ResultSet, i: Int): Try[Timestamp] = Try(rs.getTimestamp(i))
  implicit def timestampFromColumn(rs: ResultSet, k: String): Try[Timestamp] = Try(rs.getTimestamp(k))

  implicit def objectFromIndex(rs: ResultSet, i: Int): Try[Object] = Try(rs.getObject(i))
  implicit def objectFromColumn(rs: ResultSet, k: String): Try[Object] = Try(rs.getObject(k))


  implicit def pickInResultSet[O](p: Path)(implicit e1: (ResultSet, Int) => Try[O], e2: (ResultSet, String) => Try[O]) = { //(implicit r: RuleLike[ResultSet, O]): Rule[ResultSet, O] = {
    Rule[ResultSet, O] { rs =>
      p.path match {
        case IdxPathNode(i) :: t if t.isEmpty =>
          e1(rs, i) match {
            case scala.util.Success(s)               ⇒ Success(s)
            //case scala.util.Failure(e: SQLException) ⇒ Failure(Seq(Path -> Seq(ValidationError("error.sql", e.getMessage, e.getErrorCode))))
            case scala.util.Failure(e)               ⇒ Failure(Seq(Path -> Seq(ValidationError("error.required"))))
          }

        case KeyPathNode(k) :: t if t.isEmpty =>
          e2(rs, k) match {
            case scala.util.Success(s)               ⇒ Success(s)
            //case scala.util.Failure(e: SQLException) ⇒ Failure(Seq(Path -> Seq(ValidationError("error.sql", e.getMessage, e.getErrorCode))))
            case scala.util.Failure(e)               ⇒ Failure(Seq(Path -> Seq(ValidationError("error.required"))))
          }
        //case _ => Failure(Seq(p -> Seq(ValidationError("error.invalid"))))
      }
    }
  }


//  implicit def pick[Intermediate, Output](p: Path)(implicit e1: ResultSet ⇒ Int ⇒ Intermediate, e2: ResultSet ⇒ String ⇒ Intermediate, coerce: RuleLike[Intermediate, Output]): Rule[ResultSet, Output] = {
//
//    def atIndex(i: Int, extract: Int ⇒ Intermediate): VA[Intermediate] = Try(extract(i)) match {
//      case scala.util.Success(s) ⇒ Success(s)
//      case scala.util.Failure(e: SQLException) ⇒ Failure(Seq(p -> Seq(ValidationError("error.sql", e.getMessage, e.getErrorCode))))
//      case scala.util.Failure(e) ⇒ Failure(Seq(p -> Seq(ValidationError("error.required"))))
//    }
//
//    def atColumn(c: String, extract: String ⇒ Intermediate): VA[Intermediate] = Try(extract(c)) match {
//      case scala.util.Success(s) ⇒ Success(s)
//      case scala.util.Failure(e: SQLException) ⇒ Failure(Seq(p -> Seq(ValidationError("error.sql", e.getMessage, e.getErrorCode))))
//      case scala.util.Failure(e) ⇒ Failure(Seq(p -> Seq(ValidationError("error.required"))))
//    }
//
//    Rule[ResultSet, Intermediate] { rs ⇒
//      p.path match {
//        case IdxPathNode(i) :: _ ⇒ atIndex(i, rs)
//        case KeyPathNode(c) :: _ ⇒ atColumn(c, rs)
//      }
//    }.compose(coerce)
//  }

}
