package com.indywiz.springorama.phantom.example.database.provider
import com.indywiz.springorama.phantom.example.database.model.{UserByFirstName, UserById}
import com.outworkers.phantom.dsl._

// This class will encapsulate all the valid database instances
class UserDatabase(implicit cassandraConnection: CassandraConnection)
  extends Database[UserDatabase](connector = cassandraConnection) {
  object userByIdInstance extends UserById with Connector
  object userByFirstName extends UserByFirstName with Connector
}

// This trait will act as a database instance provider.
trait UserDbProvider extends DatabaseProvider[UserDatabase]
