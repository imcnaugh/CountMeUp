package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Candidate
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait InMemoryCandidateDao extends CandidateDao{
  override def candidateDao = new InMemCandidateDao

  var candidates = DataStorage.candidates

  class InMemCandidateDao extends CandidateDao {

    val mongoclient: MongoClient = MongoClient()
    val database: MongoDatabase = mongoclient.getDatabase("bbc")
    val collection: MongoCollection[Document] = database.getCollection[Document]("candidates")

    override def create(model: Candidate): UUID = {
      val future =collection.insertOne(Document(
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

    override def read(id: UUID): Option[Candidate] = {
      val future = collection.find(Filters.eq("_id", id.toString)).head() map { c =>
        Option(Candidate(
          id = UUID.fromString(c.getString("_id")),
          name = c.getString("name")))}

      try{
        Await.result[Option[Candidate]](future, Duration.Inf)
      } catch{
        case _: IllegalStateException => None
      }
    }

    override def update(model: Candidate): Candidate = {
      val future = collection.findOneAndReplace(Filters.eq("_id", model.id.toString), Document(
        "_id" -> model.id.toString,
        "name" -> model.name
      )).head() map {c =>
        Candidate(
          id = UUID.fromString(c.getString("_id")),
          name = c.getString("name"))
      }

      try{
        Await.result[Candidate](future, Duration.Inf)
      } catch {
        case _: Exception => throw new EntityDoesNotExistException(model.id)
      }

    }

    override def delete(id: UUID): Unit = {
      val future = collection.deleteOne(Filters.eq("_id", id.toString)).head()
      Await.result[DeleteResult](future, Duration.Inf)
    }
  }
}
