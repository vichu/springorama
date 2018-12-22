package com.indywiz.springorama.phantom.example

import java.util.UUID

import com.indywiz.springorama.phantom.example.database.provider.UserDatabase
import com.indywiz.springorama.phantom.example.database.service.UserDatabaseService
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

object Main extends App {

  val logger: Logger = LoggerFactory.getLogger(classOf[App])
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  // Construct cassandra connection
  implicit val cassandraConnection: CassandraConnection = {
    ContactPoint.local.keySpace("user_keyspace")
  }

  logger.info("create cassandra connection")

  // Construct database instance from Database provider
  object UserDatabase extends UserDatabase
  object databaseService extends UserDatabaseService {
    override def database: UserDatabase = UserDatabase
  }

  logger.info("created tables if not present")

  // Use the instance to perform queries.
  val userPair = for{
    uuid <- databaseService.createUser(UUID.nameUUIDFromBytes("Sumanth".getBytes), "Sumanth", "Kumar", "sumanth@indywiz.com")
    userByFirstName <- databaseService.selectUserByFirstName("Sumanth")
    userById <- databaseService.selectUserById(uuid.get)
  } yield (userById, userByFirstName)

  val res = userPair.map {
    case (userById, userByFirstName) =>
      logger.info(s"User by Id : ${userById.get}")
      logger.info(s"User by first name: ${userByFirstName.get}")
  }

  logger.info("Exiting")
}
