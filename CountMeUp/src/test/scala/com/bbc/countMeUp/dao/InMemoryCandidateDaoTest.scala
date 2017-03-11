package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.dao.impl.InMemoryCandidateDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Candidate
import org.scalatest.{FunSpec, Matchers}

class InMemoryCandidateDaoTest extends FunSpec with Matchers{

  private class InMemoryCandidateDaoTest {
    this: InMemoryCandidateDao =>
  }

  private val target = new InMemoryCandidateDaoTest with InMemoryCandidateDao

  val testCandidate = Candidate(
    id = UUID.randomUUID(),
    name = "test candidate"
  )

  describe("create candidate test") {
    it("creating a candidate should not throw an exception when ID is unique") {
      val newId = UUID.randomUUID()
      target.candidateDao.create(testCandidate.copy(id = newId)) should equal(newId)
    }

    it("creating a candidate with a non unique id should throw an exception") {
      target.candidateDao.create(testCandidate)
      intercept[EntityAlreadyExistsException] {
        target.candidateDao.create(testCandidate)
      }
    }
  }

  describe("read candidate test") {
    it("attempting to read a candidate that does not exist should return None") {
      target.candidateDao.read(UUID.randomUUID()) should equal(None)
    }

    it("reading a candidate from an ID should return that candidate") {
      val candidateId = UUID.randomUUID()
      val readCandidate = testCandidate.copy(id = candidateId)
      target.candidateDao.create(readCandidate)

      target.candidateDao.read(candidateId).get should equal(readCandidate)
    }
  }

  describe("update candidate test") {
    it("updating a candidate should reflect the updates when read") {
      val candidateId = UUID.randomUUID()
      val initialCandidate = testCandidate.copy(id = candidateId)
      target.candidateDao.create(initialCandidate)
      target.candidateDao.read(candidateId).get.name should equal(initialCandidate.name)

      val updateCandidate = initialCandidate.copy(name = "newName")
      target.candidateDao.update(updateCandidate)

      target.candidateDao.read(candidateId).get should equal(updateCandidate)

    }

    it("updating a candidate that does not exist should throw an exception") {
      intercept[EntityDoesNotExistException] {
        target.candidateDao.update(testCandidate.copy(id = UUID.randomUUID()))
      }
    }
  }

  describe("delete candidate test") {
    it("should be able to delete a candidate that exists") {
      //hard deletes for right now
      val candidateId = UUID.randomUUID()
      val deleteCandidate = testCandidate.copy(id = candidateId)
      target.candidateDao.create(deleteCandidate)

      target.candidateDao.read(candidateId).get should equal(deleteCandidate)

      target.candidateDao.delete(candidateId)

      target.candidateDao.read(candidateId) should equal(None)
    }
  }
}
