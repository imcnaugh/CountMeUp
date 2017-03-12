package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.dao.impl.MongoElectionDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.{Candidate, Election}
import org.scalatest.{FunSpec, Matchers}

class MongoElectionDaoTest extends FunSpec with Matchers {

  private class InMemoryElectionDaoTest {
    this: MongoElectionDao =>
  }

  private val target = new InMemoryElectionDaoTest with MongoElectionDao

  val testElection = Election(
    id = UUID.randomUUID(),
    candidates = Set(Candidate(UUID.randomUUID(), "c1"), Candidate(UUID.randomUUID(), "c2")),
    maxVotesPerUser = 3
  )

  describe("create election test") {
    it("creating a election should not throw an exception when ID is unique") {
      val newId = UUID.randomUUID()
      target.electionDao.create(testElection.copy(id = newId)) should equal(newId)
    }

    it("creating a election with a non unique id should throw an exception") {
      target.electionDao.create(testElection)
      intercept[EntityAlreadyExistsException] {
        target.electionDao.create(testElection)
      }
    }
  }

  describe("read election test") {
    it("attempting to read a election that does not exist should return None") {
      target.electionDao.read(UUID.randomUUID()) should equal(None)
    }

    it("reading a election from an ID should return that election") {
      val electionId = UUID.randomUUID()
      val readElection = testElection.copy(id = electionId)
      target.electionDao.create(readElection)

      target.electionDao.read(electionId).get should equal(readElection)
    }
  }

  describe("update election test") {
    it("updating a election should reflect the updates when read") {
      val electionId = UUID.randomUUID()
      val initialElection = testElection.copy(id = electionId)
      target.electionDao.create(initialElection)
      target.electionDao.read(electionId).get.maxVotesPerUser should equal(initialElection.maxVotesPerUser)

      val updateElection = initialElection.copy(maxVotesPerUser = 20)
      target.electionDao.update(updateElection)

      target.electionDao.read(electionId).get should equal(updateElection)

    }

    it("updating a election that does not exist should throw an exception") {
      intercept[EntityDoesNotExistException] {
        target.electionDao.update(testElection.copy(id = UUID.randomUUID()))
      }
    }
  }

  describe("delete election test") {
    it("should be able to delete a election that exists") {
      //hard deletes for right now
      val electionId = UUID.randomUUID()
      val deleteElection = testElection.copy(id = electionId)
      target.electionDao.create(deleteElection)

      target.electionDao.read(electionId).get should equal(deleteElection)

      target.electionDao.delete(electionId)

      target.electionDao.read(electionId) should equal(None)
    }
  }
}
