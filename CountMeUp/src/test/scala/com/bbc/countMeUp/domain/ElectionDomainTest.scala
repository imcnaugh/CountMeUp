package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{CandidateDao, ElectionDao, VoteDao}
import com.bbc.countMeUp.model.{Candidate, CandidateTally, Election, ElectionResults}
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{FunSpec, Matchers}
import sun.reflect.annotation.ExceptionProxy

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

  describe("get election results tests"){
    it("returns an election when asked for one that exists"){
      val candidate1 = Candidate(UUID.randomUUID(), "candidate1")
      val candidate2 = Candidate(UUID.randomUUID(), "candidate2")
      val candidate3 = Candidate(UUID.randomUUID(), "candidate3")
      val candidate1Votes = 12342
      val candidate2Votes = 10000000
      val candidate3Votes = 4298402
      val election = Election(
        id = UUID.randomUUID(),
        candidates = Set(
          candidate1, candidate2, candidate3
        ),
        maxVotesPerUser = 3)

      when(domain.electionDao.read(election.id)).thenReturn(Option(election))
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate1.id)).thenReturn(candidate1Votes)
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate2.id)).thenReturn(candidate2Votes)
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate3.id)).thenReturn(candidate3Votes)

      val expectedCandidateTallys = Seq(
        CandidateTally(candidate1, candidate1Votes),
        CandidateTally(candidate2, candidate2Votes),
        CandidateTally(candidate3, candidate3Votes)
      )

      val electionResults = domain.getElectionResults(election.id)

      electionResults.electionId should equal(election.id)
      electionResults.maxVotesPerUser should equal(election.maxVotesPerUser)
      electionResults.results should equal(expectedCandidateTallys)

      verify(domain.electionDao, times(1)).read(election.id)
      verify(domain.voteDao, times(1)).getVoteCountForElectionAndCandidate(election.id, candidate1.id)
      verify(domain.voteDao, times(1)).getVoteCountForElectionAndCandidate(election.id, candidate2.id)
      verify(domain.voteDao, times(1)).getVoteCountForElectionAndCandidate(election.id, candidate3.id)
    }

    it("should throw an exception if looking for an election that does not exist"){
      val candidate1 = Candidate(UUID.randomUUID(), "candidate1")
      val candidate2 = Candidate(UUID.randomUUID(), "candidate2")
      val candidate3 = Candidate(UUID.randomUUID(), "candidate3")
      val candidate1Votes = 12342
      val candidate2Votes = 10000000
      val candidate3Votes = 4298402
      val election = Election(
        id = UUID.randomUUID(),
        candidates = Set(
          candidate1, candidate2, candidate3
        ),
        maxVotesPerUser = 3)

      when(domain.electionDao.read(election.id)).thenReturn(Option(election))
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate1.id)).thenReturn(candidate1Votes)
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate2.id)).thenReturn(candidate2Votes)
      when(domain.voteDao.getVoteCountForElectionAndCandidate(election.id, candidate3.id)).thenReturn(candidate3Votes)

      intercept[Exception]{
        val electionResults = domain.getElectionResults(election.id)
      }
    }
  }

}
