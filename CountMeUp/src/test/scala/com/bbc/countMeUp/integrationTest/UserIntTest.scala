package com.bbc.countMeUp.integrationTest

import com.bbc.countMeUp.dao.impl._
import com.bbc.countMeUp.domain.{CandidateDomain, ElectionDomain, UserDomain}
import com.bbc.countMeUp.exception.ReachedElectionVoteLimitException
import org.scalatest.{FunSpec, Matchers}

class UserIntTest extends FunSpec with Matchers {
  val userDomain = new UserDomain with MongoUserDao with MongoVoteDao with MongoElectionDao
  val candidateDomain = new CandidateDomain with MongoCandidateDao
  val electionDomain = new ElectionDomain with MongoElectionDao with MongoVoteDao with  MongoCandidateDao

  describe("User round trip test"){
    it("I should be able to add a user, and read it back with no data loss"){
      val testUser = userDomain.addUser("test user")
      val readUser = userDomain.getUser(testUser.id)

      testUser should equal(readUser)
    }
  }

  describe("user voting tests"){
    it("a users vote should count in an election"){
      val candidate = candidateDomain.addCandidate("testCandidate")
      val election = electionDomain.addElection(Set(candidate.id), 1)
      val user = userDomain.addUser("test user")

      val electionResultsBeforVote = electionDomain.getElectionResults(election.id)
      electionResultsBeforVote.results.head.votes should equal(0)

      userDomain.voteInElection(user.id,election.id, candidate.id)

      val electionResultsAfterVote = electionDomain.getElectionResults(election.id)
      electionResultsAfterVote.results.head.votes should equal(1)
    }

    it("a user should not be allowed to vote more times then the election allows"){
      val candidate = candidateDomain.addCandidate("testCandidate")
      val election = electionDomain.addElection(Set(candidate.id), 1)
      val user = userDomain.addUser("test user")

      val electionResultsBeforVote = electionDomain.getElectionResults(election.id)
      electionResultsBeforVote.results.head.votes should equal(0)

      userDomain.voteInElection(user.id,election.id, candidate.id)

      intercept[ReachedElectionVoteLimitException]{
        userDomain.voteInElection(user.id,election.id, candidate.id)
      }

      val electionResultsAfterVote = electionDomain.getElectionResults(election.id)
      electionResultsAfterVote.results.head.votes should equal(1)
    }
  }

}
