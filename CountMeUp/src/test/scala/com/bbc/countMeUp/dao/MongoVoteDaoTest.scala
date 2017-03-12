package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.dao.impl.MongoVoteDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Vote
import org.scalatest.{FunSpec, Matchers}

class MongoVoteDaoTest extends FunSpec with Matchers {

  private class InMemoryVoteDaoTest {
    this: MongoVoteDao =>
  }

  private val target = new InMemoryVoteDaoTest with MongoVoteDao

  // user, candidate, and election ID don't need to match up to anything just yet, If I implement a DB there will be
  // foreign key constraints keeping random values from being used.
  val testVote = Vote(
    id = UUID.randomUUID(),
    userId = UUID.randomUUID(),
    candidateId = UUID.randomUUID(),
    electionId = UUID.randomUUID())

  describe("create vote test") {
    it("creating a vote should not throw an exception when ID is unique") {
      val newId = UUID.randomUUID()
      target.voteDao.create(testVote.copy(id = newId)) should equal(newId)
    }

    it("creating a vote with a non unique id should throw an exception") {
      //add the vote to the repository, then add the same vote again, this should throw an exception
      target.voteDao.create(testVote)
      intercept[EntityAlreadyExistsException] {
        target.voteDao.create(testVote)
      }
    }
  }

  describe("read vote test") {
    it("attempting to read a vote that does not exist should return None") {
      target.voteDao.read(UUID.randomUUID()) should equal(None)
    }

    it("reading a vote from an ID should return that vote") {
      val voteId = UUID.randomUUID()
      val readVote = testVote.copy(id = voteId)
      target.voteDao.create(readVote)

      target.voteDao.read(voteId).get should equal(readVote)
    }
  }

  describe("update vote test") {
    it("updating a vote should reflect the updates when read") {
      val voteId = UUID.randomUUID()
      val initialVote = testVote.copy(id = voteId)
      target.voteDao.create(initialVote)
      target.voteDao.read(voteId).get.electionId should equal(testVote.electionId)

      val newElectionId = UUID.randomUUID()
      val updatedVote = initialVote.copy(electionId = newElectionId)
      target.voteDao.update(updatedVote)

      target.voteDao.read(voteId).get should equal(updatedVote)

    }

    it("updating a vote that does not exist should throw an exception") {
      intercept[EntityDoesNotExistException] {
        target.voteDao.update(testVote.copy(id = UUID.randomUUID()))
      }
    }
  }

  describe("delete vote test") {
    it("should be able to delete a vote that exists") {
      //hard deletes for right now
      val voteId = UUID.randomUUID()
      val deleteVote = testVote.copy(id = voteId)
      target.voteDao.create(deleteVote)

      target.voteDao.read(voteId).get should equal(deleteVote)

      target.voteDao.delete(voteId)

      target.voteDao.read(voteId) should equal(None)
    }
  }

  describe("get votes for election and users") {
    it("should return all votes for a user and election") {
      val electionId = UUID.randomUUID()
      val userId = UUID.randomUUID()
      val voteIds = for (x <- 1 to 10) yield {
        target.voteDao.create(testVote.copy(
          id = UUID.randomUUID(),
          electionId = electionId,
          userId = if (x <= 5)
            userId
          else
            UUID.randomUUID()
        ))
      }

      val readVotes = target.voteDao.getVoteCountForElectionAndUser(electionId, userId)
      readVotes should equal(5)
    }
  }

  describe("get votes for election and candidate") {
    it("should return all votes for a candidate and election") {
      val electionId = UUID.randomUUID()
      val candidateId = UUID.randomUUID()
      val voteIds = for (x <- 1 to 10) yield {
        target.voteDao.create(testVote.copy(
          id = UUID.randomUUID(),
          electionId = electionId,
          candidateId = if (x <= 5)
            candidateId
          else
            UUID.randomUUID()
        ))
      }

      val readVotes = target.voteDao.getVoteCountForElectionAndCandidate(electionId, candidateId)
      readVotes should equal(5)
    }

    it("should be able to read one million records in less then a second") {
      val electionId = UUID.randomUUID()
      val candidateId = UUID.randomUUID()
      val voteIds = for (x <- 1 to 1000000) yield {
        target.voteDao.create(testVote.copy(
          id = UUID.randomUUID(),
          electionId = electionId,
          candidateId = if (x % 3 == 0)
            candidateId
          else
            UUID.randomUUID()
        ))
      }

      val startTime = System.currentTimeMillis()
      val readVotes = target.voteDao.getVoteCountForElectionAndCandidate(electionId, candidateId)
      val endTime = System.currentTimeMillis()
      val runtime = endTime - startTime

      runtime < 1000 should be(true)
      readVotes should equal(voteIds.size / 3)
    }

//    it("should be able to read ten million records in less then a second") {
//      val electionId = UUID.randomUUID()
//      val candidateId = UUID.randomUUID()
//      val voteIds = for (x <- 1 to 10000000) yield {
//        target.voteDao.create(testVote.copy(
//          id = UUID.randomUUID(),
//          electionId = electionId,
//          candidateId = candidateId
//        ))
//      }
//
//      val startTime = System.currentTimeMillis()
//      val readVotes = target.voteDao.getVoteCountForElectionAndCandidate(electionId, candidateId)
//      val endTime = System.currentTimeMillis()
//      val runtime = endTime - startTime
//
//      runtime should be < 1000L
//      readVotes should equal(voteIds.size)
//    }
  }
}
