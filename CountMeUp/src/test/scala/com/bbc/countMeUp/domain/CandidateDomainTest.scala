package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.exception.EntityDoesNotExistException
import com.bbc.countMeUp.model.Candidate
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{FunSpec, Matchers}

class CandidateDomainTest extends FunSpec with Matchers {

  trait MockCandidateDao extends CandidateDao {
    val candidateDao = org.scalatest.mockito.MockitoSugar.mock[CandidateDao]
  }

  val domain = new CandidateDomain with MockCandidateDao

  describe("add candidate tests"){
    it("candidates should be assigned a unique id, and inserted properly"){
      val name = "testing"

      when(domain.candidateDao.create(Candidate((any[UUID]), name))).thenAnswer(new Answer[UUID] {
        override def answer(invocation: InvocationOnMock): UUID = invocation.getArguments()(0).asInstanceOf[Candidate].id})

      when(domain.candidateDao.read(any[UUID])).thenReturn(None)

      val newCandidate = domain.addCandidate(name)
      newCandidate.id should not be(None)
      newCandidate.name should equal(name)
    }
  }

  describe("get candidate tests"){
    it("looking up a candidate should succeed if the candidate exists"){
      val id = UUID.randomUUID()
      val name = "test candidate"
      when(domain.candidateDao.read(id)).thenReturn(Option(Candidate(id = id, name = name)))

      Candidate(id = id, name = name) should equal(domain.getCandidate(id))
    }

    it("looking up a candidate should fail if the candidate does not exist"){
      val id = UUID.randomUUID()
      when(domain.candidateDao.read(id)).thenReturn(None)

      intercept[EntityDoesNotExistException] {
        domain.getCandidate(id)
      }
    }
  }
}
