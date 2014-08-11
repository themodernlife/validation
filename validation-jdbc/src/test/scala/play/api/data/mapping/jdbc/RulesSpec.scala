/**
 * Copyright (C) 2014 MediaMath <http://www.mediamath.com>
 *
 * @author ihummel
 */
package play.api.data.mapping.jdbc

import java.sql.{DriverManager, ResultSet}

import org.specs2.mock._
import org.specs2.mutable._
import play.api.data.mapping._
import play.api.data.mapping.jdbc.Rules._

object RulesSpec extends Specification with Mockito {

  "Rules" should {
    val ddl =
      """
        |create table people (
        |  name varchar(64) not null,
        |  email varchar(64) not null,
        |  age integer,
        |  per_hour double,
        |  us_citizen boolean
        |)
      """.stripMargin

    val inserts =
      """
        | insert into people values ('Ian', 'ian@example.com', 33, 4.25, true);
        | insert into people values ('Joe', 'joe@example.com', 21, 5.25, false);
        | insert into people values ('Jill', 'jill@example.com', 43, 9.25, false);
        | insert into people values ('Nils', 'nils@example.com', NULL, NULL, NULL);
      """.stripMargin

    val driver = Class.forName("org.hsqldb.jdbcDriver")
    val connection = DriverManager.getConnection("jdbc:hsqldb:mem:unit-test", "", "")
    val statement = connection.createStatement()

    statement.execute(ddl);
    statement.execute(inserts);

    val resultSet = {
      val query = "select * from people where name = 'Ian'"
      val rs = statement.executeQuery(query)
      rs.next()
      rs
    }

    println(s"ResulSet is ${resultSet}")

    "extract data using column names" in {
      From[ResultSet] { __ =>
        (__ \ "name").read[String]
      }.validate(resultSet) mustEqual(Success("Ian"))

      val error = Failure(Seq((Path \ "foo") -> Seq(ValidationError("error.required"))))
      From[ResultSet] { __ =>
        (__ \ "foo").read[String]
      }.validate(resultSet) mustEqual(error)
    }

    "extract data using column indices" in {
      From[ResultSet] { __ =>
        (__ \ 1).read[String]
      }.validate(resultSet) mustEqual(Success("Ian"))

      val error = Failure(Seq((Path \ 10) -> Seq(ValidationError("error.required"))))
      From[ResultSet] { __ =>
        (__ \ 10).read[String]
      }.validate(resultSet) mustEqual(error)
    }

    /*
    name varchar(64) not null,
  email varchar(64) not null,
  age integer,
  per_hour double,
  us_citizen boolean
     */

    "read people" in {
      case class Person(name: String, email: String, age: Int, perHour: Double, isUsCitizen: Boolean)

      From[ResultSet] { __ =>
        ((__ \ "name").read[String] ~
        (__ \ "email").read[String] ~
        (__ \ "age").read[Int] ~
        (__ \ "per_hour").read[Double] ~
        (__ \ "us_citizen").read[Boolean])(Person)
      }.validate(resultSet) mustEqual(Success(Person("Ian", "ian@example.com", 33, 4.25, true)))
    }

    "read options" in {
      case class Person(name: String, email: String, age: Option[Int], perHour: Option[Double], isUsCitizen: Option[Boolean])

      From[ResultSet] { __ =>
        ((__ \ "name").read[String] ~
          (__ \ "email").read[String] ~
          (__ \ "age").read[Option[Int]] ~
          (__ \ "per_hour").read[Option[Double]] ~
          (__ \ "us_citizen").read[Option[Boolean]])(Person)
      }.validate(resultSet) mustEqual(Success(Person("Nils", "nils@example.com", None, None, None)))
    }
  }
}
