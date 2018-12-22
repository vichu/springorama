package com.indywiz.springorama.phantom.example.database.service

import java.util.UUID

import com.indywiz.springorama.phantom.example.database.model.User
import com.indywiz.springorama.phantom.example.database.provider.UserDbProvider
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

trait UserDatabaseService extends UserDbProvider {

  // Orchestrate your low level queries appropriately.
  def createUser(uuid: UUID, firstName: String, lastName: String, email:String): Future[Option[UUID]] =
    database.userByIdInstance.createUserById(uuid, firstName, lastName, email)
    .flatMap(_ => {
      database.userByFirstName.createUserByUserName(uuid, firstName, lastName, email)
        .map(rs => if(rs.wasApplied) Some(uuid) else None)
    })

  def selectUserById(uuid: UUID): Future[Option[User]] = database.userByIdInstance.getUserById(uuid)

  def selectUserByFirstName(firstName: String): Future[Option[User]] = database.userByFirstName.getUserByFirstName(firstName)

}
