package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.ElectionDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.{Candidate, Election}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait InMemoryElectionDao extends ElectionDao{
  override def electionDao = new InMemElectionDao

  class InMemElectionDao extends ElectionDao {

    var collection = DataStorage.getCollection("elections")

    override def create(model: Election): UUID = {
      val candidates: List[Document] = model.candidates.map(c =>{
        Document(
          "id" -> c.id.toString,
          "name" -> c.name
        )
      }).toList

      val future = collection.insertOne(Document(
        "_id" -> model.id.toString,
        "candidates" -> candidates,
        "maxVotesPerUser" -> model.maxVotesPerUser)).head()

      try{
        Await.result[Completed](future, Duration.Inf)
        model.id
      } catch {
        case _: Exception => throw new EntityAlreadyExistsException(model)
      }
    }

    override def read(id: UUID): Option[Election] = {
      val future = collection.find(Filters.eq("_id", id.toString)).head() map (e =>{
        val jsonString = e.toJson()
        val candidateString = e.toJson().substring(jsonString.indexOf('[') + 2, jsonString.indexOf(']') -1)
        val candidateStringArray = candidateString.split("\\},\\W+\\{")

        val candidates: Set[Candidate] = candidateStringArray.map(sa =>{
          val args = sa.replace(" ", "")split(",")
          val id = args(0).split(":")(1).substring(1, 37)
          val name = args(1).split(":")(1).substring(1)
          Candidate(
            id = UUID.fromString(id),
            name = name.subSequence(0, name.size-1).toString
          )
        }).toSet

        Option(Election(
          id = UUID.fromString(e.getString("_id")),
          candidates = candidates,
          maxVotesPerUser = e.getInteger("maxVotesPerUser")
        ))
      })

      try{
        Await.result[Option[Election]](future, Duration.Inf)
      } catch {
        case _: IllegalStateException => None
      }
    }

    override def update(model: Election): Election = {
      val candidates: List[Document] = model.candidates.map(c =>{
        Document(
          "id" -> c.id.toString,
          "name" -> c.name
        )
      }).toList

      val future = collection.findOneAndReplace(Filters.eq("_id", model.id.toString), Document(
        "_id" -> model.id.toString,
        "candidates" -> candidates,
        "maxVotesPerUser" -> model.maxVotesPerUser)).head() map (e =>{
        val jsonString = e.toJson()
        val candidateString = e.toJson().substring(jsonString.indexOf('[') + 2, jsonString.indexOf(']') -1)
        val candidateStringArray = candidateString.split("\\},\\W+\\{")

        val candidates: Set[Candidate] = candidateStringArray.map(sa =>{
          val args = sa.replace(" ", "")split(",")
          val id = args(0).split(":")(1).substring(1, 37)
          val name = args(1).split(":")(1).substring(1)
          Candidate(
            id = UUID.fromString(id),
            name = name.subSequence(0, name.size-1).toString
          )
        }).toSet

        Election(
          id = UUID.fromString(e.getString("_id")),
          candidates = candidates,
          maxVotesPerUser = e.getInteger("maxVotesPerUser")
        )
      })

      try{
        Await.result[Election](future, Duration.Inf)
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
