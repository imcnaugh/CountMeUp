package com.bbc.countMeUp

import java.util.UUID

import com.bbc.countMeUp.dao.impl._
import com.bbc.countMeUp.domain.{CandidateDomain, ElectionDomain, UserDomain}
import org.scalatest.{FunSpec, Matchers}

import scala.util.Random

class perfTest extends FunSpec with Matchers{

  describe("testing"){
    it("perf test"){
      val userDomain = new UserDomain with InMemoryUserDao with InMemoryVoteDao with MongoElectionDao
      val electionDomain = new ElectionDomain with MongoElectionDao with InMemoryVoteDao with  MongoCandidateDao
      val candidateDomain = new CandidateDomain with MongoCandidateDao

      val candidate1 = candidateDomain.addCandidate("candidate1")
      val candidate2 = candidateDomain.addCandidate("candidate2")
      val candidate3 = candidateDomain.addCandidate("candidate3")
      val candidate4 = candidateDomain.addCandidate("candidate4")

      val candidateList = List(
        candidate1,
        candidate2,
        candidate3,
        candidate4
      )

      val maxVotesPerPerson = 5
      val election = electionDomain.addElection(
        Set(candidate1.id, candidate2.id, candidate3.id, candidate4.id),
        maxVotesPerPerson)

      var userVotes: Int = 0
      var currentUser = userDomain.addUser("user 0")

      for(x <- 1 to 100){
        if(userVotes >= maxVotesPerPerson){
          currentUser = userDomain.addUser("user " + x)
          userVotes = 0
        }

        val randomCandidateId: UUID = candidateList(Random.nextInt(candidateList.size)).id

        userVotes = userVotes + 1
        userDomain.voteInElection(currentUser.id, election.id, randomCandidateId)
        Console.print("\r" + x)
      }

      val startTime = System.currentTimeMillis()
      val electionResults = electionDomain.getElectionResults(election.id)
      val endTime = System.currentTimeMillis()

      Console.println(electionResults)
      Console.println((endTime - startTime) + " milli seconds runtime")
    }
  }

}
