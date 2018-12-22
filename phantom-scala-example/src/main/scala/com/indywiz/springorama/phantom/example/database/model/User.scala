package com.indywiz.springorama.phantom.example.database.model

import java.util.UUID

import com.outworkers.phantom.dsl._

import scala.concurrent.Future

// Base model used as DTO
case class User(id: UUID, firstName: String, lastName: String, email: String)

// Query based model
abstract class UserById extends Table[UserById, User] {

  // Override table metadata
  override def tableName: String = "user_by_id"

  // Define the table schema
  object id extends UUIDColumn with PartitionKey

  object fName extends StringColumn {
    override def name: String = "firstName"
  }

  object lName extends StringColumn {
    override def name: String = "lastName"
  }

  object email extends StringColumn


  // Define low level queries
  def createUserById(uuid: UUID, firstName: String, lastName: String, emailId: String): Future[ResultSet] = {
    insert
      .value(_.id, uuid)
      .value(_.fName, firstName)
      .value(_.lName, lastName)
      .value(_.email, emailId)
      .future()
  }

  def getUserById(uuid: UUID): Future[Option[User]] = select
    .all()
    .where(_.id eqs uuid)
    .one()

}


abstract class UserByFirstName extends Table[UserByFirstName, User] {
  // Override table metadata
  override def tableName: String = "user_by_first_name"

  // Define the table schema
  object fName extends StringColumn with PartitionKey {
    override def name: String = "firstName"
  }

  object id extends UUIDColumn

  object lName extends StringColumn {
    override def name: String = "lastName"
  }

  object email extends StringColumn

  // Define low level queries
  def createUserByUserName(uuid: UUID, firstName: String, lastName: String, emailId: String): Future[ResultSet] = {
    insert
      .value(_.id, uuid)
      .value(_.fName, firstName)
      .value(_.lName, lastName)
      .value(_.email, emailId)
      .future()
  }

  def getUserByFirstName(firstName: String): Future[Option[User]] = select
    .all()
    .where(_.fName eqs firstName)
    .one()

}
