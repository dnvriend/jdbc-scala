package com.example

import java.sql.{Timestamp, ResultSet}
import java.util.Date
import org.joda.time.DateTime
import org.springframework.scala.jdbc.core.JdbcTemplate
import scalikejdbc._

object Entities {
  import org.joda.time._

  case class Member(id: Long, name: Option[String], createdAt: DateTime)

  object Member extends SQLSyntaxSupport[Member] {
    override val tableName = "members"
    def apply(rs: WrappedResultSet) = new Member(rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at"))
  }
}

/**
 * see: http://scalikejdbc.org/
 *
 * ScalikeJdbc has some nice query dsl and sql interpolation going for it
 */
object ScalikeMain extends App with ScalikeConnection with H2JdbcConfig {
  import Entities._
  implicit val session = AutoSession
  sql"""
    create table members (
      id serial not null primary key,
      name varchar(64),
      created_at timestamp not null
    ) """.execute().apply()

  // insert initial data
  Seq("Alice", "Bob", "Chris") foreach { name =>
    sql"insert into members (name, created_at) values (${name}, current_timestamp)".update().apply()
  }

  // for now, retrieves all data as Map value
  val entities: List[Map[String, Any]] = sql"select * from members".map(_.toMap()).list().apply()
  assert(entities.size == 3)

  // find all members
  val members: List[Member] = sql"select * from members".map(rs => Member(rs)).list().apply()
  assert(members.size == 3)
  println(s"Members: $members")

  val m = Member.syntax("m")
  val alice: Option[Member] =
    withSQL {
      select.from(Member as m).where.eq(m.name, "Alice")
    }.map(rs => Member(rs)).single().apply()

  alice match {
    case Some(Member(_, Some(n), _)) if n == "Alice" => println("Found " + n)
    case _ => throw new Error("No Alice")
  }
}

/**
 * see: https://github.com/spring-projects/spring-scala/wiki/Using-Spring-Templates-in-Scala
 *
 * When renaming the ScalaJdbcTemplate to sql, it it not that bad
 * */
object SpringMain extends App with DbcpConnection with H2JdbcConfig {
  import Entities._
  val sql = new JdbcTemplate(dataSource)

  val create = """ create table members (
                 |      id serial not null primary key,
                 |      name varchar(64),
                 |      created_at timestamp not null
                 |    )""".stripMargin

  sql.execute(create)
  // insert initial data
  Seq("Alice", "Bob", "Chris") foreach { name =>
    sql.execute(s"insert into members (name, created_at) values ('$name', current_timestamp)")
  }

  val members: Seq[Member] = sql.queryAndMap[Member]("select * from members") { (rs: ResultSet, _) =>
    Member(rs.getLong("id"), Option(rs.getString("name")), new DateTime(rs.getTimestamp("created_at")))
  }
  assert(members.size == 3)
  println(s"Members: $members")

  val alice: Option[Member] = sql.queryForObjectAndMap[Member]("select * from members where name = ?", "Alice") { (rs: ResultSet, _) =>
    Member(rs.getLong("id"), Option(rs.getString("name")), new DateTime(rs.getTimestamp("created_at")))
  }

  alice match {
    case Some(Member(_, Some(n), _)) if n == "Alice" => println("Found " + n)
    case _ => throw new Error("No Alice")
  }
}

object SlickMain extends App {

  import scala.slick.driver.H2Driver.simple._

  class Members(tag: Tag) extends Table[(Option[Int], String, Timestamp)](tag, "members") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc, O.Nullable)
    def name = column[String]("name", O.NotNull)
    def createdAt = column[Timestamp]("created_at", O.NotNull)
    def * = (id, name, createdAt)
  }

  val members = TableQuery[Members]

  Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession { implicit session =>

    // create the table
    members.ddl.create

    Seq("Alice", "Bob", "Chris") foreach { (name: String) =>
      members += (None, name, new Timestamp(new Date().getTime))
    }

    assert(members.list().size == 3)
    println(members.list())

    val alice = members.filter(_.name === "Alice")
    alice.foreach {
      case (id, name, createdAt) =>
        println(s"id: $id, name: $name, $createdAt")
    }
  }
}