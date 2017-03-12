package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.VoteDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Vote

import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait MongoVoteDao extends VoteDao {
  override def voteDao = new InMemVoteDao

  class InMemVoteDao extends VoteDao {

    var collection = DataStorage.getCollection("votes")

    override def getVoteCountForElection(electionId: UUID): Int = {
      val future = collection.count(Filters.eq("electionId", electionId.toString)).head()
      Await.result[Long](future, Duration.Inf).toInt
    }

    override def getVoteCountForElectionAndUser(electionId: UUID, userId: UUID): Int = {
      val future = collection.count(Filters.and(Filters.eq("electionId", electionId.toString), Filters.eq("userId", userId.toString))).head()
      Await.result[Long](future, Duration.Inf).toInt
    }

    override def getVoteCountForElectionAndCandidate(electionId: UUID, candidateId: UUID): Int = {
      val future = collection.count(Filters.and(Filters.eq("electionId", electionId.toString), Filters.eq("candidateId", candidateId.toString))).head()
      Await.result[Long](future, Duration.Inf).toInt
    }

    override def create(model: Vote): UUID = {
      val future = collection.insertOne(Document(
        "_id" -> model.id.toString,
        "userId" -> model.userId.toString,
        "candidateId" -> model.candidateId.toString,
        "electionId" -> model.electionId.toString
      )).head()

      try{
        Await.result[Completed](future, Duration.Inf)
        model.id
      } catch {
        case _: Exception => throw new EntityAlreadyExistsException(model)
      }
    }

    override def read(id: UUID): Option[Vote] = {
      val future = collection.find(Filters.eq("_id", id.toString)).head() map { v =>
        Option(Vote(
          id = UUID.fromString(v.getString("_id")),
          userId = UUID.fromString(v.getString("userId")),
          candidateId = UUID.fromString(v.getString("candidateId")),
          electionId = UUID.fromString(v.getString("electionId"))
        ))
      }

      try{
        Await.result[Option[Vote]](future, Duration.Inf)
      } catch {
        case _: IllegalStateException => None
      }
    }

    override def update(model: Vote): Vote = {
      val future = collection.findOneAndReplace(Filters.eq("_id", model.id.toString), Document(
        "_id" -> model.id.toString,
        "userId" -> model.userId.toString,
        "candidateId" -> model.candidateId.toString,
        "electionId" -> model.electionId.toString
      )).head() map { v=>
        Vote(
          id = UUID.fromString(v.getString("_id")),
          userId = UUID.fromString(v.getString("userId")),
          candidateId = UUID.fromString(v.getString("candidateId")),
          electionId = UUID.fromString(v.getString("electionId"))
        )
      }

      try{
        Await.result[Vote](future, Duration.Inf)
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
