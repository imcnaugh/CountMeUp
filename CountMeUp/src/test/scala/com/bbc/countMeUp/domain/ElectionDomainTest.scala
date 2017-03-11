package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{CandidateDao, ElectionDao, VoteDao}
import com.bbc.countMeUp.model.{Candidate, Election}
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{FunSpec, Matchers}

class ElectionDomainTest extends FunSpec with Matchers{

  trait MockElectionDao extends ElectionDao{
    val electionDao = org.scalatest.mockito.MockitoSugar.mock[ElectionDao]
  }

  trait MockVoteDao extends VoteDao{
    val voteDao = org.scalatest.mockito.MockitoSugar.mock[VoteDao]
  }

  trait MockCandidateDao extends CandidateDao{
    val candidateDao = org.scalatest.mockito.MockitoSugar.mock[CandidateDao]
  }

  val domain = new ElectionDomain with MockElectionDao with MockVoteDao with MockCandidateDao

  describe("create election test"){
    it("election should be assigned a unique id, and inserted properly"){
      val maxVotesPerUser = 3
      val candidate1 = Candidate(UUID.randomUUID(), "candidate 1")
      val candidate2 = Candidate(UUID.randomUUID(), "candidate 2")
      val candidate3 = Candidate(UUID.randomUUID(), "candidate 3")

      when(domain.electionDao.create(Election(
        id = (any[UUID]),
        candidates = Set(
          candidate1,
          candidate2,
          candidate3),
        maxVotesPerUser = 3))).thenAnswer(new Answer[UUID] {
          override def answer(invocation: InvocationOnMock): UUID = invocation.getArguments()(0).asInstanceOf[Election].id})

      when(domain.candidateDao.read(candidate1.id)).thenReturn(Option(candidate1))
      when(domain.candidateDao.read(candidate2.id)).thenReturn(Option(candidate2))
      when(domain.candidateDao.read(candidate3.id)).thenReturn(Option(candidate3))

      when(domain.electionDao.read(any[UUID])).thenReturn(None)

      val newElection = domain.addElection(
        Set(
          candidate1.id,
          candidate2.id,
          candidate3.id),
        maxVotesPerUser)

      newElection.id should not be(None)
      newElection.maxVotesPerUser should equal(maxVotesPerUser)
      newElection.candidates should equal(Set(candidate1,candidate2, candidate3))

      verify(domain.electionDao, times(1)).create(any[Election])
      // 3 one for each candidate
      verify(domain.candidateDao, times(3)).read(any[UUID])
    }

    it("addElection should throw an exception if all candidates do not exist"){
      val maxVotesPerUser = 3
      val candidate1 = Candidate(UUID.randomUUID(), "candidate 1")
      val candidate2 = Candidate(UUID.randomUUID(), "candidate 2")
      val candidate3 = Candidate(UUID.randomUUID(), "candidate 3")

      when(domain.electionDao.create(Election(
        id = (any[UUID]),
        candidates = Set(
          candidate1,
          candidate2,
          candidate3),
        maxVotesPerUser = 3))).thenAnswer(new Answer[UUID] {
        override def answer(invocation: InvocationOnMock): UUID = invocation.getArguments()(0).asInstanceOf[Election].id})

      when(domain.candidateDao.read(candidate1.id)).thenReturn(Option(candidate1))
      when(domain.candidateDao.read(candidate2.id)).thenReturn(Option(candidate2))
      when(domain.candidateDao.read(candidate3.id)).thenReturn(None)

      when(domain.electionDao.read(any[UUID])).thenReturn(None)

      intercept[Exception] {
        val newElection = domain.addElection(
          Set(
            candidate1.id,
            candidate2.id,
            candidate3.id),
          maxVotesPerUser)
      }

      verify(domain.electionDao, times(1)).create(any[Election])
    }
  }

}
