/**
 * Copyright (C) 2014 MediaMath <http://www.mediamath.com>
 *
 * @author ihummel
 */
package play.api.data.mapping.jdbc

import java.sql.{SQLException, ResultSet}

import scala.util.Try

import play.api.data.mapping._

object Rules extends DefaultRules[ResultSet] {
  implicit def stringGI(rs: ResultSet): Int ⇒ String = rs.getString
  implicit def stringGC(rs: ResultSet): String ⇒ String = rs.getString

  implicit def intGI(rs: ResultSet): Int ⇒ Int = rs.getInt
  implicit def intGC(rs: ResultSet): String ⇒ Int = rs.getInt

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
