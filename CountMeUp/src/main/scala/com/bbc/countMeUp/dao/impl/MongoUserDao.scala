package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.User
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait MongoUserDao extends UserDao {
  override def userDao = new InMemUserDao

  class InMemUserDao extends UserDao {

    val collection = DataStorage.getCollection("users")

    override def create(model: User): UUID = {
      val future = collection.insertOne(Document(
        "_id" -> model.id.toString,
        "name" -> model.name
      )).head()

      try{
        Await.result[Completed](future, Duration.Inf)
        model.id
      } catch {
        case _: Exception => throw new EntityAlreadyExistsException(model)
      }
    }

    override def read(id: UUID): Option[User] = {
      val future = collection.find(Filters.eq("_id", id.toString)).head() map { u=>
        Option(User(
          id = UUID.fromString(u.getString("_id")),
          name = u.getString("name")
        ))
      }

      try{
        Await.result[Option[User]](future, Duration.Inf)
      } catch {
        case _: IllegalStateException => None
      }
    }

    override def update(model: User): User = {
      val future = collection.findOneAndReplace(Filters.eq("_id", model.id.toString), Document(
        "_id" -> model.id.toString,
        "name" -> model.name
      )).head() map { u =>
        User(
          id = UUID.fromString(u.getString("_id")),
          name = u.getString("name")
        )
      }

      try{
        Await.result[User](future, Duration.Inf)
      } catch {
        case _: Exception => throw new EntityDoesNotExistException(model.id)
      }
    }

    override def delete(id: UUID): Unit = {
      val future = collection.deleteOne(Filters.eq("_id", id.toString)).head()
      Await.result[DeleteResult](future,Duration.Inf)
    }
  }

}
