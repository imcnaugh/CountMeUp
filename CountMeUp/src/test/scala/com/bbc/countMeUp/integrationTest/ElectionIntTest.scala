package com.bbc.countMeUp.integrationTest

import com.bbc.countMeUp.dao.impl.{InMemoryCandidateDao, InMemoryElectionDao, InMemoryVoteDao}
import com.bbc.countMeUp.domain.{CandidateDomain, ElectionDomain}
import com.bbc.countMeUp.model.Candidate
import org.scalatest.{FunSpec, Matchers}

class ElectionIntTest extends FunSpec with Matchers {
  val candidateDomain = new CandidateDomain with InMemoryCandidateDao
  val electionDomain = new ElectionDomain with InMemoryElectionDao with InMemoryVoteDao with  InMemoryCandidateDao

  describe("Election round trip test"){
    it("I should be able to add a election, and get its results with no data loss"){
      val candidate1 = candidateDomain.addCandidate("candidate 1")
      val candidate2 = candidateDomain.addCandidate("candidate 2")
      val candidate3 = candidateDomain.addCandidate("candidate 3")

      val testElection = electionDomain.addElection(Set(
        candidate1.id, candidate2.id, candidate3.id), 5)
      val readElection = electionDomain.getElectionResults(testElection.id)

      readElection.electionId should equal(testElection.id)
      readElection.maxVotesPerUser should equal(testElection.maxVotesPerUser)
      readElection.results.foreach(r => r.votes should equal(0))
      val readElectionCandidatesSeq: Set[Candidate] = readElection.results.map(r => r.candidate)
      val testCandidateSeq: Set[Candidate] = Set(candidate1, candidate2, candidate3)
      readElectionCandidatesSeq should equal(testCandidateSeq)

    }
  }


}
