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
  implicit def pickString[O](p: Path)(implicit r: RuleLike[String, O]): Rule[ResultSet, O] = {
    def extractString(p: Path, rs: ResultSet): Option[String] = p.path match {
      case IdxPathNode(i) :: _ ⇒ Try(rs.getString(i)).toOption
      case KeyPathNode(column) :: _ ⇒ Try(rs.getString(column)).toOption
    }

    Rule[ResultSet, String] { rs ⇒
      extractString(p, rs) match {
        case None    ⇒ Failure(Seq(p -> Seq(ValidationError("error.required"))))
        case Some(s) ⇒ Success(s)
      }
    }.compose(r)
  }
}
