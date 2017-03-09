package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.dao.impl.InMemoryVoteDao
import com.bbc.countMeUp.model.Vote
import org.scalatest.{FunSpec, Matchers}

class InMemoryVoteDaoTest extends FunSpec with Matchers{

  private class InMemoryVoteDaoTest{
    this: InMemoryVoteDao =>
  }

  private val target = new InMemoryVoteDaoTest with InMemoryVoteDao

  // user, candidate, and election ID don't need to match up to anything just yet, If I implement a DB there will be
  // foreign key constraints keeping random values from being used.
  val testVote = Vote(
    id = UUID.randomUUID(),
    userId = UUID.randomUUID(),
    candidateId = UUID.randomUUID(),
    electionId = UUID.randomUUID())

  describe("create vote test"){
    it("creating a vote should not throw an exception when ID is unique"){
      val newId = UUID.randomUUID()
      target.voteDao.create(testVote.copy(id = newId)) should equal(newId)
    }

    it("creating a vote with a non unique id should throw an exception"){
      //add the vote to the repository, then add the same vote again, this should throw an exception
      target.voteDao.create(testVote)
      intercept[Exception]{
        target.voteDao.create(testVote)
      }
    }
  }

  describe("read vote test"){
    it("attempting to read a vote that does not exist should return None"){
      target.voteDao.read(UUID.randomUUID()) should equal (None)
    }

    it("reading a vote from an ID should return that vote"){
      val voteId = UUID.randomUUID()
      val readVote = testVote.copy(id = voteId)
      target.voteDao.create(readVote)

      target.voteDao.read(voteId).get should equal(readVote)
    }
  }

  describe("update vote test"){
    it("updating a vote should reflect the updates when read"){
      val voteId = UUID.randomUUID()
      val initialVote = testVote.copy(id = voteId)
      target.voteDao.create(initialVote)
      target.voteDao.read(voteId).get.electionId should equal(testVote.electionId)

      val newElectionId = UUID.randomUUID()
      val updatedVote = initialVote.copy(electionId = newElectionId)
      target.voteDao.update(updatedVote)

      target.voteDao.read(voteId).get should equal(updatedVote)

    }

    it("updating a vote that does not exist should throw an exception"){
      intercept[Exception]{
        target.voteDao.update(testVote.copy(id = UUID.randomUUID()))
      }
    }
  }

  describe("delete vote test"){
    it("should be able to delete a vote that exists")
    //hard deletes for right now
    val voteId = UUID.randomUUID()
    val deleteVote = testVote.copy(id = voteId)
    target.voteDao.create(deleteVote)

    target.voteDao.read(voteId).get should equal(deleteVote)

    target.voteDao.delete(voteId)

    intercept[Exception]{
      target.voteDao.read(voteId)
    }
  }

}
