package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.model.Vote

/**
  * Created by Ian on 3/9/17.
  */
trait VoteDao {

  def voteDao: VoteDao

  trait VoteDao extends CrudDao[Vote] {
    def getVotesForElection(electionId: UUID): collection.mutable.Map[UUID, Vote]

    def getVoteCountForElection(electionId: UUID): Int

    def getVoteCountForElectionAndUser(electionId: UUID, userId: UUID): Int

    def getVoteCountForElectionAndCandidate(electionId: UUID, candidateId: UUID): Int
  }

}
