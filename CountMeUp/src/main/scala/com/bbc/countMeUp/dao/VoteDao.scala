package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.model.Vote

/**
  * Created by Ian on 3/9/17.
  */
trait VoteDao {

  def voteDao: VoteDao

  trait VoteDao extends CrudDao[Vote] {
    /**
      * Searches data store for all votes in an election
      *
      * @param electionId
      * @return collection.mutable.Map[UUID, Vote]
      */
    def getVotesForElection(electionId: UUID): collection.mutable.Map[UUID, Vote]

    /**
      * Gives a count of total votes in election
      *
      * @param electionId
      * @return Int
      */
    def getVoteCountForElection(electionId: UUID): Int

    /**
      * Gives a count of how many times a user has voted in an election
      *
      * @param electionId
      * @param userId UUID
      * @return Int
      */
    def getVoteCountForElectionAndUser(electionId: UUID, userId: UUID): Int

    /**
      * Gives a count of how many votes a candidate has recived in an election
      *
      * @param electionId
      * @param candidateId
      * @return Int
      */
    def getVoteCountForElectionAndCandidate(electionId: UUID, candidateId: UUID): Int
  }

}
