package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{CandidateDao, ElectionDao, VoteDao}
import com.bbc.countMeUp.domain.util.IdUtils
import com.bbc.countMeUp.exception.EntityDoesNotExistException
import com.bbc.countMeUp.model.{Candidate, CandidateTally, Election, ElectionResults}

class ElectionDomain {
  this: ElectionDao with VoteDao with CandidateDao =>

  /**
    * Adds an election
    *
    * @param candidateIds
    * @param maxVotesPerUser
    * @return Election
    */
  def addElection(candidateIds: Set[UUID],
                  maxVotesPerUser: Int): Election = {
    //verify all candidates exists
    val candidates: Set[Candidate] = candidateIds.map(id =>{
      candidateDao.read(id) match {
        case c: Some[Candidate] => c.get
        case _ => throw new EntityDoesNotExistException(id)
      }})
    val newElection = Election(
      IdUtils.uniqueId(electionDao.read),
      candidates,
      maxVotesPerUser
    )
    electionDao.create(newElection)
    newElection
  }

  /**
    * Returns the current results of an election
    *
    * @param id
    * @return ElectionResults object
    */
  def getElectionResults(id: UUID): ElectionResults = {
    val election = electionDao.read(id) match {
      case e: Some[Election] => e.get
      case _ => throw new EntityDoesNotExistException(id)
    }

    ElectionResults(
      electionId = election.id,
      maxVotesPerUser = election.maxVotesPerUser,
      results = election.candidates.map(c => {
        CandidateTally(
          candidate = c,
          votes = voteDao.getVoteCountForElectionAndCandidate(id, c.id)
        )})
    )
  }
}
