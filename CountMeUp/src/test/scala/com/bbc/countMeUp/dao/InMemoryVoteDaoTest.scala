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
      target.voteDao.create(testVote.copy(id = UUID.randomUUID())) should equal(None)
    }

    it("creating a vote with a non unique id should throw an exception"){
      //add the vote to the repository, then add the same vote again, this should throw an exception
      target.voteDao.create(testVote)
      intercept[Exception]{
        target.voteDao.create(testVote)
      }
    }
  }

}
